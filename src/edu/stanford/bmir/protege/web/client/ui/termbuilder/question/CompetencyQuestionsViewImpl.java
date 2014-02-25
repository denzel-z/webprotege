package edu.stanford.bmir.protege.web.client.ui.termbuilder.question;

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

import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialog;
import edu.stanford.bmir.protege.web.shared.termbuilder.CompetencyQuestionInfo;
import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;

public class CompetencyQuestionsViewImpl extends Composite implements CompetencyQuestionsView {

	private static CompetencyQuestionsViewImplUiBinder uiBinder = GWT
			.create(CompetencyQuestionsViewImplUiBinder.class);

	interface CompetencyQuestionsViewImplUiBinder extends
			UiBinder<Widget, CompetencyQuestionsViewImpl> {
	}
	
	private final String COMPETENCY_QUESTIONS_COL_TITLE = "Competentcy Questions";
	private final String SELECT_COL_TITLE = "Select";
	private final String EMPTY_TABLE_LABEL = "There is no competency question to show.";

	@UiField(provided=true) DataGrid<CompetencyQuestionInfo> dataGrid;
	@UiField Button createButton;
	@UiField Button deleteButton;
	@UiField Button clearButton;
	@UiField Button generateButton;
	
	public CompetencyQuestionsViewImpl() {
		ProvidesKey<CompetencyQuestionInfo> providesKey = new ProvidesKey<CompetencyQuestionInfo>() {
			@Override
			public Object getKey(CompetencyQuestionInfo item) {
				return item.getId();
			}
		};
		
		dataGrid = new DataGrid<CompetencyQuestionInfo>(1000, providesKey);
		initWidget(uiBinder.createAndBindUi(this));
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label(this.EMPTY_TABLE_LABEL));
		final SelectionModel<CompetencyQuestionInfo> selectionModel =
				new MultiSelectionModel<CompetencyQuestionInfo>(providesKey);
		dataGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager
				.<CompetencyQuestionInfo> createCheckboxManager());
		initTableColumns(selectionModel);
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
		dataGrid.setColumnWidth(questionColumn, 90, Unit.PCT);
		
		Column<CompetencyQuestionInfo, Boolean> checkColumn =
				new Column<CompetencyQuestionInfo, Boolean>(new CheckboxCell(true, false)) {
					@Override
					public Boolean getValue(CompetencyQuestionInfo object) {
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

	@UiHandler("createButton")
	void onCreateButtonClick(ClickEvent event) {
		onCreateQuestions();
	}
	
	@UiHandler("deleteButton")
	void onDeleteButtonClick(ClickEvent event) {
		//TODO: Add click event handler
	}
	
	@UiHandler("clearButton")
	void onClearButtonClick(ClickEvent event) {
		//TODO: Add click event handler
	}
	
	@UiHandler("generateButton")
	void onGenerateButtonClick(ClickEvent event) {
		//TODO: Add click event handler
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

	@Override
	public void setDataProvider(
			AbstractDataProvider<CompetencyQuestionInfo> dataProvider) {
		dataProvider.addDataDisplay(dataGrid);
		
	}
}
