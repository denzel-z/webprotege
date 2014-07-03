package edu.stanford.bmir.protege.web.client.ui.termbuilder.reference;

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
 * @author Yuhao Zhang
 */
public class ReferenceDocumentsPortlet extends AbstractOWLEntityPortlet {

    public static final int INITIAL_HEIGHT = 280;
    private ReferenceDocumentsViewPresenter presenter;

    private final String DEFAULT_TITLE = "Online Reference Docs";
    private final String DEFAULT_TITLE_PREFFIX = "Online Reference Docs for: ";

    public ReferenceDocumentsPortlet(Project project) {super(project);}

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
        presenter = new ReferenceDocumentsViewPresenter(getProjectId(), new ReferenceDocumentsViewImpl(getProject(), this));
        reload();
        add(presenter.getWidget());
    }

    @Override
    public Collection<EntityData> getSelection() {
        return Collections.emptyList();
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
