package edu.stanford.bmir.protege.web.client.ui.termbuilder.question;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.termbuilder.CompetencyQuestionInfo;

/**
 * The presenter of Competency Question Portlet to connect data model with 
 * table display.
 * TODO: It is very incomplete right now. Will add the model in later.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class CompetencyQuestionsViewPresenter {
	
	private ProjectId projectId;
	private CompetencyQuestionsView view;
	private ListDataProvider<CompetencyQuestionInfo> dataProvider;
	
	public CompetencyQuestionsViewPresenter(ProjectId projectId, CompetencyQuestionsView view) {
		this.view = view;
		this.projectId = projectId;
		dataProvider = new ListDataProvider<CompetencyQuestionInfo>();
		view.setDataProvider(dataProvider);
		
		//Here probably need some code.
	}
	
	public Widget getWidget() {
		return view.getWidget();
	}
	
	public void reload() {
		//Here is some code to get data from the service, and store the data
		//into dataProvider
	}
}
