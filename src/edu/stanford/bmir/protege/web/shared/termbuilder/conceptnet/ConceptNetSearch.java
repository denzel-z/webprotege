package edu.stanford.bmir.protege.web.shared.termbuilder.conceptnet;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo.ConceptRelation;

public class ConceptNetSearch {
	
	public Concept srcConcept;
	public String normalizedSrcConceptName;
	
	public Set<RecommendedConceptInfo> recommendedConcepts;
	
	private HttpConnection con;
	private Gson gson;
	private Pattern conceptInUriPattern;
	
	/*
	 * Some properties to set
	 */
	private int SEARCH_RESULT_LIMIT = 5;
	private String ROOT_URL = "http://conceptnet5.media.mit.edu/data/5.1/search";
	
	public ConceptNetSearch(Concept srcConcept) {
		this.srcConcept = srcConcept;
		recommendedConcepts = new HashSet<RecommendedConceptInfo>();
		con = new HttpConnection();
		gson = new Gson();
		normalizedSrcConceptName = normalizeConceptName(srcConcept.getConceptName());
		// The uri should be like "/c/en/publisher/n/a_firm_in_the_publishing_business"
		conceptInUriPattern = Pattern.compile("^/c/en/([^/]+)");
	}
	
	public Set<RecommendedConceptInfo> getRecommendedConcepts() {
		return recommendedConcepts;
	}

	public void searchAll() throws Exception {
		searchIsAConcepts();
//		searchRelatedToConcepts();
		searchSuperclassOfConcepts();
		searchPartOfConcepts();
//		searchSynonymConcepts();
//		searchAntonymConcepts();
	}

	public void searchIsAConcepts() throws Exception {
		String url = ROOT_URL + "?rel=/r/PartOf&end=/c/en/" +
				normalizedSrcConceptName + "&limit=" + SEARCH_RESULT_LIMIT;
		con.sendGet(url);
		ConceptNetSearchResult result = gson.fromJson(con.getResponseString(), ConceptNetSearchResult.class);
		for(ConceptNetEdge edge:result.getEdges()) {
			String concept = parseConceptFromStartField(edge.getStart());
			RecommendedConceptInfo info = new RecommendedConceptInfo(srcConcept, new Concept(concept),
					RecommendedConceptInfo.ConceptRelation.SUBCLASS_OF);
			recommendedConcepts.add(info);
		}
	}
	
	public void searchRelatedToConcepts() throws Exception {
		String url = ROOT_URL + "?rel=/r/RelatedTo&end=/c/en/" +
				normalizedSrcConceptName + "&limit=" + SEARCH_RESULT_LIMIT;
		con.sendGet(url);
		ConceptNetSearchResult result = gson.fromJson(con.getResponseString(), ConceptNetSearchResult.class);
		for(ConceptNetEdge edge:result.getEdges()) {
			String concept = parseConceptFromStartField(edge.getStart());
			RecommendedConceptInfo info = new RecommendedConceptInfo(srcConcept, new Concept(concept),
					RecommendedConceptInfo.ConceptRelation.RELATED_TO);
			recommendedConcepts.add(info);
		}
	}
	
	public void searchSuperclassOfConcepts() throws Exception {
		String url = ROOT_URL + "?rel=/r/IsA&start=/c/en/" +
				normalizedSrcConceptName + "&limit=" + SEARCH_RESULT_LIMIT;
		con.sendGet(url);
		ConceptNetSearchResult result = gson.fromJson(con.getResponseString(), ConceptNetSearchResult.class);
		for(ConceptNetEdge edge:result.getEdges()) {
			//Here I should use end field instead of start field
			String concept = parseConceptFromStartField(edge.getEnd());
			RecommendedConceptInfo info = new RecommendedConceptInfo(srcConcept, new Concept(concept),
					RecommendedConceptInfo.ConceptRelation.SUPERCLASS_OF);
			recommendedConcepts.add(info);
		}
	}
	
	public void searchPartOfConcepts() throws Exception {
		String url = ROOT_URL + "?rel=/r/PartOf&end=/c/en/" +
				normalizedSrcConceptName + "&limit=" + SEARCH_RESULT_LIMIT;
		con.sendGet(url);
		ConceptNetSearchResult result = gson.fromJson(con.getResponseString(), ConceptNetSearchResult.class);
		for(ConceptNetEdge edge:result.getEdges()) {
			String concept = parseConceptFromStartField(edge.getStart());
			RecommendedConceptInfo info = new RecommendedConceptInfo(srcConcept, new Concept(concept),
					RecommendedConceptInfo.ConceptRelation.PART_OF);
			recommendedConcepts.add(info);
		}
	}
	
	public void searchSynonymConcepts() throws Exception {
		String url = ROOT_URL + "?rel=/r/Synonym&end=/c/en/" +
				normalizedSrcConceptName + "&limit=" + SEARCH_RESULT_LIMIT;
		con.sendGet(url);
		ConceptNetSearchResult result = gson.fromJson(con.getResponseString(), ConceptNetSearchResult.class);
		for(ConceptNetEdge edge:result.getEdges()) {
			String concept = parseConceptFromStartField(edge.getStart());
			RecommendedConceptInfo info = new RecommendedConceptInfo(srcConcept, new Concept(concept),
					RecommendedConceptInfo.ConceptRelation.SYNONYM);
			recommendedConcepts.add(info);
		}
	}
	
//	public void searchAntonymConcepts() throws Exception {
//		String url = ROOT_URL + "?rel=/r/Antonym&end=/c/en/" +
//				normalizedSrcConceptName + "&limit=" + SEARCH_RESULT_LIMIT;
//		con.sendGet(url);
//		ConceptNetSearchResult result = gson.fromJson(con.getResponseString(), ConceptNetSearchResult.class);
//		for(ConceptNetEdge edge:result.getEdges()) {
//			String concept = parseConceptFromStartField(edge.getStart());
//			RecommendedConceptInfo info = new RecommendedConceptInfo(srcConcept, new Concept(concept),
//					RecommendedConceptInfo.ConceptRelation.ANTONYM);
//			recommendedConcepts.add(info);
//		}
//	}
	
	/**
	 * For most usage, it will receive a start field, for the parse of a SUPERCLASS_OF result,
	 * it should receive an end field.
	 * 
	 * @param startField
	 * @return
	 */
	private String parseConceptFromStartField(String startField) {
		String concept = "";
		Matcher matcher = conceptInUriPattern.matcher(startField);
		if(matcher.find()) {
			concept = matcher.group(1);
		}
		return concept;
	}
	
	private String normalizeConceptName(String conceptName) {
		//TODO: need some better normalization mechanism
		String normalizedConceptName = conceptName.toLowerCase();
		return normalizedConceptName;
	}
	
}