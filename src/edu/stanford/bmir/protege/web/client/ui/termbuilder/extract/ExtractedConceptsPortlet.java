package edu.stanford.bmir.protege.web.client.ui.termbuilder.extract;

import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.question.CompetencyQuestionsPortlet2;

import java.util.Collection;
import java.util.Collections;

/**
 * The portlet that shows concepts extracted from {@link CompetencyQuestionsPortlet2} and
 * allows user to select concepts from them. The concepts will be visualized in a tag
 * cloud.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
@SuppressWarnings("unchecked")
public class ExtractedConceptsPortlet extends AbstractOWLEntityPortlet {
	
	public static final int INITIAL_HEIGHT = 280;
	private ExtractedConceptsViewPresenter presenter;

	public ExtractedConceptsPortlet(Project project) {
		super(project);
	}
	
	public ExtractedConceptsPortlet(Project project, boolean initialize) {
		super(project, initialize);
	}

	@Override
	public Collection<EntityData> getSelection() {
		return Collections.emptyList();
	}
	
//	@Override
//	public void afterRendered() {
//		
//	}

	@Override
	public void reload() {
        // Don't reload the presenter otherwise the canvas will be reinitialized.
		// presenter.reload();
	}

	@Override
	public void initialize() {
		System.err.println("ExtractedConceptsPortlet initialize() method callded!");
		setLayout(new FitLayout());
		setHeight(INITIAL_HEIGHT);
		presenter = new ExtractedConceptsViewPresenter(getProjectId(),
				new ExtractedConceptsViewImpl(getProject()));
		add(presenter.getWidget());
        // Don't reload the presenter when initialize.
		// presenter.reload();
		setTitle("Extracted Concepts");
	}
	
}