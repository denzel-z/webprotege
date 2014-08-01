package edu.stanford.bmir.protege.web.client.ui.termbuilder.extract;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.question.CompetencyQuestionsPortlet2;

import java.util.Collection;

/**
 * The portlet that shows concepts extracted from {@link CompetencyQuestionsPortlet2} and
 * allows user to select concepts from them. The concepts will be visualized in a tag
 * cloud.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
@SuppressWarnings("unchecked")
public class ExtractedConceptsPortlet2 extends AbstractOWLEntityPortlet {
	
	protected ToolbarButton selectAllButton;
	protected ToolbarButton unselectAllButton;
	protected ToolbarButton acceptButton;

	public ExtractedConceptsPortlet2(Project project) {
		super(project);
	}
	
	public ExtractedConceptsPortlet2(Project project, boolean initialize) {
		super(project, initialize);
	}

	@Override
	public Collection<EntityData> getSelection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize() {
		setLayout(new FitLayout());
		setTitle("Extracted Concepts");
		addToolbarButtons();
	}

	protected void addToolbarButtons() {
		setTopToolbar(new Toolbar());
		final Toolbar toolbar = getTopToolbar();
		
		selectAllButton = createSelectAllButton();
		if(selectAllButton != null) {
			toolbar.addButton(selectAllButton);
		}
		
		unselectAllButton = createUnselectAllButton();
		if(unselectAllButton != null) {
			toolbar.addButton(unselectAllButton);
		}
		
		acceptButton = createAcceptButton();
		if(acceptButton != null) {
			toolbar.addSpacer();
//			toolbar.addSeparator();
			toolbar.addButton(acceptButton);
		}
		
		toolbar.setAutoWidth(true);
		
	}

	private ToolbarButton createAcceptButton() {
		ToolbarButton acceptButton = new ToolbarButton("Add Selected Into Class Tree");
		acceptButton.setCls("toolbar-button");
		acceptButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(final Button button, final EventObject e) {
				//TODO: add click event listener
			}
		});
		return acceptButton;
	}

	private ToolbarButton createUnselectAllButton() {
		ToolbarButton unselectAllButton = new ToolbarButton("Unselect All");
		unselectAllButton.setCls("toolbar-button");
		unselectAllButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(final Button button, final EventObject e) {
				//TODO: add click event listener
			}
		});
		return unselectAllButton;
	}

	private ToolbarButton createSelectAllButton() {
		ToolbarButton selectAllButton = new ToolbarButton("Select All");
		selectAllButton.setCls("toolbar-button");
		selectAllButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(final Button button, final EventObject e) {
				//TODO: add click event listener
			}
		});
		return selectAllButton;
	}
	
}