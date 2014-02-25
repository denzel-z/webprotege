package edu.stanford.bmir.protege.web.client.ui.termbuilder.recommend;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;

/**
 * The presenter of Recommended Concept Portlet to connect data model with 
 * table display.
 * TODO: It is very incomplete right now. Will add the model in later.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class RecommendedConceptsListViewPresenter {
	
	private ProjectId projectId;
	private RecommendedConceptsListView view;
	private ListDataProvider<RecommendedConceptInfo> dataProvider;
	
	//Here need to add a server side interface to send in the data.
	
	public RecommendedConceptsListViewPresenter(ProjectId projectId, RecommendedConceptsListView view) {
		this.view = view;
		this.projectId = projectId;
		dataProvider = new ListDataProvider<RecommendedConceptInfo>();
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
	
	//Here probably need some other methods.
}