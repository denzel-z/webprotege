package edu.stanford.bmir.protege.web.shared.termbuilder;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This is the class that represent a "concept" extracted from a competency question.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class Concept implements Serializable, IsSerializable, Comparable<Concept>{
	
	private String conceptName;

	public Concept(String conceptName) {
		super();
		this.conceptName = conceptName;
	}

	public String getConceptName() {
		return conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	
	@Override
	public int compareTo(Concept concept) {
		return this.conceptName.compareTo(concept.conceptName);
	}
}