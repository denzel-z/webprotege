package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.stanford.bmir.protege.web.client.dispatch.actions.RecommendConceptsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.RecommendConceptsResult;
import edu.stanford.bmir.protege.web.server.dispatch.ActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.NullValidator;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.conceptnet.ConceptNetSearch;
import edu.stanford.bmir.protege.web.shared.termbuilder.wordnet.WordNetSearch;

public class RecommendConceptsActionHandler implements
	ActionHandler<RecommendConceptsAction, RecommendConceptsResult> {

	@Override
	public Class<RecommendConceptsAction> getActionClass() {
		return RecommendConceptsAction.class;
	}

	@Override
	public RequestValidator<RecommendConceptsAction> getRequestValidator(
			RecommendConceptsAction action, RequestContext requestContext) {
		return new NullValidator<RecommendConceptsAction, RecommendConceptsResult>();
	}

	@Override
	public RecommendConceptsResult execute(RecommendConceptsAction action,
			ExecutionContext executionContext) {
		Set<Concept> conceptSet = action.getConcepts();
		
		HashSet<RecommendedConceptInfo> resultSet = recommendConcepts(conceptSet);
		
		RecommendConceptsResult result = new RecommendConceptsResult(resultSet);
		return result;
	}
	
	public HashSet<RecommendedConceptInfo> recommendConcepts(Set<Concept> conceptSet) {
		HashSet<RecommendedConceptInfo> result = new HashSet<RecommendedConceptInfo>();

		/* Use ConceptNet to recommend concepts
		 * 
		for(Concept c: conceptSet) {
			ConceptNetSearch search = new ConceptNetSearch(c);
			//Here I use catch instead of throwing exceptions
			try {
				search.searchAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.addAll(search.getRecommendedConcepts());
		}
		*/
		
		/**
		 * Use WordNet to recommend concepts
		 */
		WordNetSearch search;
		List<RecommendedConceptInfo> rec = null;
		try {
			search = WordNetSearch.getInstance();
			for(Concept c: conceptSet) {
				//Here I use catch instead of throwing exceptions
				rec = search.searchConcept(c);
				result.addAll(rec);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
}