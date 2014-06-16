package com.cqu.easyalbum;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;

import com.cqu.bean.DataItem;
import com.cqu.customizedview.ViewNumberUpdown;
import com.cqu.listadapter.OperationListener;

public abstract class ItemListPageView extends Activity implements OperationListener{
	
	protected ListView lvDataList;
	private ImageView ivSearchData;
	private ViewSwitcher vsMorePageOperations;
	
	private TextView tvMoreOperationsA;
	private TextView tvMoreOperationsB;
	private TextView tvPrevPage;
	private TextView tvNextPage;
	private TextView tvCurPageNumber;
	private TextView tvJumpToAPage;
	
	private TextView tvDecreaseNumber;
	private TextView tvIncreaseNumber;
	private TextView tvNumberValue;
	protected ViewNumberUpdown viewNumberUpdown;
	
	protected EditText etSearchString;
	
	private int curPageNumber=0;
	private int totalPage=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_list_page_view);

		init();
	}

	protected void init() {
		lvDataList = (ListView) findViewById(R.id.lvItemList);
		ivSearchData=(ImageView)findViewById(R.id.ivSearchTable);
		vsMorePageOperations=(ViewSwitcher) findViewById(R.id.vsPageOperation);
		tvMoreOperationsA=(TextView)findViewById(R.id.tvMoreOperationsA);
		tvMoreOperationsB=(TextView)findViewById(R.id.tvMoreOperationsB);
		tvPrevPage=(TextView) findViewById(R.id.tvPreviousPage);
		tvNextPage=(TextView) findViewById(R.id.tvNextPage);
		tvCurPageNumber=(TextView) findViewById(R.id.tvCurrentPageNumber);
		tvJumpToAPage=(TextView) findViewById(R.id.tvJumpToAPage);
		
		tvDecreaseNumber=(TextView) findViewById(R.id.tvDecreaseNumber);
		tvIncreaseNumber=(TextView) findViewById(R.id.tvIncreaseNumber);
		tvNumberValue=(TextView) findViewById(R.id.tvNumberValue);
		viewNumberUpdown=new ViewNumberUpdown(1, 1, 3, 1, 10);
		viewNumberUpdown.setViews(tvDecreaseNumber, tvIncreaseNumber, tvNumberValue);
		
		etSearchString=(EditText) findViewById(R.id.etTableSearchString);
		
		ivSearchData.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchData();
			}
		});
		
		tvPrevPage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(curPageNumber>1)
				{
					loadNewPage(curPageNumber-1);
				}
			}
		});
		
		tvNextPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(curPageNumber<totalPage)
				{
					loadNewPage(curPageNumber+1);
				}
			}
		});

		tvJumpToAPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pageNum=viewNumberUpdown.getValue();
				if(curPageNumber!=pageNum)
				{
					if(loadNewPage(pageNum)==true)
					{
						vsMorePageOperations.setDisplayedChild(0);
					}
				}
			}
		});
		
		tvMoreOperationsA.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vsMorePageOperations.setDisplayedChild(1);
			}
		});
		
        tvMoreOperationsB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vsMorePageOperations.setDisplayedChild(0);
			}
		});
        
        lvDataList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				itemClicked(pos, (DataItem)lvDataList.getAdapter().getItem(pos));
			}
			
		});
	}
	
	protected void enableSearchingView(boolean enable)
	{
		etSearchString.setEnabled(enable);
		ivSearchData.setEnabled(enable);
	}
	
	protected int getCurPageNumber()
	{
		return this.curPageNumber;
	}
	
	protected abstract void itemClicked(int pos, DataItem item);
	
	protected abstract void searchData();
	
	protected abstract boolean loadNewPage(int pageNumber);
	
	protected void refreshPageNumber(int curPage_)
	{
		this.curPageNumber=curPage_;
		this.tvCurPageNumber.setText("第"+curPageNumber+"页 "+"共"+totalPage+"页");
	}
	
	protected void refreshPageNumber(int curPage_, int totalPage_)
	{
		this.curPageNumber=curPage_;
		if(this.totalPage!=totalPage_)
		{
			this.totalPage=totalPage_;
			viewNumberUpdown.setMax(this.totalPage);
		}
		
		this.tvCurPageNumber.setText("第"+curPageNumber+"页 "+"共"+totalPage+"页");
	}
}
