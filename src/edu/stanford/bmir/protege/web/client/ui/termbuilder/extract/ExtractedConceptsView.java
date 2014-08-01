package edu.stanford.bmir.protege.web.client.ui.termbuilder.extract;

import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.TermBuilderManagerBoard;

/**
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public interface ExtractedConceptsView {
	
	public int canvasWidth = -1;
	public int canvasHeight = -1;

    public Widget getWidget();

    public TermBuilderManagerBoard getTermBuilderManagerBoard();
    
//    public int getCanvasHeight();
//    
//    public int getCanvasWidth();

}
