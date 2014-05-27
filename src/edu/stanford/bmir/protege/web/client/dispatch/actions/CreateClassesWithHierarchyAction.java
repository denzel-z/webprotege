package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.recommend.ClassStringAndSuperClassPair;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Create classes in the class tree, with specified superclass information in the argument.
 *
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
public class CreateClassesWithHierarchyAction extends AbstractHasProjectAction<CreateClassesWithHierarchyResult> {
    private List<ClassStringAndSuperClassPair> pairList;

    private CreateClassesWithHierarchyAction() {
    }

    public CreateClassesWithHierarchyAction(ProjectId projectId, List<ClassStringAndSuperClassPair> pairList) {
        super(projectId);
        this.pairList = new ArrayList<ClassStringAndSuperClassPair>(pairList);
    }

    public List<ClassStringAndSuperClassPair> getPairList() {
        return pairList;
    }
}
