package edu.stanford.bmir.protege.web.client.ui.termbuilder.recommend;

import com.google.common.base.Optional;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesWithHierarchyAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesWithHierarchyResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.RecommendConceptsResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.RecommendForSingleConceptAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.RecommendForSingleConceptResult;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.AcceptedConceptsManager;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.CompetencyQuestionsManager;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.RecommendedConceptsManager;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.TermBuilderConstant;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.TermBuilderManagerBoard;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RecommendedConceptsListViewImpl extends Composite implements RecommendedConceptsListView {

	private final Project project;
	private  RecommendedConceptsListViewPresenter presenter = null;
    private RecommendedConceptsPortlet portlet = null;

    private AcceptedConceptsManager acceptedConceptsManager;
    private RecommendedConceptsManager recommendedConceptsManager;
	
	private static RecommendedConceptsListViewImplUiBinder uiBinder = GWT
			.create(RecommendedConceptsListViewImplUiBinder.class);

	interface RecommendedConceptsListViewImplUiBinder extends
			UiBinder<Widget, RecommendedConceptsListViewImpl> {
	}
	
	private final String CANDIDATE_CONCEPT_COL_TITLE = "Candidate Concepts";
	private final String RELATION_COL_TITLE = "Relation to Existing Concepts";
	private final String EMPTY_TABLE_LABEL = "There is no recommendation to show.";

    private final String ANCHOR_TEXT_PREFFIX = "Click here to search WordNet for related concepts to: ";
	
	final MultiSelectionModel<RecommendedConceptInfo> selectionModel;
	
	@UiField(provided=true) DataGrid<RecommendedConceptInfo> dataGrid;
    @UiField HorizontalPanel buttonBar;
    @UiField HorizontalPanel anchorTextBar;
	@UiField Button acceptButton;
    @UiField Anchor textAnchor;

	public RecommendedConceptsListViewImpl(Project project, RecommendedConceptsPortlet portlet) {
		ProvidesKey<RecommendedConceptInfo> providesKey = new ProvidesKey<RecommendedConceptInfo>() {
			@Override
			public Object getKey(RecommendedConceptInfo item) {
				return item.getId();
			}
		};
		
		this.project = project;
        this.portlet = portlet;
		
		dataGrid = new DataGrid<RecommendedConceptInfo>(1000, providesKey);
		initWidget(uiBinder.createAndBindUi(this));
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label(this.EMPTY_TABLE_LABEL));
		selectionModel =
				new MultiSelectionModel<RecommendedConceptInfo>(providesKey);
        dataGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager.<RecommendedConceptInfo>createDefaultManager());
		initTableColumns(selectionModel);

        textAnchor.setText(ANCHOR_TEXT_PREFFIX + project.getDisplayName());
        textAnchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onRefresh();
                textAnchor.setVisible(false);
                anchorTextBar.setVisible(false);
                dataGrid.setVisible(true);
                buttonBar.setVisible(true);
                acceptButton.setVisible(true);
            }
        });

        acceptedConceptsManager = project.getTermBuilderManagerBoard().getAcceptedConceptsManager();
        recommendedConceptsManager = project.getTermBuilderManagerBoard().getRecommendedConceptsManager();
	}
	
	private void initTableColumns(
			final SelectionModel<RecommendedConceptInfo> selectionModel) {

        final SafeHtmlCell conceptCell = new SafeHtmlCell();
        Column<RecommendedConceptInfo, SafeHtml> candidateConceptColumn = new Column<RecommendedConceptInfo, SafeHtml>(conceptCell) {
            @Override
            public SafeHtml getValue(RecommendedConceptInfo object) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                sb.appendHtmlConstant(object.getHTMLRecommendedConceptDescription());
                return sb.toSafeHtml();
            }
        };
		dataGrid.addColumn(candidateConceptColumn, this.CANDIDATE_CONCEPT_COL_TITLE);
		dataGrid.setColumnWidth(candidateConceptColumn, 40, Unit.PCT);

        final SafeHtmlCell relationCell = new SafeHtmlCell();
        Column<RecommendedConceptInfo, SafeHtml> relationColumn = new Column<RecommendedConceptInfo, SafeHtml>(relationCell) {
            @Override
            public SafeHtml getValue(RecommendedConceptInfo object) {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                sb.appendHtmlConstant(object.getHTMLConceptRelationDescription());
                return sb.toSafeHtml();
            }
        };
		dataGrid.addColumn(relationColumn, this.RELATION_COL_TITLE);
		dataGrid.setColumnWidth(relationColumn, 60, Unit.PCT);
	}

    @Override
    public void refreshView(String className) {
        acceptButton.setVisible(false);
        buttonBar.setVisible(false);
        dataGrid.setVisible(false);
        anchorTextBar.setVisible(true);
        textAnchor.setVisible(true);
        if(className == null || className.equals(TermBuilderConstant.OWLTHING_NAME)) {
            textAnchor.setText(ANCHOR_TEXT_PREFFIX + project.getDisplayName());
        } else {
            textAnchor.setText(ANCHOR_TEXT_PREFFIX + className);
        }
    }

	@Override
    public Widget getWidget() {
        return this;
    }
	
	private void onRefresh() {
        // New implementation that only recommend for selected conceptName
        Optional<OWLEntityData> entity = portlet.getSelectedEntityData();
        if(!entity.isPresent()) return;
        // Get class name and IRI
        String className = entity.get().getBrowserText();
        IRI classIRI = null;
        if(className.equals(TermBuilderConstant.OWLTHING_NAME)) {
            className = project.getDisplayName();
        } else {
            classIRI = entity.get().getEntity().getIRI();
        }
        RecommendForSingleConceptAction action = new RecommendForSingleConceptAction(project.getProjectId(), new Concept(className, classIRI));
        DispatchServiceManager.get().execute(action, getRecommendForSingleConceptActionAsyncHandler());
	}

    private AsyncCallback<RecommendForSingleConceptResult> getRecommendForSingleConceptActionAsyncHandler() {
        return new AsyncCallback<RecommendForSingleConceptResult>() {

            @Override
            public void onFailure(Throwable caught) {
                try {
                    System.err.println("[Client] Recommend Concept Action Handling Error!");
                    throw caught;
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(RecommendForSingleConceptResult result) {
                System.err.println("[Client] Recommend Concept Action Handling Succeed!");
                recommendedConceptsManager.addRecommendedConcepts(result.getRecommendedConcepts());
//                EventBusManager.getManager().postEvent(new SourceConceptChangedEvent(project.getProjectId()));
                presenter.reload();
            }
        };
    }
	
	private AsyncCallback<RecommendConceptsResult> getRecommendConceptsActionAsyncHandler() {
		return new AsyncCallback<RecommendConceptsResult>() {
			
			@Override
			public void onFailure(Throwable caught) {
				try {
					System.err.println("[Client] Recommend Concept Action Handling Error!");
					throw caught;
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onSuccess(RecommendConceptsResult result) {
				System.err.println("[Client] Recommend Concept Action Handling Succeed!");
				recommendedConceptsManager.addRecommendedConcepts(result.getRecommendedConcepts());
				presenter.reload();
			}
			
		};
	}

	@UiHandler("acceptButton")
	void onAcceptButtonClick(ClickEvent event) {
		onAccept();
	}
	
	private void onAccept() {
		Set<RecommendedConceptInfo> selectedSet = selectionModel.getSelectedSet();
        if(selectedSet.isEmpty()) return;

		List<String> selectedClassesArray = new ArrayList<String>();

        // The new implementation
        OWLClass srcConcept = getSrcConcept(selectedSet);
        populateAcceptedClassesToArray(selectedSet, selectedClassesArray);
		
		acceptedConceptsManager.addAcceptedConceptsFromString(selectedClassesArray);
		
		/*
		//Add classes into class tree, naive implementation simply add all concepts as the subclass of Thing
		final OWLClass superCls = DataFactory.getOWLThing();
		final Set<String> newClasses = new HashSet<String>(selectedClassesArray);
		DispatchServiceManager.get().execute(new CreateClassesAction(project.getProjectId(), superCls, newClasses),
				getCreateClassesActionAsyncHandler());
	    */

        DispatchServiceManager.get().execute(new CreateClassesWithHierarchyAction(project.getProjectId(), selectedSet, srcConcept),
                getCreateClassesWithHierarchyActionAsyncHandler());

	}

    private OWLClass getSrcConcept(Set<RecommendedConceptInfo> selectedSet) {
        if(selectedSet.isEmpty()) return null;

        Iterator<RecommendedConceptInfo> iter = selectedSet.iterator();
        Concept c = iter.next().getSrcConcept();
        OWLClass srcConcept = DataFactory.getOWLClass(c.getIRI());
        return srcConcept;
    }

    private void populateAcceptedClassesToArray(Set<RecommendedConceptInfo> selectedSet, List<String> selectedClassesArray) {
        for(RecommendedConceptInfo info: selectedSet) {
            String conceptName = info.getRecommendedConcept().getConceptName();
            selectedClassesArray.add(conceptName);
        }
    }

	private AsyncCallback<CreateClassesResult> getCreateClassesActionAsyncHandler() {
        return new AsyncCallback<CreateClassesResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("There was a problem creating the classes.  Please try again.");
            }

            @Override
            public void onSuccess(CreateClassesResult result) {
            	System.err.println("Classes added into the tree!");
            }
        };
    }

    private AsyncCallback<CreateClassesWithHierarchyResult> getCreateClassesWithHierarchyActionAsyncHandler() {
        return new AsyncCallback<CreateClassesWithHierarchyResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("There was a problem creating the classes.  Please try again.");
            }

            @Override
            public void onSuccess(CreateClassesWithHierarchyResult result) {
                System.err.println("Classes added into the tree!");
            }
        };
    }

	@Override
	public void setDataProvider(
			AbstractDataProvider<RecommendedConceptInfo> dataProvider) {
		dataProvider.addDataDisplay(dataGrid);
	}
	
	@Override
	public void setViewPresenter(RecommendedConceptsListViewPresenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public TermBuilderManagerBoard getTermBuilderManagerBoard() {
		return project.getTermBuilderManagerBoard();
	}
}
