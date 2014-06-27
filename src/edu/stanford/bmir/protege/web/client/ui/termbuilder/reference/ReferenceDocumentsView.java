package edu.stanford.bmir.protege.web.client.ui.termbuilder.reference;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.CompetencyQuestionsManager;
import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;

/**
 * @author Yuhao Zhang
 */
public interface ReferenceDocumentsView {

    void setDataProvider(AbstractDataProvider<ReferenceDocumentInfo> dataProvider);
    void setViewPresenter(ReferenceDocumentsViewPresenter presenter);
    CompetencyQuestionsManager getCompetencyQuestionsManager();
    Widget getWidget();
    void onSearch();
}
