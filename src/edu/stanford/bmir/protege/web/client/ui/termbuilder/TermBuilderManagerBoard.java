package edu.stanford.bmir.protege.web.client.ui.termbuilder;

import com.gwtext.client.core.Ext;

/**
 * @author Yuhao Zhang
 */
public class TermBuilderManagerBoard {

    private CompetencyQuestionsManager competencyQuestionsManager;
    private ExtractedConceptsManager extractedConceptsManager;

    public TermBuilderManagerBoard() {
        competencyQuestionsManager = new CompetencyQuestionsManager();
        extractedConceptsManager = new ExtractedConceptsManager();
    }

    public CompetencyQuestionsManager getCompetencyQuestionsManager() {
        return competencyQuestionsManager;
    }

    public ExtractedConceptsManager getExtractedConceptsManager() {
        return extractedConceptsManager;
    }

}
