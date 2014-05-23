package edu.stanford.bmir.protege.web.client.dispatch.actions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;

public class RecommendConceptsResult implements Result {
	
	private HashSet<RecommendedConceptInfo> set;
	
	protected RecommendConceptsResult() {
		
	}
	
	public RecommendConceptsResult(HashSet<RecommendedConceptInfo> set) {
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