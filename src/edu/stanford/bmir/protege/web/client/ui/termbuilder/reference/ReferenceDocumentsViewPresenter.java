package edu.stanford.bmir.protege.web.client.ui.termbuilder.reference;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.SourceConceptChangedEvent;
import edu.stanford.bmir.protege.web.shared.termbuilder.SourceConceptChangedHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yuhao Zhang
 */
public class ReferenceDocumentsViewPresenter {

    private ProjectId projectId;
    private ReferenceDocumentsView view;
    private ListDataProvider<ReferenceDocumentInfo> dataProvider;

    private HandlerRegistration registration;

    public ReferenceDocumentsViewPresenter(ProjectId projectId, ReferenceDocumentsView view) {
        this.view = view;
        this.projectId = projectId;

        registration = EventBusManager.getManager().registerHandlerToProject(projectId,
                SourceConceptChangedEvent.TYPE, new SourceConceptChangedHandler() {
                    @Override
                    public void handleSourceConceptChanged(SourceConceptChangedEvent event) {
                        System.err.println("[Client] SourceConceptChanged handler triggered!");
                        searchAndReload();
                    }
        });

        dataProvider = new ListDataProvider<ReferenceDocumentInfo>();
        view.setDataProvider(dataProvider);

        view.setViewPresenter(this);
    }

    public void reload() {
        List<ReferenceDocumentInfo> listData = new ArrayList<ReferenceDocumentInfo>(view.getCompetencyQuestionsManager().getRecommendedDocuments());
        setListData(listData);
    }

    private void setListData(List<ReferenceDocumentInfo> list) {
        List<ReferenceDocumentInfo> data = dataProvider.getList();
        data.clear();
        data.addAll(list);
        dataProvider.flush();
    }

    public void searchAndReload() {
        view.onSearch();
    }

    public Widget getWidget() {
        return view.getWidget();
    }
}
