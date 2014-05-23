package edu.stanford.bmir.protege.web.client.dispatch.actions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;

public class RecommendConceptsAction extends AbstractHasProjectAction<RecommendConceptsResult> {
	
	private HashSet<Concept> conceptSet;
	
	public RecommendConceptsAction(HashSet<Concept> conceptSet) {
		this.conceptSet = conceptSet;
	}
	
	public RecommendConceptsAction(ProjectId projectId, HashSet<Concept> conceptSet) {
		super(projectId);
		this.conceptSet = conceptSet;
	}
	
	protected RecommendConceptsAction() {
		
	}
	
	public Set<Concept> getConcepts() {
		return Collections.unmodifiableSet(conceptSet);
	}
}