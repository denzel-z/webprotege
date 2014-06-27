package edu.stanford.bmir.protege.web.shared.termbuilder;

import java.io.Serializable;

/**
 * This class is used to represent the all the info about a recommended conceptName.
 * The three properties are necessary for the table display.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class RecommendedConceptInfo implements Serializable, Comparable<RecommendedConceptInfo> {

	private String recommendationID;
	private Concept srcConcept;
	private Concept recommendedConcept;
	private ConceptRelation relation;
	
	public static enum ConceptRelation {
		SUPERCLASS_OF,
		SUBCLASS_OF,
		RELATED_TO,
		PART_OF,
		SYNONYM
	}
	
	public RecommendedConceptInfo(Concept srcConcept,
			Concept recommendedConcept, ConceptRelation relation) {
		super();
		this.srcConcept = srcConcept;
		this.recommendedConcept = recommendedConcept;
		this.relation = relation;
		this.recommendationID = srcConcept.getConceptName().hashCode() + "-" +
				recommendedConcept.getConceptName().hashCode() + "-" + relation.hashCode();
	}
	
	//For serialization purpose
	protected RecommendedConceptInfo() {
		
	}
	
	public String getId() {
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

    public String[] getTextRelation() {
        if(relation == ConceptRelation.SUPERCLASS_OF) {
            return new String[] {"SuperClass", "Of"};
        } else if (relation == ConceptRelation.SUBCLASS_OF) {
            return new String[] {"SubClass", "Of"};
        } else if (relation == ConceptRelation.RELATED_TO) {
            return new String[] {"Related", "To"};
        } else if (relation == ConceptRelation.PART_OF) {
            return new String[] {"Part", "Of"};
        } else {
            return new String[] {"Synonym", "Of"};
        }
    }
	
	public String getConceptRelationDescription() {
        String[] text = getTextRelation();
        return text[0] + " " + text[1] + " [" + srcConcept.getConceptName() + "]";
	}

    public String getHTMLConceptRelationDescription() {
        String[] text = getTextRelation();
        StringBuffer sb = new StringBuffer();
        sb.append("<span style=\"color:");
        sb.append(getRelationColor());
        sb.append(";\">");
        sb.append(text[0]);
        sb.append("</span>");
        sb.append(" " + text[1] + " ");
        sb.append("<span style=\"color:grey;\">");
        sb.append(srcConcept.getConceptName());
        sb.append("</span>");
        return sb.toString();
    }

    public String getHTMLRecommendedConceptDescription() {
        return "<span style=\"color:#175AA9;\">" + recommendedConcept.getConceptName() + "</span>";
    }

    public String getRelationColor() {
        if(relation == ConceptRelation.SUPERCLASS_OF) {
            return "#337FB8";
        } else if (relation == ConceptRelation.SUBCLASS_OF) {
            return "#6CB42A";
        } else if (relation == ConceptRelation.RELATED_TO) {
            return "#87579C";
        } else if (relation == ConceptRelation.PART_OF) {
            return "#CD4939";
        } else {
            return "#D96F00";
        }
    }

	/**
	 * The comparison is based on the srcConcept, the recommendedConcept.
	 * TODO: A better comparison need to be implemented.
	 */
	@Override
	public int compareTo(RecommendedConceptInfo info) {
//		int srcResult = this.srcConcept.compareTo(info.srcConcept);
//		if(srcResult != 0) {
//			return srcResult;
//		} else {
//			int recResult = this.recommendedConcept.compareTo(this.recommendedConcept);
//			return recResult;
//		}
		int result = this.recommendationID.compareTo(info.recommendationID);
		return result;
	}
	
	@Override
	public String toString() {
		return recommendedConcept.getConceptName() + "::" + getConceptRelationDescription();
	}
	
	@Override
	public boolean equals(Object info) {
		return this.recommendationID.equals(((RecommendedConceptInfo)info).recommendationID);
	}
	
	
}