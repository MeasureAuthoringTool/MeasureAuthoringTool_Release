package mat.client.codelist;

import mat.client.CustomPager;
import mat.client.codelist.events.OnChangeOptionsEvent;
import mat.client.measure.AdminMeasureSearchResultAdaptor;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.metadata.Grid508;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.HasSortHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.model.CodeListSearchDTO;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class AdminValueSetSearchView implements ManageCodeListSearchPresenter.AdminValueSetSearchDisplay {
	private FlowPanel searchCriteriaPanel = new FlowPanel();
	final FormPanel form = new FormPanel();
	public CellTable<CodeListSearchDTO> cellTable = new CellTable<CodeListSearchDTO>();
	ListDataProvider<CodeListSearchDTO> sortProvider = new ListDataProvider<CodeListSearchDTO>();
	private SearchView<CodeListSearchDTO> view = new SearchView<CodeListSearchDTO>(true);
	private AdminCodeListSearchResultsAdapter adapter;
	private Button searchButton = new PrimaryButton("Search","primaryGreyLeftButton");
	private TextBox searchInput = new TextBox();
	Button transferButton = new PrimaryButton("Transfer","primaryGreyButton");
	private Button clearButton = new PrimaryButton("Clear","primaryGreyLeftButton");
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	protected ErrorMessageDisplay transferErrorMessages = new ErrorMessageDisplay();
	private ValueSetSearchFilterPanel vssfp = new ValueSetSearchFilterPanel();
	
	AdminValueSetSearchView (){
		view.buildDataTable(new AdminManageCodeListSearchModel());
		searchCriteriaPanel.add(errorMessages);
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(buildSearchWidget());
		searchCriteriaPanel.add(view.asWidget());
		searchCriteriaPanel.setStyleName("contentPanel");
		searchCriteriaPanel.add(transferErrorMessages);
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(buildTransferWidget());
		searchCriteriaPanel.add(new SpacerWidget()); 
	}
	
	private Widget buildSearchWidget(){
		HorizontalPanel hp = new HorizontalPanel();
		FlowPanel fp1 = new FlowPanel();
		fp1.add(searchInput);
		fp1.add(searchButton);
		fp1.add(new SpacerWidget());
		hp.add(fp1);
		return hp;
	}
	
	private Widget buildTransferWidget(){
		FlowPanel hpT = new FlowPanel();
		hpT.add(errorMessages);
		hpT.add(transferButton);
		hpT.add(clearButton);
		form.setWidget(hpT);
		return form;
	}
	
	
	
	/*protected void buildSearchResults(int numRows,int numColumns,final SearchResults results){
		
		for(int i = 0; i < numRows; i++) {
			
			if(i > 0){
				CodeListSearchDTO result = (CodeListSearchDTO)results.get(i);
				String currentValueSetFamily = result.getOid();
				result = (CodeListSearchDTO)results.get(i-1);
				String previousValueSetFamily = result.getOid();
				if(!currentValueSetFamily.equalsIgnoreCase(previousValueSetFamily)){
					odd = !odd;
					addImage = true;
				}else{
					addImage = false;
				}
			}else{
				odd = false;
				addImage = true;
			}
			if(addImage){
				((CodeListSearchDTO)results.get(i)).setTransferable(true);
			}
			for(int j = 0; j < numColumns; j++) {
				if(results.isColumnFiresSelection(j)) {
					String innerText = results.getValue(i, j).getElement().getInnerText();
					innerText = addSpaces(innerText, 27);
					Label a = new Label();
					a.setText(innerText);
					final int rowIndex = i;
					Panel holder = new HorizontalFlowPanel();
					SimplePanel innerPanel = new SimplePanel();
					if(addImage){
						
						innerPanel.setStylePrimaryName("pad-right5px");
						Image image = createImage(rowIndex, results, innerText);
						innerPanel.add(image);
						holder.add(innerPanel);
						holder.add(a);
					}else{
						innerPanel.setStylePrimaryName("pad-left21px");
						innerPanel.add(a);
						holder.add(innerPanel);
					}
					
					dataTable.setWidget(i+1, j, holder);
				}
				else {
					dataTable.setWidget(i+1, j,results.getValue(i, j));
				}
			}
			if(odd){
				dataTable.getRowFormatter().addStyleName(i + 1, "odd");
			}else{
				//if already set to 'odd' and we are just refreshing, then 'odd' has to be removed
				dataTable.getRowFormatter().removeStyleName(i + 1, "odd");
			}
		}
	}*/
	
	
	public void clearTransferCheckBoxes(){
		for(CodeListSearchDTO result : sortProvider.getList()){
				result.setTransferable(false);
		}
		sortProvider.refresh();
	}
	
	@Override
	public void buildDataTable(SearchResults<CodeListSearchDTO> results, boolean isAsc) {		
		buildCodeListDataTable(results);//Default value for isAscending is true and isChecked is false.
	}
	
	
	private void buildCodeListDataTable(SearchResults<CodeListSearchDTO> results){
		if(results == null) {
			return;
		}
		//CellTable<ManageMeasureSearchModel.Result> table = new CellTable<ManageMeasureSearchModel.Result>();
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		sortProvider = new ListDataProvider<CodeListSearchDTO>();
		
		// Display 50 rows in one page or all records.
		cellTable.setPageSize(50);
		//cellTable.setSelectionModel(adapter.addSelectionHandlerOnTable());
		cellTable = adapter.addColumnToTable(cellTable);
		cellTable.redraw();
		sortProvider.refresh();
		sortProvider.setList(adapter.getModel().getData());
			
		sortProvider.addDataDisplay(cellTable);
		//Used custom pager class - for disabling next/last button when on last page and for showing correct pagination number.
		MatSimplePager spager;
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
	    spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
        spager.setDisplay(cellTable);
        spager.setPageStart(0);
        spager.setToolTipAndTabIndex(spager);
        view.getvPanelForQDMTable().clear();
        view.getvPanelForQDMTable().add(cellTable);
		view.getvPanelForQDMTable().add(new SpacerWidget());
		view.getvPanelForQDMTable().add(spager);
		

	}

	
	public SearchView<CodeListSearchDTO> getView() {
		return view;
	}

	@Override
	public Widget asWidget() {
		return searchCriteriaPanel;
	}

	@Override
	public HasClickHandlers getSearchButton() {
		// TODO Auto-generated method stub
		return searchButton;
	}

	@Override
	public HasValue<String> getSearchString() {
		// TODO Auto-generated method stub
		return searchInput;
	}

	@Override
	public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForEditTool() {
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForQDSElement() {
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public void buildDataTable(SearchResults<CodeListSearchDTO> results) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public HasSortHandler getPageSortTool() {
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		// TODO Auto-generated method stub
		return errorMessages;
	}

	@Override
	public ErrorMessageDisplayInterface getTransferErrorMessageDisplay() {
		// TODO Auto-generated method stub
		return transferErrorMessages;
	}

	@Override
	public HasClickHandlers getTransferButton() {
		// TODO Auto-generated method stub
		return transferButton;
	}

	@Override
	public HasClickHandlers getCreateButton() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearSelections() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSelectedOption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPageSize() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public ValueSetSearchFilterPanel getValueSetSearchFilterPanel() {
		// TODO Auto-generated method stub
		return vssfp;
	}

	@Override
	public Grid508 getDataTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearAllCheckBoxes(Grid508 dataTable) {
			
	}

	public AdminCodeListSearchResultsAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(AdminCodeListSearchResultsAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public HasClickHandlers getClearButton() {
		// TODO Auto-generated method stub
		return clearButton;
	}
	
}