package edu.stanford.bmir.protege.web.shared.termbuilder;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class CompetencyQuestionInfo implements Serializable, IsSerializable, Comparable<CompetencyQuestionInfo> {
	
	private int questionID;
	private String question;
	
	private CompetencyQuestionInfo() {
		
	}
	
	public CompetencyQuestionInfo(int questionID, String question) {
		super();
		this.questionID = questionID;
		this.question = question;
	}
	
	public CompetencyQuestionInfo(CompetencyQuestionInfo info) {
		super();
		this.questionID = info.getId();
		this.question = info.getQuestion();
	}

	public int getId() {
		return questionID;
	}

	public void setId(int questionID) {
		this.questionID = questionID;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Override
	public int compareTo(CompetencyQuestionInfo info) {
		return new Integer(questionID).compareTo(new Integer(info.questionID));
	}
	
	@Override
	public boolean equals(Object o) {
		CompetencyQuestionInfo info = (CompetencyQuestionInfo) o;
		if(this.questionID == info.questionID && this.question.equals(info.question)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return this.questionID + "::" + question;
	}
	
}