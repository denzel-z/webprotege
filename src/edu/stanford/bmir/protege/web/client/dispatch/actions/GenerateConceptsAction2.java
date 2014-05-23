package edu.stanford.bmir.protege.web.client.dispatch.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.CompetencyQuestionsManager;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.termbuilder.CompetencyQuestionInfo;

/**
 * The Generate Action on the Competency Question Portlet. For now it just
 * implements the Action interface. Will extend a abstract action class
 * later.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class GenerateConceptsAction2 implements Action<GenerateConceptsResult>, Serializable {
	
	private List<CompetencyQuestionInfo> questions;
	
	public GenerateConceptsAction2(List<CompetencyQuestionInfo> qList) {
		questions = new ArrayList<CompetencyQuestionInfo>();
		for(CompetencyQuestionInfo q: qList) {
			questions.add(new CompetencyQuestionInfo(q));
		}
		//try shallow copy first
//		questions = new ArrayList<CompetencyQuestionInfo>(qList);
		System.err.println("GenerateConceptAction constructor being called!");
	}
	
	public GenerateConceptsAction2(CompetencyQuestionsManager manager) {
		questions = new ArrayList<CompetencyQuestionInfo>(manager.getQuestions());
	}
	
	//For serialization purposes only.
	protected GenerateConceptsAction2() {
		
	}
	
	public List<CompetencyQuestionInfo> getQuestions() {
		return Collections.unmodifiableList(questions);
	}
	
}