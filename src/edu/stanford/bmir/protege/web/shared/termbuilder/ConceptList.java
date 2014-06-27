package edu.stanford.bmir.protege.web.shared.termbuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This is the class that represent a "conceptName" extracted from a competency question.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class ConceptList implements Serializable, IsSerializable{

	List<Concept> conceptList;
	
	public ConceptList() {
		super();
		conceptList = new ArrayList<Concept>();
	}
	
	public ConceptList(ConceptList list) {
		super();
		conceptList = new ArrayList<Concept>();
		for(Concept c: list.conceptList) {
			conceptList.add(new Concept(c.getConceptName()));
		}
	}
	
	public ConceptList(String s) {
		super();
		conceptList = new ArrayList<Concept>();
		conceptList.add(new Concept(s));
	}
	
	public ConceptList(ConceptSet set) {
		conceptList.addAll(set.getSet());
	}
	
	public ConceptList(List<String> sList) {
		//TODO: add it
	}
	
	public void addConcept(Concept concept) {
		conceptList.add(new Concept(concept.getConceptName()));
	}
	
	public void addConcept(String s) {
		conceptList.add(new Concept(s));
	}
	
	public List<Concept> getList() {
		return conceptList;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(Concept c:conceptList) {
			sb.append(c.getConceptName());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
	
}