package edu.stanford.bmir.protege.web.client.ui.termbuilder;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
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

import edu.stanford.bmir.protege.web.shared.termbuilder.RecommendedConceptInfo;

public class RecommendedConceptsListViewImpl extends Composite implements RecommendedConceptsListView {

	private static RecommendedConceptsListViewImplUiBinder uiBinder = GWT
			.create(RecommendedConceptsListViewImplUiBinder.class);

	interface RecommendedConceptsListViewImplUiBinder extends
			UiBinder<Widget, RecommendedConceptsListViewImpl> {
	}
	
	private final String CANDIDATE_CONCEPT_COL_TITLE = "Candidate Concepts";
	private final String RELATION_COL_TITLE = "Relation to Existing Concepts";
	private final String SELECT_COL_TITLE = "Select";
	private final String EMPTY_TABLE_LABEL = "There is no recommendations to show.";
	
	@UiField(provided=true) DataGrid<RecommendedConceptInfo> dataGrid;
	@UiField Button refreshButton = new Button();
	@UiField Button acceptButton = new Button();

	public RecommendedConceptsListViewImpl() {
		ProvidesKey<RecommendedConceptInfo> providesKey = new ProvidesKey<RecommendedConceptInfo>() {
			@Override
			public Object getKey(RecommendedConceptInfo item) {
				return item.getId();
			}
		};
		
		dataGrid = new DataGrid<RecommendedConceptInfo>(1000, providesKey);
		initWidget(uiBinder.createAndBindUi(this));
		dataGrid.setAutoHeaderRefreshDisabled(true);
		dataGrid.setEmptyTableWidget(new Label(this.EMPTY_TABLE_LABEL));
		final SelectionModel<RecommendedConceptInfo> selectionModel =
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
		//TODO: Add click event handler
	}
	
	@UiHandler("acceptButton")
	void onAcceptButtonClick(ClickEvent event) {
		//TODO: Add click event handler
	}

	@Override
	public void setDataProvider(
			AbstractDataProvider<RecommendedConceptInfo> dataProvider) {
		dataProvider.addDataDisplay(dataGrid);
		
	}
}
