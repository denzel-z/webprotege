package edu.stanford.bmir.protege.web.shared.termbuilder;

import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public class ExtractedConceptsChangedEvent extends ProjectEvent<ExtractedConceptsChangedHandler> {

	public static final transient Type<ExtractedConceptsChangedHandler> TYPE = new Type<ExtractedConceptsChangedHandler>();
	
	/**
	 * For serialization purpose.
	 */
	private ExtractedConceptsChangedEvent() {
		
	}
	
	public ExtractedConceptsChangedEvent(ProjectId source) {
		super(source);
	}
	
	@Override
	public Type<ExtractedConceptsChangedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ExtractedConceptsChangedHandler handler) {
		handler.handleExtractedConceptsChanged(this);
	}
	
}