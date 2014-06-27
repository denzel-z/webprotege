package edu.stanford.bmir.protege.web.client.ui.termbuilder.extract;

import java.util.Set;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.web.bindery.event.shared.HandlerRegistration;

import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.CompetencyQuestionsManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
import edu.stanford.bmir.protege.web.shared.termbuilder.ExtractedConceptsChangedEvent;
import edu.stanford.bmir.protege.web.shared.termbuilder.ExtractedConceptsChangedHandler;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;

/**
 * The presenter of Extracted Concept Portlet to connect data model with 
 * table display.
 * TODO: It is very incomplete right now. Will add the model in later.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class ExtractedConceptsViewPresenter implements HasDispose {
	
	private ProjectId projectId;
	private ExtractedConceptsView view;
	
	// For the purpose of deregister a handler.
	private HandlerRegistration registration;
	
	private int canvasWidth;
	private int canvasHeight;
	
	//Here need to add a server side interface to send in the data.
	
	public ExtractedConceptsViewPresenter(ProjectId projectId, ExtractedConceptsView view) {
		this.view = view;
		this.projectId = projectId;
		
		registration = EventBusManager.getManager().registerHandlerToProject(projectId, 
				ExtractedConceptsChangedEvent.TYPE, new ExtractedConceptsChangedHandler() {
					@Override
					public void handleExtractedConceptsChanged(
							ExtractedConceptsChangedEvent event) {
						System.err.println("[Client] ExtractedConceptsChanged handler triggered!");
						reloadWithConcepts();
					}
		});
		//Here probably need some code.
	}
	
	public Widget getWidget() {
		return view.getWidget();
	}
	
	public void reload() {
//		canvasWidth = view.getCanvasWidth();
//		canvasHeight = view.getCanvasHeight();
		//First clear visualization area
		clearTermVis();
		//Then initialize again
		initializeTermVis();
		System.err.println("Canvas initialize finished!");
		//Here is some code to get data from the service, and store the data
		//into dataProvider
	}
	
	public void reloadWithConcepts() {
		CompetencyQuestionsManager manager = view.getCompetencyQuestionsManager();
		Set<Concept> conceptSet = manager.getExtractedConcepts();
		JsArrayString conceptArray = (JsArrayString) JsArrayString.createArray();
		for(Concept c:conceptSet) {
			conceptArray.push(c.getConceptName());
		}
		clearTermVis();
		initializeTermVisWithConcepts(conceptArray);
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
	
	private native void initializeTermVisWithConcepts(JsArrayString array)/*-{
		console.log("Embeded Javascript initializeTermVisWithConcept() run!");
		$wnd.initializeTermVisWithConcepts(array);
}-*/;
	
	/**
	 * A local call to the term_vis.js function, which clear the SVG
	 * area that includes extracted class visualization.
	 */
	private native void clearTermVis()/*-{
		console.log("Embeded Javascript clearTermVis() run!");
		$wnd.clearTermVis();
	}-*/;

	@Override
	public void dispose() {
		registration.removeHandler();
	}
	
	//Here probably need some other methods.
}