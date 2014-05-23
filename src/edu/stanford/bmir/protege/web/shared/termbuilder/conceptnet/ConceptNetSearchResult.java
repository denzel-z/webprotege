package edu.stanford.bmir.protege.web.shared.termbuilder.conceptnet;

public class ConceptNetSearchResult {
	
	public int numFound;
	public ConceptNetEdge[] edges;
	
	public int getNumFound() {
		return numFound;
	}
	
	public ConceptNetEdge[] getEdges() {
		return edges;
	}
	
}