package edu.stanford.bmir.protege.web.client.ui.termbuilder.recommend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.widget.client.TextButton;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesResult;
import edu.stanford.bmir.protege.web.client.dispatch.actions.RecommendConceptsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.RecommendConceptsResult;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.CompetencyQuestionsManager;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.question.CompetencyQuestionsViewPresenter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.termbuilder.Concept;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;

public class RecommendedConceptsListViewImpl extends Composite implements RecommendedConceptsListView {

	private final Project project;
	private  RecommendedConceptsListViewPresenter presenter = null;
	
	private static RecommendedConceptsListViewImplUiBinder uiBinder = GWT
			.create(RecommendedConceptsListViewImplUiBinder.class);

	interface RecommendedConceptsListViewImplUiBinder extends
			UiBinder<Widget, RecommendedConceptsListViewImpl> {
	}
	
	private final String CANDIDATE_CONCEPT_COL_TITLE = "Candidate Concepts";
	private final String RELATION_COL_TITLE = "Relation to Existing Concepts";
	private final String SELECT_COL_TITLE = "Select";
	private final String EMPTY_TABLE_LABEL = "There is no recommendations to show.";
	
	final MultiSelectionModel<RecommendedConceptInfo> selectionModel;
	
	@UiField(provided=true) DataGrid<RecommendedConceptInfo> dataGrid;
	@UiField Button refreshButton;
	@UiField Button acceptButton;

	public RecommendedConceptsListViewImpl(Project project) {
		ProvidesKey<RecommendedConceptInfo> providesKey = new ProvidesKey<RecommendedConceptInfo>() {
			@Override
			public Object getKey(RecommendedConceptInfo item) {
				return item.getId();
			}
		};
		
		this.project = project;
		
		dataGrid = new DataGrid<RecommendedConceptInfo>(1000, providesKey);
		initWidget(uiBinder.createAndBindUi(this));
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label(this.EMPTY_TABLE_LABEL));
		selectionModel =
				new MultiSelectionModel<RecommendedConceptInfo>(providesKey);
		dataGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager
				.<RecommendedConceptInfo> createCheckboxManager());
		initTableColumns(selectionModel);
		
	}
	
	private void initTableColumns(
			final SelectionModel<RecommendedConceptInfo> selectionModel) {
		
		TextColumn<RecommendedConceptInfo> candidateConceptColumn = 
				new TextColumn<RecommendedConceptInfo> () {
					@Override
					public String getValue(RecommendedConceptInfo object) {						
						return object.getRecommendedConcept().getConceptName();
					}
		};
		dataGrid.addColumn(candidateConceptColumn, this.CANDIDATE_CONCEPT_COL_TITLE);
		dataGrid.setColumnWidth(candidateConceptColumn, 25, Unit.PCT);
		
		TextColumn<RecommendedConceptInfo> relationColumn = 
				new TextColumn<RecommendedConceptInfo> () {
					@Override
					public String getValue(RecommendedConceptInfo object) {						
						return object.getConceptRelationDescription();
					}
		};
		dataGrid.addColumn(relationColumn, this.RELATION_COL_TITLE);
		dataGrid.setColumnWidth(relationColumn, 65, Unit.PCT);
		
		Column<RecommendedConceptInfo, Boolean> checkColumn =
				new Column<RecommendedConceptInfo, Boolean>(new CheckboxCell(true, false)) {
					@Override
					public Boolean getValue(RecommendedConceptInfo object) {
						return selectionModel.isSelected(object);
					}
		};
		dataGrid.addColumn(checkColumn, this.SELECT_COL_TITLE);
		dataGrid.setColumnWidth(checkColumn, 10, Unit.PCT);
	}

	@Override
    public Widget getWidget() {
        return this;
    }

	@UiHandler("refreshButton")
	void onRefreshButtonClick(ClickEvent event) {
		onRefresh();
	}
	
	private void onRefresh() {
		Set<Concept> conceptSet = project.getCompetencyQuestionsManager().getAcceptedConcepts();
		System.err.println("[Client] Accepted Concepts Number: " + conceptSet.size());
		HashSet<Concept> conceptHashSet = new HashSet<Concept>(conceptSet);
		RecommendConceptsAction action = new RecommendConceptsAction(project.getProjectId(), conceptHashSet);
		DispatchServiceManager.get().execute(action, getRecommendConceptsActionAsyncHandler());
	}
	
	private AsyncCallback<RecommendConceptsResult> getRecommendConceptsActionAsyncHandler() {
		return new AsyncCallback<RecommendConceptsResult>() {
			
			@Override
			public void onFailure(Throwable caught) {
				try {
					System.err.println("[Client] Recommend Concept Action Handling Error!");
					throw caught;
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onSuccess(RecommendConceptsResult result) {
				System.err.println("[Client] Recommend Concept Action Handling Succeed!");
				CompetencyQuestionsManager manager = project.getCompetencyQuestionsManager();
				manager.addRecommendedConcepts(result.getRecommendedConcepts());
				presenter.reload();
			}
			
		};
	}

	@UiHandler("acceptButton")
	void onAcceptButtonClick(ClickEvent event) {
		onAccept();
	}
	
	private void onAccept() {
		//Get CQ Manager
		CompetencyQuestionsManager manager = project.getCompetencyQuestionsManager();
		
		Set<RecommendedConceptInfo> selectedSet = selectionModel.getSelectedSet();
		List<String> selectedClassesArray = new ArrayList<String>();
		
		for(RecommendedConceptInfo info: selectedSet) {
			selectedClassesArray.add(info.getRecommendedConcept().getConceptName());
		}
		
		manager.addAcceptedConceptsFromString(selectedClassesArray);
//		Window.alert("You have accepted " + selectedClassesArray.size() + " concepts!");
		
		//Add classes into class tree
		final OWLClass superCls = DataFactory.getOWLThing();
		final Set<String> newClasses = new HashSet<String>(selectedClassesArray);
		DispatchServiceManager.get().execute(new CreateClassesAction(project.getProjectId(), superCls, newClasses),
				getCreateClassesActionAsyncHandler());
	}
	
	private AsyncCallback<CreateClassesResult> getCreateClassesActionAsyncHandler() {
        return new AsyncCallback<CreateClassesResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("There was a problem creating the classes.  Please try again.");
            }

            @Override
            public void onSuccess(CreateClassesResult result) {
            	System.err.println("Classes added into the tree!");
            }
        };
    }

	@Override
	public void setDataProvider(
			AbstractDataProvider<RecommendedConceptInfo> dataProvider) {
		dataProvider.addDataDisplay(dataGrid);
	}
	
	@Override
	public void setViewPresenter(RecommendedConceptsListViewPresenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public CompetencyQuestionsManager getCompetencyQuestionsManager() {
		return project.getCompetencyQuestionsManager();
	}
}
