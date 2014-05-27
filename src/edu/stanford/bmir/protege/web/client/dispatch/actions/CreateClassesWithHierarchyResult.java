package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.RenderableResult;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class CreateClassesWithHierarchyResult extends RenderableResult {
    private Set<ObjectPath<OWLClass>> superClassPathToRootSet;
    private Set<OWLClass> createdClasses = new HashSet<OWLClass>();

    public CreateClassesWithHierarchyResult(BrowserTextMap browserTextMap, Set<ObjectPath<OWLClass>> superClassPathToRootSet, Set<OWLClass> createdClasses) {
        super(browserTextMap);
        this.superClassPathToRootSet = superClassPathToRootSet;
        this.createdClasses = createdClasses;
    }

    public CreateClassesWithHierarchyResult() {
    }
}
