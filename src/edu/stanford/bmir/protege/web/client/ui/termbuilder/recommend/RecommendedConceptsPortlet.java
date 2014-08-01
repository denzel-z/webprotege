package edu.stanford.bmir.protege.web.client.ui.termbuilder.recommend;

import com.google.common.base.Optional;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.TermBuilderConstant;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import java.util.Collection;
import java.util.Collections;

/**
 * The portlet that shows recommended concepts generated according to the concepts in 
 * {@linkplain edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet}.
 * User can add selected concepts into the class tree view in the Term Builder.
 * This is the second implementation which uses GWT native api rather than gwtext.
 * This will be standard in future implementation.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
@SuppressWarnings("unchecked")
public class RecommendedConceptsPortlet extends AbstractOWLEntityPortlet {

	public static final int INITIAL_HEIGHT = 280;
	private RecommendedConceptsListViewPresenter presenter;

    private final String DEFAULT_TITLE = "Recommended Concepts";
    private final String DEFAULT_TITLE_PREFFIX = "Recommended Concepts for: ";
	
	public RecommendedConceptsPortlet(Project project) {
		super(project);
	}

	@Override
	public Collection<EntityData> getSelection() {
		return Collections.emptyList();
	}

	@Override
	public void reload() {
        String className = getSelectedClassName();
        presenter.refresh(className);
        setTitleDynamically(className);
	}

	@Override
	public void initialize() {
		setLayout(new FitLayout());
		setHeight(INITIAL_HEIGHT);
		presenter = new RecommendedConceptsListViewPresenter(getProjectId(), 
				new RecommendedConceptsListViewImpl(getProject(), this));
        reload();
		add(presenter.getWidget());
	}

    private String getSelectedClassName() {
        Optional<OWLEntityData> entity = getSelectedEntityData();
        if(!entity.isPresent()) return null;
        String className = entity.get().getBrowserText();
        return className;
    }

    private void setTitleDynamically(String className) {
        // Dynamically change the title of the portlet
        if(className == null || className.equals(TermBuilderConstant.OWLTHING_NAME)) {
            setTitle(DEFAULT_TITLE);
        } else {
            setTitle(DEFAULT_TITLE_PREFFIX + className);
        }
    }
}