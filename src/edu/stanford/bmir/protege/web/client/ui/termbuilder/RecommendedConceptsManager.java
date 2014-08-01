package edu.stanford.bmir.protege.web.client.ui.termbuilder;

import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Yuhao Zhang
 */
public class RecommendedConceptsManager {

    private Set<RecommendedConceptInfo> recommendedConceptSet;

    public RecommendedConceptsManager() {
        recommendedConceptSet = new HashSet<RecommendedConceptInfo>();
    }

    public void addRecommendedConcepts(Collection<RecommendedConceptInfo> c) {
        recommendedConceptSet.clear();
        recommendedConceptSet.addAll(c);
    }

    public List<RecommendedConceptInfo> getRecommendedConcepts() {
        List<RecommendedConceptInfo> result = new ArrayList<RecommendedConceptInfo>();
        result.addAll(recommendedConceptSet);
        Collections.sort(result);
        return Collections.unmodifiableList(result);
    }

}
