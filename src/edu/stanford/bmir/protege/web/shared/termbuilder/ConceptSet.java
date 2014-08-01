package edu.stanford.bmir.protege.web.shared.termbuilder;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This is the class that represent a "conceptName" extracted from a competency question.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class ConceptSet implements Serializable, IsSerializable{

	Set<Concept> conceptSet;
	
	public ConceptSet() {
		super();
		conceptSet = new HashSet<Concept>();
	}
	
	public ConceptSet(ConceptSet set) {
		super();
		conceptSet = new HashSet<Concept>();
		for(Concept c: set.conceptSet) {
			conceptSet.add(new Concept(c.getConceptName()));
		}
	}
	
	public ConceptSet(String s) {
		super();
		conceptSet = new HashSet<Concept>();
		conceptSet.add(new Concept(s));
	}
	
	public ConceptSet(Set<String> sSet) {
		//TODO: add it
	}
	
	public void addConcept(Concept concept) {
		conceptSet.add(new Concept(concept.getConceptName()));
	}
	
	public void addConcept(String s) {
		conceptSet.add(new Concept(s));
	}
	
	public Set<Concept> getSet() {
		return conceptSet;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Concept c:conceptSet) {
			sb.append(c.getConceptName());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
	
}