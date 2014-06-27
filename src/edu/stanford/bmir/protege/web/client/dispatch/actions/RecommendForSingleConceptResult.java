package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This action is similar to RecommendedConceptsResult, save for that it store results
 * for a single conceptName instead of concepts in a set.
 *
 * @author Yuhao Zhang
 */
public class RecommendForSingleConceptResult implements Result {
    private HashSet<RecommendedConceptInfo> set;

    protected RecommendForSingleConceptResult() {

    }

    public RecommendForSingleConceptResult(HashSet<RecommendedConceptInfo> set) {
        this.set = set;
    }

    public Set<RecommendedConceptInfo> getRecommendedConcepts() {
        return Collections.unmodifiableSet(set);
    }

    @Override
    public String toString() {
        return set.toString();
    }
}
