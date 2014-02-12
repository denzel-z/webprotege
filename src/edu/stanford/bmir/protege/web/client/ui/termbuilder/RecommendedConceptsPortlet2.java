package edu.stanford.bmir.protege.web.client.ui.termbuilder;

import java.util.Collection;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxColumnConfig;
import com.gwtext.client.widgets.grid.CheckboxSelectionModel;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

/**
 * The portlet that shows recommended concepts generated according to the concepts in 
 * {@linkplain edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet}.
 * User can add selected concepts into the class tree view in the Term Builder.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
@SuppressWarnings("unchecked")
public class RecommendedConceptsPortlet2 extends AbstractOWLEntityPortlet {
	
	protected ToolbarButton refreshButton;
	protected ToolbarButton acceptButton;
	
	protected GridPanel recommendationPanel;

	public RecommendedConceptsPortlet2(Project project) {
		super(project);
	}
	
	public RecommendedConceptsPortlet2(Project project, boolean initialize) {
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
		setTitle("Recommended Concepts");
		addToolbarButtons();
		add(creatRecommendationPanel());
	}

	protected void addToolbarButtons() {
		setTopToolbar(new Toolbar());
		final Toolbar toolbar = getTopToolbar();
		
		refreshButton = createRefreshButton();
		if(refreshButton != null) {
			toolbar.addButton(refreshButton);
		}
		
		acceptButton = createAcceptButton();
		if(acceptButton != null) {
			toolbar.addSpacer();
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

	private ToolbarButton createRefreshButton() {
		ToolbarButton refreshButton = new ToolbarButton("Refresh Recommendations");
		refreshButton.setCls("toolbar-button");
		refreshButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(final Button button, final EventObject e) {
				//TODO: add click event listener
			}
		});
		return refreshButton;
	}
	
	private Panel creatRecommendationPanel() {
		GridPanel recommendationPanel = new GridPanel();
		final CheckboxSelectionModel cbSelectionModel = new CheckboxSelectionModel();
		
		RecordDef recordDef = new RecordDef(  
                new FieldDef[]{  
                        new StringFieldDef("candidate_concept"),
                        new StringFieldDef("relation")
                }
        ); 
		
		Object[][] data = getRecData();
		MemoryProxy proxy = new MemoryProxy(data);
		ArrayReader reader = new ArrayReader(recordDef);
		Store store = new Store(proxy, reader);
		store.load();
		
		BaseColumnConfig[] columns = new BaseColumnConfig[]{
				new CheckboxColumnConfig(cbSelectionModel),
                new RowNumberingColumnConfig(),  
                //column ID is company which is later used in setAutoExpandColumn  
                new ColumnConfig("Candidate Concepts", "candidate_concept", 200, true, null, "candidate_concept"),
                new ColumnConfig("Relation to Existing Concepts", "relation", 800, true, null, "relation")
        };  
		ColumnModel columnModel = new ColumnModel(columns);
		
		recommendationPanel.setStore(store);
		recommendationPanel.setColumnModel(columnModel);
		recommendationPanel.setFrame(true);
		recommendationPanel.setStripeRows(true);
		recommendationPanel.setAutoExpandColumn("relation");
		recommendationPanel.setSelectionModel(cbSelectionModel);
		recommendationPanel.setWidth("auto");
		recommendationPanel.setHeight("auto");
		recommendationPanel.setEnableColumnResize(true);
		
		Panel panel = new Panel();
        panel.setBorder(true);
        
        GridView view = new GridView();
        view.setForceFit(true);
        recommendationPanel.setView(view);
        
        panel.add(recommendationPanel);
        
        return panel;
	}

	private Object[][] getRecData() {
		return new Object[][] {
				new Object[] {"Vegetable Topping", "Child of [Pizza Topping]"},
				new Object[] {"Beef Topping", "Child of [Pizza Topping]"},
				new Object[] {"American Pizza", "Child of [Pizza]"},
				new Object[] {"Italian Pizza", "Child of [Pizza]"},
				new Object[] {"Food", "Parent of [Pizza]"}
		};
	}
}