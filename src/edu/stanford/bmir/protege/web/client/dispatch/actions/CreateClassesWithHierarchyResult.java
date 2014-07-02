package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.RenderableResult;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.event.HasEventList;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class CreateClassesWithHierarchyResult extends RenderableResult implements HasEventList<ProjectEvent<?>> {

    private Set<ObjectPath<OWLClass>> superClassPathToRootSet;
    private Set<OWLClass> createdClasses = new HashSet<OWLClass>();
    private EventList<ProjectEvent<?>> eventList;

    public CreateClassesWithHierarchyResult() {
    }

    public CreateClassesWithHierarchyResult(BrowserTextMap browserTextMap, Set<ObjectPath<OWLClass>> superClassPathToRootSet, Set<OWLClass> createdClasses, EventList<ProjectEvent<?>> eventList) {
        super(browserTextMap);
        this.superClassPathToRootSet = superClassPathToRootSet;
        this.createdClasses = createdClasses;
        this.eventList = eventList;
    }

    @Override
    public EventList<ProjectEvent<?>> getEventList() {
        return eventList;
    }
}
