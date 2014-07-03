package edu.stanford.bmir.protege.web.client.ui.termbuilder.reference;

import com.google.common.base.Optional;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.SearchReferenceDocumentAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.SearchReferenceDocumentResult;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.CompetencyQuestionsManager;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.TermBuilderConstant;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;

/**
 * @author Yuhao Zhang
 */
public class ReferenceDocumentsViewImpl extends Composite implements ReferenceDocumentsView {

    private static ReferenceDocumentsViewImplUiBinder uiBinder = GWT
            .create(ReferenceDocumentsViewImplUiBinder.class);

    interface ReferenceDocumentsViewImplUiBinder extends UiBinder<Widget, ReferenceDocumentsViewImpl> {
    }

    private final String EMPTY_TABLE_LABEL = "There is no online documents to show.";

    private final String ANCHOR_TEXT_PREFFIX = "Click here to get online documents for: ";

    private final Project project;
    private ReferenceDocumentsViewPresenter presenter = null;
    private ReferenceDocumentsPortlet portlet = null;
    private final NoSelectionModel<ReferenceDocumentInfo> selectionModel;

    @UiField(provided=true) DataGrid<ReferenceDocumentInfo> dataGrid;
    @UiField Anchor anchor;
    @UiField HorizontalPanel anchorTextBar;

    public ReferenceDocumentsViewImpl(Project project, ReferenceDocumentsPortlet portlet) {
        ProvidesKey<ReferenceDocumentInfo> providesKey = new ProvidesKey<ReferenceDocumentInfo>() {
            @Override
            public Object getKey(ReferenceDocumentInfo item) {
                return item.getId();
            }
        };

        this.project = project;
        this.portlet = portlet;

        dataGrid = new DataGrid<ReferenceDocumentInfo>(10, providesKey);
        initWidget(uiBinder.createAndBindUi(this));
        dataGrid.setAutoHeaderRefreshDisabled(true);
        dataGrid.setEmptyTableWidget(new Label(this.EMPTY_TABLE_LABEL));
        selectionModel = new NoSelectionModel<ReferenceDocumentInfo>(providesKey);
        dataGrid.setSelectionModel(selectionModel);
        initTableColumns(selectionModel);

        anchor.setText(ANCHOR_TEXT_PREFFIX + project.getDisplayName());
        anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onSearch();
                anchor.setVisible(false);
                anchorTextBar.setVisible(false);
                dataGrid.setVisible(true);
            }
        });
    }

    public void initTableColumns(final SelectionModel<ReferenceDocumentInfo> selectionModel) {
        final SafeHtmlCell docCell = new SafeHtmlCell();
        Column<ReferenceDocumentInfo, SafeHtml> documentColumn = new Column<ReferenceDocumentInfo, SafeHtml> (docCell) {
            @Override
            public SafeHtml getValue(ReferenceDocumentInfo object) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                sb.appendHtmlConstant(object.getHyperlinkDocument());
                return sb.toSafeHtml();
            }
        };

        dataGrid.addColumn(documentColumn);
        dataGrid.setColumnWidth(documentColumn, 100, Style.Unit.PCT);
    }

    @Override
    public void refreshView(String className) {
        dataGrid.setVisible(false);
        anchorTextBar.setVisible(true);
        anchor.setVisible(true);
        if(className == null || className.equals(TermBuilderConstant.OWLTHING_NAME)) {
            anchor.setText(ANCHOR_TEXT_PREFFIX + project.getDisplayName());
        } else {
            anchor.setText(ANCHOR_TEXT_PREFFIX + className);
        }
    }

    @Override
    public void onSearch() {
        Optional<OWLEntityData> entity = portlet.getSelectedEntityData();
        if(!entity.isPresent()) return;
        String className = entity.get().getBrowserText();
        SearchReferenceDocumentAction action = new SearchReferenceDocumentAction(project.getProjectId(), className);
        DispatchServiceManager.get().execute(action, getSearchReferenceDocumentActionAsyncHandler());
    }

    private AsyncCallback<SearchReferenceDocumentResult> getSearchReferenceDocumentActionAsyncHandler() {
        return new AsyncCallback<SearchReferenceDocumentResult>() {
            @Override
            public void onFailure(Throwable caught) {
                try {
                    System.err.println("[Client] Search Reference Document Action Handling Error!");
                    throw caught;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(SearchReferenceDocumentResult result) {
                System.err.println("[Client] Recommend Concept Action Handling Succeed!");
                CompetencyQuestionsManager manager = project.getCompetencyQuestionsManager();
                manager.addRecommendedDocuments(result.getReferenceDocuments());
                presenter.reload();
            }
        };
    }

    @Override
    public void setDataProvider(AbstractDataProvider<ReferenceDocumentInfo> dataProvider) {
        dataProvider.addDataDisplay(dataGrid);
    }

    @Override
    public void setViewPresenter(ReferenceDocumentsViewPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public CompetencyQuestionsManager getCompetencyQuestionsManager() {
        return project.getCompetencyQuestionsManager();
    }

    @Override
    public Widget getWidget() {
        return this;
    }
}
