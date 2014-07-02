package edu.stanford.bmir.protege.web.client.ui.termbuilder.extract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesWithHierarchyAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesWithHierarchyResult;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectConfigurationListener;
import edu.stanford.bmir.protege.web.shared.termbuilder.ClassStringAndSuperClassPair;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;
import org.semanticweb.owlapi.model.OWLClass;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesResult;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.CompetencyQuestionsManager;
import edu.stanford.bmir.protege.web.shared.DataFactory;

/**
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class ExtractedConceptsViewImpl extends Composite implements ExtractedConceptsView {

	public static final String TERM_VIS_CANVAS_ID = "termVisCanvas";
	public int canvasWidth;
	public int canvasHeight;
	private Project project;
	
	private static ExtractedConceptsViewImplUiBinder uiBinder = GWT
			.create(ExtractedConceptsViewImplUiBinder.class);

	interface ExtractedConceptsViewImplUiBinder extends
			UiBinder<Widget, ExtractedConceptsViewImpl> {
	}
	
	@UiField Button selectAllButton;
	@UiField Button unselectAllButton;
	@UiField Button acceptButton;
	
	@UiField Widget termVisCanvas;
	
	public ExtractedConceptsViewImpl(Project project) {
		initWidget(uiBinder.createAndBindUi(this));
		termVisCanvas.getElement().setId(TERM_VIS_CANVAS_ID);
		this.project = project;
	}

//	@Override
//	public void onAttach() {
//		canvasHeight = termVisCanvas.getOffsetHeight();
//		canvasWidth = termVisCanvas.getOffsetWidth();
//	}

	@UiHandler("selectAllButton")
	void onelectAllButtonClick(ClickEvent e) {
		onSelectAll();
	}
	
	@UiHandler("unselectAllButton")
	void onUnelectAllButtonClick(ClickEvent e) {
		onUnselectAll();
	}
	
	@UiHandler("acceptButton")
	void onAcceptButtonClick(ClickEvent e) {
		onAccept();
	}
	
	private void onAccept() {
		//Get CQ Manager
		CompetencyQuestionsManager manager = project.getCompetencyQuestionsManager();
		JsArrayString selectedClasses = getSelectedClass();
		
		List<String> selectedClassesArray = new ArrayList<String>();
		for(int i=0; i<selectedClasses.length(); i++) {
			selectedClassesArray.add(selectedClasses.get(i));
		}

        List<ClassStringAndSuperClassPair> pairList = new ArrayList<ClassStringAndSuperClassPair>();

        populateAcceptedClassesInfo(selectedClassesArray, pairList);
		
		manager.addAcceptedConceptsFromString(selectedClassesArray);
		
		//Add classes into class tree
//		final OWLClass superCls = DataFactory.getOWLThing();
//		final Set<String> newClasses = new HashSet<String>(selectedClassesArray);
//		DispatchServiceManager.get().execute(new CreateClassesAction(project.getProjectId(), superCls, newClasses),
//				getCreateClassesActionAsyncHandler());
        DispatchServiceManager.get().execute(new CreateClassesWithHierarchyAction(project.getProjectId(), pairList),
                getCreateClassesWithHierarchyActionAsyncHandler());
	}

    private void populateAcceptedClassesInfo(List<String> selectedClassesArray, List<ClassStringAndSuperClassPair> pairList) {
        for(String className : selectedClassesArray) {
            // Add all selected classes as the subclass of owl:Thing class
            OWLClass superClass = DataFactory.getOWLThing();
            pairList.add(new ClassStringAndSuperClassPair(className, superClass));
        }
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

	private native void onSelectAll()/*-{
		console.log("Javascript selectAll() run!");
		$wnd.selectAll();
	}-*/;
	
	private native void onUnselectAll()/*-{
		console.log("Javascript unselectAll() run!");
		$wnd.unselectAll();
	}-*/;
	
	private native JsArrayString getSelectedClass()/*-{
		return $wnd.getSelectedClass();
	}-*/;
	
	@Override
    public Widget getWidget() {
        return this;
    }

	@Override
	public CompetencyQuestionsManager getCompetencyQuestionsManager() {
		return project.getCompetencyQuestionsManager();
	}
	
//	public int getCanvasWidth() {
//		return termVisCanvas.getOffsetWidth();
//	}
//	
//	public int getCanvasHeight() {
//		return termVisCanvas.getOffsetHeight();
//	}

}
