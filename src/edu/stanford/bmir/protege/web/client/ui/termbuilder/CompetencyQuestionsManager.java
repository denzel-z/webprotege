package edu.stanford.bmir.protege.web.client.ui.termbuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.stanford.bmir.protege.web.shared.termbuilder.CompetencyQuestionInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.ReferenceDocumentInfo;

/**
 * This is the class to store competency questions and their corresponding concepts
 * in the client side. Project object will hold a instance of this
 * CompetencyQuestionsManager, and every portlet can get access to it.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class CompetencyQuestionsManager {
	
	private List<CompetencyQuestionInfo> questions;
	//Used to generate an unique Id for each question.
	private int maxId = 0;
	private Set<Concept> acceptedConceptSet;
	private Set<RecommendedConceptInfo> recommendedConceptSet;
    private List<ReferenceDocumentInfo> referenceDocumentInfoList;
	
	public CompetencyQuestionsManager() {
		questions = new ArrayList<CompetencyQuestionInfo>();

		acceptedConceptSet = new HashSet<Concept>();
		recommendedConceptSet = new HashSet<RecommendedConceptInfo>();
        referenceDocumentInfoList = new ArrayList<ReferenceDocumentInfo>();
		maxId = 0;
	}
	
	private void increaseMaxId() {
		maxId++;
	}
	
	public int assignId() {
		increaseMaxId();
		return maxId;
	}
	
	public List<CompetencyQuestionInfo> getQuestions() {
		return questions;
	}
	
	public void addQuestion(CompetencyQuestionInfo info) {
		if(info!=null) {
			info.setId(assignId());
			questions.add(info);
		}
	}
	
	public void addQuestionFromString(String s) {
		if(s.length()>0) {
			//TODO: For now I only use zero for every questionID.
			//Should figure out later how to assign each question
			//a unique Question ID
			CompetencyQuestionInfo info = new CompetencyQuestionInfo(assignId(), s);
			questions.add(info);
		}
	}
	
	public void addQuestionFromStringSet(Set<String> set) {
		int count = 0;
		for(String s: set) {
			addQuestionFromString(s);
			count++;
		}
		System.err.println(count + " questions added!");
	}
	
	public void removeQuestionsInSet(Set<CompetencyQuestionInfo> set) {
		//Can only use iterator while removing through iterating
		Iterator<CompetencyQuestionInfo> iter = questions.iterator();
		while(iter.hasNext()) {
			if(set.contains(iter.next())) {
				iter.remove();
			}
		}
		//Restart
		if(questions.size() == 0) {
			maxId = 0;
		}
	}
	
	public void clearQuestions() {
		questions.clear();
		maxId = 0;
	}
	
	public void addAcceptedConcepts(Collection<Concept> c) {
		acceptedConceptSet.addAll(c);
	}
	
	public void addAcceptedConceptsFromString(Collection<String> c) {
		for(String s: c) {
			acceptedConceptSet.add(new Concept(s));
		}
	}
	
	public void addRecommendedConcepts(Collection<RecommendedConceptInfo> c) {
		recommendedConceptSet.clear();
		recommendedConceptSet.addAll(c);
	}

    public void addRecommendedDocuments(Collection<ReferenceDocumentInfo> c) {
        referenceDocumentInfoList.clear();
        referenceDocumentInfoList.addAll(c);
    }
	
	/**
	 * Return a readonly accepted conceptName set. If want to change the acceptedConceptSet, please use
	 * other methods like addAcceptedConcepts().
	 * @return
	 */
	public Set<Concept> getAcceptedConcepts() {
		return Collections.unmodifiableSet(acceptedConceptSet);
	}
	
	public List<RecommendedConceptInfo> getRecommendedConcepts() {
        List<RecommendedConceptInfo> result = new ArrayList<RecommendedConceptInfo>();
        result.addAll(recommendedConceptSet);
        Collections.sort(result);
		return Collections.unmodifiableList(result);
	}

    public List<ReferenceDocumentInfo> getRecommendedDocuments() {
        return Collections.unmodifiableList(referenceDocumentInfoList);
    }
	
}