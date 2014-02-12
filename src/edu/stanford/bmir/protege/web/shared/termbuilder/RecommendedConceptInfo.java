package edu.stanford.bmir.protege.web.shared.termbuilder;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class is used to represent the all the info about a recommended concept. 
 * The three properties are necessary for the table display.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class RecommendedConceptInfo implements Serializable, IsSerializable, Comparable<RecommendedConceptInfo> {

	private int recommendationID;
	private Concept srcConcept;
	private Concept recommendedConcept;
	private ConceptRelation relation;
	
	private static enum ConceptRelation {
		IS_CHILD_OF,
		IS_PARENT_OF,
		IS_SIBLING_OF
	}
	
	public RecommendedConceptInfo(Concept srcConcept,
			Concept recommendedConcept, ConceptRelation relation) {
		super();
		this.srcConcept = srcConcept;
		this.recommendedConcept = recommendedConcept;
		this.relation = relation;
	}
	
	public int getId() {
		return this.recommendationID;
	}
	
	public Concept getSrcConcept() {
		return srcConcept;
	}

	public void setSrcConcept(Concept srcConcept) {
		this.srcConcept = srcConcept;
	}

	public Concept getRecommendedConcept() {
		return recommendedConcept;
	}
	
	public void setRecommendedConcept(Concept recommendedConcept) {
		this.recommendedConcept = recommendedConcept;
	}

	public ConceptRelation getRelation() {
		return relation;
	}

	public void setRelation(ConceptRelation relation) {
		this.relation = relation;
	}
	
	public String getConceptRelationDescription() {
		if(relation == ConceptRelation.IS_CHILD_OF) {
			return "Child of " + srcConcept.getConceptName();
		} else if (relation == ConceptRelation.IS_PARENT_OF) {
			return "Parent of " + srcConcept.getConceptName();
		} else {
			return "Sibling of " + srcConcept.getConceptName();
		}
	}

	/**
	 * The comparison is based on the srcConcept, the recommendedConcept.
	 * TODO: A better comparison need to be implemented.
	 */
	@Override
	public int compareTo(RecommendedConceptInfo info) {
		int srcResult = this.srcConcept.compareTo(info.srcConcept);
		if(srcResult != 0) {
			return srcResult;
		} else {
			int recResult = this.recommendedConcept.compareTo(this.recommendedConcept);
			return recResult;
		}
	}
	
	
}