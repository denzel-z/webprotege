package edu.stanford.bmir.protege.web.client.ui.termbuilder.extract;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class ExtractedConceptsViewImpl extends Composite implements ExtractedConceptsView {

	public static final String TERM_VIS_CANVAS_ID = "termVisCanvas";
	public int canvasWidth;
	public int canvasHeight;
	
	private static ExtractedConceptsViewImplUiBinder uiBinder = GWT
			.create(ExtractedConceptsViewImplUiBinder.class);

	interface ExtractedConceptsViewImplUiBinder extends
			UiBinder<Widget, ExtractedConceptsViewImpl> {
	}
	
	@UiField Button selectAllButton;
	@UiField Button unselectAllButton;
	@UiField Button acceptButton;
	
	@UiField Widget termVisCanvas;
	
	public ExtractedConceptsViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		termVisCanvas.getElement().setId(TERM_VIS_CANVAS_ID);
	}

//	@Override
//	public void onAttach() {
//		canvasHeight = termVisCanvas.getOffsetHeight();
//		canvasWidth = termVisCanvas.getOffsetWidth();
//	}

	@UiHandler("selectAllButton")
	void onelectAllButtonClick(ClickEvent e) {
		//TODO: Add click event handler
		Window.alert("Select All!");
	}
	
	@UiHandler("unselectAllButton")
	void onUnelectAllButtonClick(ClickEvent e) {
		//TODO: Add click event handler
		Window.alert("Unselect All!");
	}
	
	@UiHandler("acceptButton")
	void onAcceptButtonClick(ClickEvent e) {
		//TODO: Add click event handler
		Window.alert("Accept All!");
	}
	
	@Override
    public Widget getWidget() {
        return this;
    }
	
//	public int getCanvasWidth() {
//		return termVisCanvas.getOffsetWidth();
//	}
//	
//	public int getCanvasHeight() {
//		return termVisCanvas.getOffsetHeight();
//	}

}
