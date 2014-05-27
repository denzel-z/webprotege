package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This action is similar to RecommendedConceptsAction, save for that it recommends
 * for a single concept instead of concepts in a set.
 *
 * @author Yuhao Zhang
 */
public class RecommendForSingleConceptAction extends AbstractHasProjectAction<RecommendForSingleConceptResult> {
    private Concept concept;

    public RecommendForSingleConceptAction(Concept concept) {
        this.concept = concept;
    }

    public RecommendForSingleConceptAction(ProjectId projectId, Concept concept) {
        super(projectId);
        this.concept = concept;
    }

    // For serialization
    protected RecommendForSingleConceptAction() {}

    public Concept getConcept() {
        return concept;
    }
}
