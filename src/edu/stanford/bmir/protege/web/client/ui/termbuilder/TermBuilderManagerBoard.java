package edu.stanford.bmir.protege.web.client.ui.termbuilder;

import com.gwtext.client.core.Ext;

/**
 * @author Yuhao Zhang
 */
public class TermBuilderManagerBoard {

    private CompetencyQuestionsManager competencyQuestionsManager;
    private ExtractedConceptsManager extractedConceptsManager;
    private AcceptedConceptsManager acceptedConceptsManager;
    private RecommendedConceptsManager recommendedConceptsManager;
    private ReferenceDocumentsManager referenceDocumentsManager;

    public TermBuilderManagerBoard() {
        competencyQuestionsManager = new CompetencyQuestionsManager();
        extractedConceptsManager = new ExtractedConceptsManager();
        acceptedConceptsManager = new AcceptedConceptsManager();
        recommendedConceptsManager = new RecommendedConceptsManager();
        referenceDocumentsManager = new ReferenceDocumentsManager();
    }

    public CompetencyQuestionsManager getCompetencyQuestionsManager() {
        return competencyQuestionsManager;
    }

    public ExtractedConceptsManager getExtractedConceptsManager() {
        return extractedConceptsManager;
    }

    public AcceptedConceptsManager getAcceptedConceptsManager() {
        return acceptedConceptsManager;
    }

    public RecommendedConceptsManager getRecommendedConceptsManager() {
        return recommendedConceptsManager;
    }

    public ReferenceDocumentsManager getReferenceDocumentsManager() {
        return referenceDocumentsManager;
    }

}
