package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;

/**
 * @author Yuhao Zhang
 */
public class SearchReferenceDocumentAction extends AbstractHasProjectAction<SearchReferenceDocumentResult> {
    private String conceptName;

    public SearchReferenceDocumentAction(String conceptName) {
        this.conceptName = conceptName;
    }

    public SearchReferenceDocumentAction(ProjectId projectId, String conceptName) {
        super(projectId);
        this.conceptName = conceptName;
    }

    // For serialization
    protected  SearchReferenceDocumentAction() {}

    public String getConceptName() {
        return conceptName;
    }

}
