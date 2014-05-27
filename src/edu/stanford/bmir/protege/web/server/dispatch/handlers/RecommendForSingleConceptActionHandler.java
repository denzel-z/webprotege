package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.RecommendForSingleConceptAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.RecommendForSingleConceptResult;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.wordnet.WordNetSearch;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/**
 * @author Yuhao Zhang
 */
public class RecommendForSingleConceptActionHandler implements
        ActionHandler<RecommendForSingleConceptAction, RecommendForSingleConceptResult> {
    @Override
    public Class<RecommendForSingleConceptAction> getActionClass() {
        return RecommendForSingleConceptAction.class;
    }

    @Override
    public RequestValidator<RecommendForSingleConceptAction> getRequestValidator(RecommendForSingleConceptAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    public RecommendForSingleConceptResult execute(RecommendForSingleConceptAction action, ExecutionContext executionContext) {
        Concept concept = action.getConcept();

        HashSet<RecommendedConceptInfo> resultSet = recommendConceptsForSingleConcept(concept);

        RecommendForSingleConceptResult result = new RecommendForSingleConceptResult(resultSet);
        return result;
    }

    public HashSet<RecommendedConceptInfo> recommendConceptsForSingleConcept(Concept concept) {
        HashSet<RecommendedConceptInfo> result = new HashSet<RecommendedConceptInfo>();

        /**
         * Use WordNet to recommend concepts
         */
        WordNetSearch search;
        List<RecommendedConceptInfo> rec = null;
        try {
            search = WordNetSearch.getInstance();
            rec = search.searchConcept(concept);
            result.addAll(rec);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
