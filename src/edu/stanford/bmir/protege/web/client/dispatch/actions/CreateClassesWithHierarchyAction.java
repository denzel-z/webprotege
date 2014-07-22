package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.termbuilder.ClassStringAndSuperClassPair;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Create classes in the class tree, with specified superclass information in the argument.
 *
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class CreateClassesWithHierarchyAction extends AbstractHasProjectAction<CreateClassesWithHierarchyResult> {
    private List<ClassStringAndSuperClassPair> pairList;

    private Set<RecommendedConceptInfo> infoSet;
    private OWLClass srcConcept;

    private CreateClassesWithHierarchyAction() {
    }

    public CreateClassesWithHierarchyAction(ProjectId projectId, List<ClassStringAndSuperClassPair> pairList) {
        super(projectId);
        this.pairList = new ArrayList<ClassStringAndSuperClassPair>(pairList);
    }

    public CreateClassesWithHierarchyAction(ProjectId projectId, Set<RecommendedConceptInfo> infoSet, OWLClass srcConcept) {
        super(projectId);
        this.infoSet = infoSet;
        this.srcConcept = srcConcept;
    }

    public List<ClassStringAndSuperClassPair> getPairList() {
        return pairList;
    }

    public Set<RecommendedConceptInfo> getRecommendedConceptSet() {
        return infoSet;
    }

    public OWLClass getSrcConcept() {
        return srcConcept;
    }
}
