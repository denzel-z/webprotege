package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesWithHierarchyAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesWithHierarchyResult;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.termbuilder.ClassStringAndSuperClassPair;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.CreateClassesChangeGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.msg.OWLMessageFormatter;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import edu.stanford.bmir.protege.web.shared.events.EventTag;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The handler for CreateClassesWithHierarchyAction.
 *
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class CreateClassesWithHierarchyActionHandler extends AbstractHasProjectActionHandler<CreateClassesWithHierarchyAction,CreateClassesWithHierarchyResult> {

    OWLClass srcConcept = null;
    OWLClass supConcept = null;
    OWLClass grandSupConcept = null;
    OWLAPIProject project = null;

    @Override
    public Class<CreateClassesWithHierarchyAction> getActionClass() {
        return CreateClassesWithHierarchyAction.class;
    }

    @Override
    protected RequestValidator<CreateClassesWithHierarchyAction> getAdditionalRequestValidator(CreateClassesWithHierarchyAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    /**
     * Get a single super concept (not all in the ontology)
     * @return superCls
     */
    private OWLClass getSupConcept() {
        if(supConcept == null) {
            OWLOntology ont = project.getRootOntology();
            for (OWLClassExpression sup : srcConcept.getSuperClasses(ont.getImportsClosure())) {
                if (!sup.isAnonymous()) {
                    supConcept = sup.asOWLClass();
                    break;
                }
            }
            if (supConcept == null) {
                supConcept = DataFactory.getOWLThing();
            }
        }
        return supConcept;
    }

    private OWLClass getGrandSupConcept() {
        if(grandSupConcept == null) {
            OWLOntology ont = project.getRootOntology();
            OWLClass superConcept = getSupConcept();
            for(OWLClassExpression sup : superConcept.getSuperClasses(ont.getImportsClosure())) {
                if(!sup.isAnonymous()) {
                    grandSupConcept = sup.asOWLClass();
                    break;
                }
            }
            if(grandSupConcept == null) {
                grandSupConcept = DataFactory.getOWLThing();
            }
        }
        return grandSupConcept;
    }

    private void clearConcepts() {
        srcConcept = null;
        supConcept = null;
        grandSupConcept = null;
    }

    @Override
    protected CreateClassesWithHierarchyResult execute(CreateClassesWithHierarchyAction action, OWLAPIProject project, ExecutionContext executionContext) {
        clearConcepts();
        EventTag tag = project.getEventManager().getCurrentTag();

        srcConcept = action.getSrcConcept();
        this.project = project;

        Set<OWLClass> superClasses = new HashSet<OWLClass>();
        Set<OWLClass> createdClasses = new HashSet<OWLClass>();
        Set<ObjectPath<OWLClass>> pathToRootSet = new HashSet<ObjectPath<OWLClass>>();

        for(RecommendedConceptInfo info : action.getRecommendedConceptSet()) {
            OWLClass superCls = null;
            if(info.getRelation() == RecommendedConceptInfo.ConceptRelation.SUBCLASS_OF) {
                superCls = srcConcept;
            } else if(info.getRelation() == RecommendedConceptInfo.ConceptRelation.SYNONYM ||
                    info.getRelation() == RecommendedConceptInfo.ConceptRelation.SIBLING_OF ||
                    info.getRelation() == RecommendedConceptInfo.ConceptRelation.RELATED_TO) {
                // Use lazy initialization
                superCls = getSupConcept();
            } else if(info.getRelation() == RecommendedConceptInfo.ConceptRelation.SUPERCLASS_OF) {
                superCls = getGrandSupConcept();
            }

            Concept recConcept = info.getRecommendedConcept();
            Set<List<OWLClass>> paths = project.getClassHierarchyProvider().getPathsToRoot(superCls);
            if(paths.isEmpty()) {
                throw new IllegalStateException("Class does not exist in hierarchy: " + project.getRenderingManager().getBrowserText(superCls));
            }
            ObjectPath<OWLClass> pathToRoot = new ObjectPath<OWLClass>(paths.iterator().next());
            Set<String> tempSet = new HashSet<String>();
            tempSet.add(recConcept.getConceptName());
            final CreateClassesChangeGenerator gen = new CreateClassesChangeGenerator(tempSet, Optional.of(superCls));
            ChangeApplicationResult<Set<OWLClass>> result = project.applyChanges(executionContext.getUserId(), gen, createChangeText(project, recConcept.getConceptName(), superCls));
            createdClasses.addAll(result.getSubject().get());
            superClasses.add(superCls);
            pathToRootSet.add(pathToRoot);
        }

        BrowserTextMap browserTextMap = BrowserTextMap.build(project.getRenderingManager(), superClasses, createdClasses);

        EventList<ProjectEvent<?>> eventList = project.getEventManager().getEventsFromTag(tag);

        return new CreateClassesWithHierarchyResult(browserTextMap, pathToRootSet, createdClasses, eventList);
    }

    private ChangeDescriptionGenerator<Set<OWLClass>> createChangeText(OWLAPIProject project, String className, OWLClass superClass) {
        return new FixedMessageChangeDescriptionGenerator<Set<OWLClass>>(OWLMessageFormatter.formatMessage("Created {0} as subclasses of {1}", project, className, superClass));
    }
}
