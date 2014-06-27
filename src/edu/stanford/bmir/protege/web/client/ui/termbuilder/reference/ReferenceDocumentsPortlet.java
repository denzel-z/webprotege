package edu.stanford.bmir.protege.web.client.ui.termbuilder.reference;

import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Yuhao Zhang
 */
public class ReferenceDocumentsPortlet extends AbstractOWLEntityPortlet {

    public static final int INITIAL_HEIGHT = 280;
    private ReferenceDocumentsViewPresenter presenter;

    public ReferenceDocumentsPortlet(Project project) {super(project);}

    @Override
    public void reload() {
        presenter.reload();
    }

    @Override
    public void initialize() {
        setLayout(new FitLayout());
        setHeight(INITIAL_HEIGHT);
        presenter = new ReferenceDocumentsViewPresenter(getProjectId(), new ReferenceDocumentsViewImpl(getProject(), this));
        presenter.reload();
        add(presenter.getWidget());
        setTitle("Online Reference Docs");
    }

    @Override
    public Collection<EntityData> getSelection() {
        return Collections.emptyList();
    }
}
