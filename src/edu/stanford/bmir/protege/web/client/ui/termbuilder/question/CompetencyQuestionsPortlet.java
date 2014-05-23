package edu.stanford.bmir.protege.web.client.ui.termbuilder.question;

import java.util.Collection;
import java.util.Collections;

import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

@SuppressWarnings("unchecked")
public class CompetencyQuestionsPortlet extends AbstractOWLEntityPortlet {

	public static final int INITIAL_HEIGHT = 570;
	private CompetencyQuestionsViewPresenter presenter;
	
	public CompetencyQuestionsPortlet(Project project) {
		super(project);
	}

	@Override
	public Collection<EntityData> getSelection() {
		return Collections.emptyList();
	}

	@Override
	public void reload() {
		presenter.reload();
	}

	@Override
	public void initialize() {
		setLayout(new FitLayout());
		setHeight(INITIAL_HEIGHT);
		presenter = new CompetencyQuestionsViewPresenter(getProjectId(), 
				new CompetencyQuestionsViewImpl(getProject()));
		presenter.reload();
		add(presenter.getWidget());
		setTitle("Competency Questions");
	}
}