package edu.stanford.bmir.protege.web.client.ui.termbuilder.extract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.data.Node;
import com.gwtext.client.widgets.tree.TreeNode;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesResult;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.SubclassEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
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
		
		manager.addAcceptedConceptsFromString(selectedClassesArray);
		
		//Add classes into class tree
		final OWLClass superCls = DataFactory.getOWLThing();
		final Set<String> newClasses = new HashSet<String>(selectedClassesArray);
		DispatchServiceManager.get().execute(new CreateClassesAction(project.getProjectId(), superCls, newClasses),
				getCreateClassesActionAsyncHandler());
	}
	
	private AsyncCallback<CreateClassesResult> getCreateClassesActionAsyncHandler() {
        return new AsyncCallback<CreateClassesResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("There was a problem creating the classes.  Please try again.");
            }

            @Override
            public void onSuccess(CreateClassesResult result) {
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
