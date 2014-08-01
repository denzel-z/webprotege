package edu.stanford.bmir.protege.web.client.ui.termbuilder.extract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesWithHierarchyAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesWithHierarchyResult;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.AcceptedConceptsManager;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.TermBuilderConstant;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.TermBuilderManagerBoard;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectConfigurationListener;
import edu.stanford.bmir.protege.web.shared.termbuilder.ClassStringAndSuperClassPair;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
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

    private AcceptedConceptsManager acceptedConceptsManager;
	
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

        acceptedConceptsManager = getTermBuilderManagerBoard().getAcceptedConceptsManager();
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
		JsArrayString selectedClasses = getSelectedClass();
		
		List<String> selectedClassesArray = new ArrayList<String>();
		for(int i=0; i<selectedClasses.length(); i++) {
			selectedClassesArray.add(selectedClasses.get(i));
		}

        Set<RecommendedConceptInfo> selectedSet = new HashSet<RecommendedConceptInfo>();

//        List<ClassStringAndSuperClassPair> pairList = new ArrayList<ClassStringAndSuperClassPair>();
//        populateAcceptedClassesInfo(selectedClassesArray, pairList);

		populateSelectedSet(selectedSet, selectedClassesArray);

		acceptedConceptsManager.addAcceptedConceptsFromString(selectedClassesArray);

        OWLClass thingClass = DataFactory.getOWLThing();

        DispatchServiceManager.get().execute(new CreateClassesWithHierarchyAction(project.getProjectId(), selectedSet, thingClass),
                getCreateClassesWithHierarchyActionAsyncHandler());
	}

    private void populateSelectedSet(Set<RecommendedConceptInfo> selectedSet, List<String> selectedClassesArray) {
        for(String c : selectedClassesArray) {
            RecommendedConceptInfo info = new RecommendedConceptInfo(new Concept(TermBuilderConstant.OWLTHING_NAME), new Concept(c),
                    RecommendedConceptInfo.ConceptRelation.SUBCLASS_OF);
            selectedSet.add(info);
        }
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
	public TermBuilderManagerBoard getTermBuilderManagerBoard() {
        return project.getTermBuilderManagerBoard();
    }
	
//	public int getCanvasWidth() {
//		return termVisCanvas.getOffsetWidth();
//	}
//	
//	public int getCanvasHeight() {
//		return termVisCanvas.getOffsetHeight();
//	}

}
