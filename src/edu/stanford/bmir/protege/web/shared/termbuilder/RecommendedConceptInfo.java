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
        SUBCLASS_OF,
        SUPERCLASS_OF,
        SIBLING_OF,
		RELATED_TO,
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
        } else if (relation == ConceptRelation.SIBLING_OF) {
            return new String[] {"Sibling", "Of"};
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
        } else if (relation == ConceptRelation.SIBLING_OF) {
            return "#CD4939";
        } else {
            return "#D96F00";
        }
    }

	@Override
	public int compareTo(RecommendedConceptInfo info) {
        if(!this.relation.equals(info.relation)) {
            return this.relation.compareTo(info.relation);
        } else if(!this.recommendedConcept.equals(info.recommendedConcept)) {
            return this.recommendedConcept.compareTo(info.recommendedConcept);
        } else {
            return this.recommendationID.compareTo(info.recommendationID);
        }
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