package edu.stanford.bmir.protege.web.client.ui.termbuilder.recommend;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;

import edu.stanford.bmir.protege.web.client.ui.termbuilder.CompetencyQuestionsManager;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;
import edu.stanford.bmir.protege.web.shared.HasDispose;

public interface RecommendedConceptsListView {

    void setDataProvider(AbstractDataProvider<RecommendedConceptInfo> dataProvider);

    Widget getWidget();

	public CompetencyQuestionsManager getCompetencyQuestionsManager();

	void setViewPresenter(RecommendedConceptsListViewPresenter presenter);

}
