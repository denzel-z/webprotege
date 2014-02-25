package edu.stanford.bmir.protege.web.client.ui.termbuilder.question;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;

import edu.stanford.bmir.protege.web.shared.termbuilder.CompetencyQuestionInfo;

public interface CompetencyQuestionsView {

    void setDataProvider(AbstractDataProvider<CompetencyQuestionInfo> dataProvider);

    Widget getWidget();

}
