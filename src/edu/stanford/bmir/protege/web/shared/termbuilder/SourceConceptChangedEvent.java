package edu.stanford.bmir.protege.web.shared.termbuilder;

import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * @author Yuhao Zhang
 */
public class SourceConceptChangedEvent extends ProjectEvent<SourceConceptChangedHandler> {

    public static final transient Type<SourceConceptChangedHandler> TYPE = new Type<SourceConceptChangedHandler>();

    /**
     * For serialization purpose.
     */
    private SourceConceptChangedEvent() {

    }

    public SourceConceptChangedEvent(ProjectId source) {
        super(source);
    }

    @Override
    public Type<SourceConceptChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SourceConceptChangedHandler handler) {
        handler.handleSourceConceptChanged(this);
    }
}
