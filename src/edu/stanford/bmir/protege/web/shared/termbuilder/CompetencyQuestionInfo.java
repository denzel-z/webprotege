package edu.stanford.bmir.protege.web.shared.termbuilder;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CompetencyQuestionInfo implements Serializable, IsSerializable, Comparable<CompetencyQuestionInfo> {
	
	private int questionID;
	private String question;
	
	public CompetencyQuestionInfo(int questionID, String question) {
		super();
		this.questionID = questionID;
		this.question = question;
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
		return this.question.compareTo(info.question);
	}
	
}