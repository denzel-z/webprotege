package edu.stanford.bmir.protege.web.client.ui.termbuilder.question;

import java.util.Collection;

import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.grid.BaseColumnConfig;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.RowNumberingColumnConfig;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;

import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;

/**
 * The portlet that allows user to add and edit competency questions and use these
 * questiosn to generate new concepts.
 * 
 * @author Yuhao Zhang <zyh@stanford.edu>
 */
@SuppressWarnings("unchecked")
public class CompetencyQuestionsPortlet2 extends AbstractOWLEntityPortlet {
	
	protected ToolbarButton createButton;
	protected ToolbarButton deleteButton;
	protected ToolbarButton clearButton;
	protected ToolbarButton generateConceptsButton;
	
	protected GridPanel questionsPanel;

	public CompetencyQuestionsPortlet2(Project project) {
		super(project);
	}
	
	public CompetencyQuestionsPortlet2(Project project, boolean initialize) {
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
		setTitle("Competency Questions");
		addToolbarButtons();
		add(createQuestionsPanel());
	}
	
	/**
	 * Add the top toolbar buttons into the portlet.
	 */
	protected void addToolbarButtons() {
		setTopToolbar(new Toolbar());
		final Toolbar toolbar = getTopToolbar();
		
		createButton = createCreateButton();
		if(createButton != null) {
			toolbar.addButton(createButton);
		}
		
		deleteButton = createDeleteButton();
		if(deleteButton != null) {
			toolbar.addButton(deleteButton);
		}
		
		clearButton = createClearButton();
		if(clearButton != null) {
			toolbar.addButton(clearButton);
		}
		
		generateConceptsButton = createGenerateConceptsButton();
		if(generateConceptsButton != null) {
			toolbar.addSpacer();
            toolbar.addSeparator();
			toolbar.addButton(generateConceptsButton);
		}
	}
	
	protected ToolbarButton createCreateButton() {
		ToolbarButton createButton = new ToolbarButton("Create");
		createButton.setCls("toolbar-button");
		createButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(final Button button, final EventObject e) {
				onCreateQuestions();
			}
		});
		return createButton;
	}
	
	protected ToolbarButton createDeleteButton() {
		ToolbarButton deleteButton = new ToolbarButton("Delete");
		deleteButton.setCls("toolbar-button");
		deleteButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(final Button button, final EventObject e) {
				//TODO: add click event listener
			}
		});
		return deleteButton;
	}
	
	protected ToolbarButton createClearButton() {
		ToolbarButton clearButton = new ToolbarButton("Clear");
		clearButton.setCls("toolbar-button");
		clearButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(final Button button, final EventObject e) {
				//TODO: add click event listener
			}
		});
		return clearButton;
	}
	
	protected ToolbarButton createGenerateConceptsButton() {
		ToolbarButton generateConceptsButton = new ToolbarButton("Generate Concepts");
		generateConceptsButton.setCls("toolbar-button");
		generateConceptsButton.addListener(new ButtonListenerAdapter() {
			@Override
			public void onClick(final Button button, final EventObject e) {
				//TODO: add click event listener
			}
		});
		return generateConceptsButton;
	}
	
	protected void onCreateQuestions() {
		WebProtegeDialog.showDialog(new CreateCompetencyQuestionsDialogController(
				new CreateCompetencyQuestionsDialogController.CreateCompetencyQuestionsHandler() {
			
			@Override
			public void handleCreateQuestions(
					CreateCompetencyQuestionsInfo createCompetencyQuestionsInfo) {
				//TODO: Add code to handle server side operation on
				// on input questions.
			}
		}));
	}
	
	/*
	 * This method creates the Competency Questions Table as a
	 * GridPanel.
	 */
	protected Panel createQuestionsPanel() {
		GridPanel questionsPanel = new GridPanel();
		
		RecordDef recordDef = new RecordDef(  
                new FieldDef[]{  
                        new StringFieldDef("question")
                }
        );  
		
		Object[][] data = getCQData();
		MemoryProxy proxy = new MemoryProxy(data);
		ArrayReader reader = new ArrayReader(recordDef);
		Store store = new Store(proxy, reader);
		store.load();
		
		BaseColumnConfig[] columns = new BaseColumnConfig[]{  
                new RowNumberingColumnConfig(),  
                //column ID is company which is later used in setAutoExpandColumn  
                new ColumnConfig("Competency Questions", "question", 1000, false, null, "question")
        };  
		ColumnModel columnModel = new ColumnModel(columns);  
		
		questionsPanel.setStore(store);
		questionsPanel.setColumnModel(columnModel);
		questionsPanel.setBorder(false);
		questionsPanel.setHeight("auto");
		questionsPanel.setAutoExpandColumn("question");
		questionsPanel.setAutoWidth(true);
		questionsPanel.setEnableColumnResize(true);
		
		Panel panel = new Panel();
        panel.setBorder(true);
        
        GridView view = new GridView();
        view.setForceFit(true);
        questionsPanel.setView(view);
        
        panel.add(questionsPanel);
        
        return panel;
	}

	/*
	 * TODO: This class should be removed once the data source is
	 * built up and connected.
	 */
	private Object[][] getCQData() {
		return new Object[][] {
				new Object[] {"What is pizza?"},
				new Object[] {"What is pizza topping?"},
				new Object[] {"What is pizza base?"},
				new Object[] {"What kinds of pizza toppings are there?"},
				new Object[] {"What kinds of pizza toppings are there?What kinds of pizza toppings are there?What kinds of pizza toppings are there?What kinds of pizza toppings are there?"}
		};
	}
}