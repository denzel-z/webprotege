package edu.stanford.bmir.protege.web.client.ui.termbuilder.extract;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;

/**
 * The presenter of Extracted Concept Portlet to connect data model with 
 * table display.
 * TODO: It is very incomplete right now. Will add the model in later.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class ExtractedConceptsViewPresenter {
	
	private ProjectId projectId;
	private ExtractedConceptsView view;
	
	private int canvasWidth;
	private int canvasHeight;
	
	//Here need to add a server side interface to send in the data.
	
	public ExtractedConceptsViewPresenter(ProjectId projectId, ExtractedConceptsView view) {
		this.view = view;
		this.projectId = projectId;
		//Here probably need some code.
	}
	
	public Widget getWidget() {
		return view.getWidget();
	}
	
	public void reload() {
//		canvasWidth = view.getCanvasWidth();
//		canvasHeight = view.getCanvasHeight();
//		System.err.println("Get Canvas Width " + view.canvasWidth);
//		System.err.println("Get Canvas Height " + view.canvasHeight);
		//First clear visualization area
		clearTermVis();
		//Then initialize again
//		initializeTermVis(canvasWidth, canvasHeight);
		initializeTermVis();
		System.err.println("Canvas initialize finished!");
		//Here is some code to get data from the service, and store the data
		//into dataProvider
	}
	
	/**
	 * A local call to the term_vis.js function, which initialize the SVG
	 * area to do extracted class visualization.
	 */
//	private native void initializeTermVis(int canvasWidth, int canvasHeight)/*-{
//		console.log("Embeded Javascript initializeTermVis() run!");
//		$wnd.initializeTermVis(canvasWidth, canvasHeight);
//	}-*/;
	
	private native void initializeTermVis()/*-{
		console.log("Embeded Javascript initializeTermVis() run!");
		$wnd.initializeTermVis();
	}-*/;
	
	/**
	 * A local call to the term_vis.js function, which clear the SVG
	 * area that includes extracted class visualization.
	 */
	private native void clearTermVis()/*-{
		console.log("Embeded Javascript clearTermVis() run!");
		$wnd.clearTermVis();
	}-*/;
	
	//Here probably need some other methods.
}