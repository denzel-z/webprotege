package edu.stanford.bmir.protege.web.client.dispatch.actions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.termbuilder.CompetencyQuestionInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.ConceptList;

public class GenerateConceptsResult implements Result {
	
	protected Map<CompetencyQuestionInfo, ConceptList> map;
	
	protected GenerateConceptsResult() {
		
	}
	
	public GenerateConceptsResult(Map<CompetencyQuestionInfo, ConceptList> map) {
		this.map = new HashMap<CompetencyQuestionInfo, ConceptList>(map);
	}
	
	public Map<CompetencyQuestionInfo, ConceptList> getQuestionToConceptMap() {
		return Collections.unmodifiableMap(map);
	}
	
	/**
	 * For debugging purpose.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(CompetencyQuestionInfo key: map.keySet()) {
			sb.append("{");
			sb.append(key.toString());
			sb.append("->");
			sb.append(map.get(key).toString());
			sb.append("},");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
}