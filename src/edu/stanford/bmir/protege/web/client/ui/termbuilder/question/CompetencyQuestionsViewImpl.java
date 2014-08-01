package edu.stanford.bmir.protege.web.client.ui.termbuilder.question;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GenerateConceptsAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.GenerateConceptsResult;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.CompetencyQuestionsManager;
import edu.stanford.bmir.protege.web.client.ui.termbuilder.ExtractedConceptsManager;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.event.EventBusManager;
import edu.stanford.bmir.protege.web.shared.termbuilder.CompetencyQuestionInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.ConceptList;
import edu.stanford.bmir.protege.web.shared.termbuilder.ExtractedConceptsChangedEvent;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;

public class CompetencyQuestionsViewImpl extends Composite implements CompetencyQuestionsView {

	private static CompetencyQuestionsViewImplUiBinder uiBinder = GWT
			.create(CompetencyQuestionsViewImplUiBinder.class);

	interface CompetencyQuestionsViewImplUiBinder extends
			UiBinder<Widget, CompetencyQuestionsViewImpl> {
	}
	
	private final String COMPETENCY_QUESTIONS_COL_TITLE = "Competency Questions";
	private final String SELECT_COL_TITLE = "Select";
	private final String EMPTY_TABLE_LABEL = "There is no competency question to show.";
	
	private final Project project;
	private CompetencyQuestionsViewPresenter presenter = null;
	private final MultiSelectionModel<CompetencyQuestionInfo> selectionModel;

    private CompetencyQuestionsManager competencyQuestionsManager;
    private ExtractedConceptsManager extractedConceptsManager;

	@UiField(provided=true) DataGrid<CompetencyQuestionInfo> dataGrid;
	@UiField Button createButton;
	@UiField Button deleteButton;
	@UiField Button clearButton;
	@UiField Button generateButton;
	
	public CompetencyQuestionsViewImpl(Project project) {
		ProvidesKey<CompetencyQuestionInfo> providesKey = new ProvidesKey<CompetencyQuestionInfo>() {
			@Override
			public Object getKey(CompetencyQuestionInfo item) {
				return item.getId();
			}
		};
		
		this.project = project;
		
		dataGrid = new DataGrid<CompetencyQuestionInfo>(1000, providesKey);
		initWidget(uiBinder.createAndBindUi(this));
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label(this.EMPTY_TABLE_LABEL));
		selectionModel =
				new MultiSelectionModel<CompetencyQuestionInfo>(providesKey);
        dataGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager.<CompetencyQuestionInfo> createDefaultManager());
		initTableColumns(selectionModel);

        competencyQuestionsManager = project.getTermBuilderManagerBoard().getCompetencyQuestionsManager();
        extractedConceptsManager = project.getTermBuilderManagerBoard().getExtractedConceptsManager();
	}
	
	private void initTableColumns(
			final SelectionModel<CompetencyQuestionInfo> selectionModel) {
		
		TextColumn<CompetencyQuestionInfo> questionColumn = 
				new TextColumn<CompetencyQuestionInfo> () {
					@Override
					public String getValue(CompetencyQuestionInfo object) {						
						return object.getQuestion();
					}
		};
		dataGrid.addColumn(questionColumn, this.COMPETENCY_QUESTIONS_COL_TITLE);
		dataGrid.setColumnWidth(questionColumn, 100, Unit.PCT);

	}

	@Override
    public Widget getWidget() {
        return this;
    }

	@UiHandler("createButton")
	void onCreateButtonClick(ClickEvent event) {
		onCreateQuestions();
	}
	
	@UiHandler("deleteButton")
	void onDeleteButtonClick(ClickEvent event) {
		onDeleteQuestions();
	}

	@UiHandler("clearButton")
	void onClearButtonClick(ClickEvent event) {
		onClearQuestions();
	}
	
	@UiHandler("generateButton")
	void onGenerateButtonClick(ClickEvent event) {
		onGenerateConcepts();
	}
	
	protected void onCreateQuestions() {
		WebProtegeDialog.showDialog(new CreateCompetencyQuestionsDialogController(
				new CreateCompetencyQuestionsDialogController.CreateCompetencyQuestionsHandler() {
			
			@Override
			public void handleCreateQuestions(
					CreateCompetencyQuestionsInfo createCompetencyQuestionsInfo) {
				//TODO: Add code to handle server side operation on
				// on input questions.
				final Set<String> browserTexts = new HashSet<String>(createCompetencyQuestionsInfo.getBrowserTexts());
				project.getTermBuilderManagerBoard().getCompetencyQuestionsManager().addQuestionFromStringSet(browserTexts);
				presenter.reload();
			}
		}));
	}
	
	private void onDeleteQuestions() {
		Set<CompetencyQuestionInfo> selectedSet = selectionModel.getSelectedSet();
		competencyQuestionsManager.removeQuestionsInSet(selectedSet);
        extractedConceptsManager.removeExtractedConceptsForQuestions(selectedSet);
		//You have to clear all the selection in selectionModel
		selectionModel.clear();
		presenter.reload();
	}
	
	private void onClearQuestions() {
		competencyQuestionsManager.clearQuestions();
        extractedConceptsManager.clearExtractedConcepts();
		presenter.reload();
	}
	
	private void onGenerateConcepts() {
		System.err.println("[Client] Generate Button Clicked!");
		List<CompetencyQuestionInfo> questions = competencyQuestionsManager.getQuestions();
		GenerateConceptsAction action = new GenerateConceptsAction(project.getProjectId(), questions);
		DispatchServiceManager.get().execute(action, getGenerateConceptsActionAsyncHandler());
	}
	
	private AsyncCallback<GenerateConceptsResult> getGenerateConceptsActionAsyncHandler() {
		return new AsyncCallback<GenerateConceptsResult>() {

			@Override
			public void onFailure(Throwable caught) {
				System.err.println("[Client] Generate Concept Action Handling Error!");
			}

			@Override
			public void onSuccess(GenerateConceptsResult result) {
				System.err.println("[Client] Generate Concept Action Handling return Success!");
				Map<CompetencyQuestionInfo, ConceptList> map = result.getQuestionToConceptMap();
				for(CompetencyQuestionInfo info: map.keySet()) {
                    extractedConceptsManager.addExtractedConceptsForSingleQuestion(map.get(info).getList(), info.getId());
				}
				EventBusManager.getManager().postEvent(new ExtractedConceptsChangedEvent(project.getProjectId()));
			}
		};
	}

	@Override
	public void setDataProvider(
			AbstractDataProvider<CompetencyQuestionInfo> dataProvider) {
		dataProvider.addDataDisplay(dataGrid);
	}

	@Override
	public void setViewPresenter(CompetencyQuestionsViewPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public CompetencyQuestionsManager getCompetencyQuestionsManager() {
		return project.getTermBuilderManagerBoard().getCompetencyQuestionsManager();
	}
}
