package edu.stanford.bmir.protege.web.client.ui.termbuilder.extract;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;

import edu.stanford.bmir.protege.web.client.ui.termbuilder.CompetencyQuestionsManager;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;
import edu.stanford.bmir.protege.web.shared.HasDispose;

/**
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public interface ExtractedConceptsView {
	
	public int canvasWidth = -1;
	public int canvasHeight = -1;

    public Widget getWidget();
    
    public CompetencyQuestionsManager getCompetencyQuestionsManager();
    
//    public int getCanvasHeight();
//    
//    public int getCanvasWidth();

}
