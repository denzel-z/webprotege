package edu.stanford.bmir.protege.web.shared.termbuilder;

import org.semanticweb.owlapi.model.IRI;

import java.io.Serializable;


/**
 * This is the class that represent a "concept" extracted from a competency question.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class Concept implements Serializable, Comparable<Concept>{
	
	private String conceptName;
    private IRI conceptIRI;
	
	private Concept() {
		
	}

	public Concept(String conceptName) {
		super();
		this.conceptName = conceptName;
        conceptIRI = null;
	}

    public Concept(String conceptName, IRI conceptIRI) {
        super();
        this.conceptName = conceptName;
        this.conceptIRI = conceptIRI;
    }

	public String getConceptName() {
		return conceptName;
	}
    public IRI getIRI() { return conceptIRI; }

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	
	@Override
	public int compareTo(Concept concept) {
		return this.conceptName.compareTo(concept.conceptName);
	}
	
	@Override
	public boolean equals(Object o) {
		return conceptName.equals(((Concept)o).conceptName);
	}
	
	/**
	 * For duplicate removal in HashSet.
	 */
	@Override
	public int hashCode() {
		return conceptName.hashCode();
	}
}