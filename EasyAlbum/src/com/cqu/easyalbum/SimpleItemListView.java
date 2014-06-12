package com.cqu.easyalbum;

import android.widget.Toast;

import com.cqu.bean.DataItem;
import com.cqu.dao.GeneralDaoInterface;
import com.cqu.db.DBManager;
import com.cqu.db.DataModel;
import com.cqu.listadapter.ItemListAdapter;

public abstract class SimpleItemListView extends ItemListPageView{
	
	private final int VIEW_MODE_ALL_DATA=0;
	private final int VIEW_MODE_SEARCH_DATA=1;
	
	private final String HEAD_OPERATION_FOR_VIEW_MODE_ALL_DATA="添加相册";
	private final String HEAD_OPERATION_FOR_VIEW_MODE_SEARCH_DATA="退出搜索视图";
	
	private DataModel.PageModel pageModel=null;
	private DataModel dataModel=null;
	
	protected DBManager dbManager;
	
	private int curViewMode=VIEW_MODE_ALL_DATA;
	private String searchString="";
	
	protected GeneralDaoInterface dao;
	protected DataItem parent;
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
		pageModel=new DataModel.PageModel();
		dbManager=new DBManager(this);
		
		initData();
		
		loadNewPage(1);
	}
	
	protected abstract void initData();
	
	@Override
	protected boolean loadNewPage(int pageNumber) {
		// TODO Auto-generated method stub
		
		if(curViewMode==VIEW_MODE_SEARCH_DATA)
		{
			return loadSearchingDataPage(pageNumber);
		}else
		{
			return loadAllDataPage(pageNumber);
		}
	}
	
	@Override
	protected void searchData() {
		// TODO Auto-generated method stub
		searchString=etSearchString.getText().toString().trim();
		if(searchString.length()==0)
		{
			Toast.makeText(this, "搜索字符串为空", Toast.LENGTH_SHORT).show();
		}else
		{
			enableSearchingView(false);
			
			dataModel=null;
			curViewMode=VIEW_MODE_SEARCH_DATA;
			
			loadSearchingDataPage(1);
		}
	}
	
	private boolean loadAllDataPage(int pageNumber)
	{
		int pageIndex=pageNumber-1;
		DataItem[] itemPage=null;
		
		if(dataModel==null)
		{
			dataModel=dao.getItems(dbManager, pageModel, pageIndex, null);
			if(dataModel!=null)
			{
				itemPage=(DataItem[]) dataModel.getPage(pageIndex);
			}
		}else
		{
			itemPage=(DataItem[]) dataModel.getPage(pageIndex);
			if(itemPage==null)
			{
				dataModel=dao.getItems(dbManager, pageModel, pageIndex, null);
				
				itemPage=(DataItem[]) dataModel.getPage(pageIndex);
			}
		}
		if(itemPage==null)
		{
			itemListAdapter=new ItemListAdapter(this, new DataItem[]{}, HEAD_OPERATION_FOR_VIEW_MODE_ALL_DATA, this);
			lvDataList.setAdapter(itemListAdapter);
			
			refreshPageNumber(0, 0);
			return false;
		}else
		{
			itemListAdapter=new ItemListAdapter(this, itemPage, HEAD_OPERATION_FOR_VIEW_MODE_ALL_DATA, this);
			lvDataList.setAdapter(itemListAdapter);
			
			refreshPageNumber(pageNumber, dataModel.getTotalPageCount());
			return true;
		}
	}
	
	private boolean loadSearchingDataPage(int pageNumber)
	{
		int pageIndex=pageNumber-1;
		DataItem[] itemPage=null;
		
		if(dataModel==null)
		{
			dataModel=dao.searchItem(dbManager, pageModel, pageIndex, searchString, parent);
			if(dataModel!=null)
			{
				itemPage=(DataItem[]) dataModel.getPage(pageIndex);
			}
		}else
		{
			itemPage=(DataItem[]) dataModel.getPage(pageIndex);
			if(itemPage==null)
			{
				dataModel=dao.searchItem(dbManager, pageModel, pageIndex, searchString, parent);;
				
				itemPage=(DataItem[]) dataModel.getPage(pageIndex);
			}
		}
		if(itemPage==null)
		{
			itemListAdapter=new ItemListAdapter(this, new DataItem[]{}, HEAD_OPERATION_FOR_VIEW_MODE_SEARCH_DATA, this);
			lvDataList.setAdapter(itemListAdapter);
			
			refreshPageNumber(0, 0);
			return false;
		}else
		{
			itemListAdapter=new ItemListAdapter(this, itemPage, HEAD_OPERATION_FOR_VIEW_MODE_SEARCH_DATA, this);
			lvDataList.setAdapter(itemListAdapter);
			
			refreshPageNumber(pageNumber, dataModel.getTotalPageCount());
			return true;
		}
	}

	@Override
	protected void itemClicked(int pos, DataItem item) {
		// TODO Auto-generated method stub
		if(pos==0)
		{
			if(curViewMode==VIEW_MODE_SEARCH_DATA)
			{
				dataModel=null;
				curViewMode=VIEW_MODE_ALL_DATA;
				loadNewPage(1);
				
				enableSearchingView(true);
				return;
			}
			addItem();
		}else
		{
			itemSelected(item);
		}
	}
	
	protected abstract void addItem();
	protected abstract void itemSelected(DataItem item);
	
	protected void itemDeletedReset()
	{
		int spaceCount=dataModel.getSpaceCount();
		dataModel=null;//由于删除了一项，需要重新从数据库取数据
		if((spaceCount+1)%pageModel.countPerPage==0)//表明删除的一项单独占了一页，删除后该页也没有了
		{
			loadNewPage(getCurPageNumber()-1);
		}else
		{
			loadNewPage(getCurPageNumber());
		}
	}
	
	protected void itemAddedReset()
	{
		int spaceCount=dataModel.getSpaceCount();
		dataModel=null;//由于新加了一项，需要重新从数据库取数据
		if(spaceCount%pageModel.countPerPage==0)//表明新添加的一项新开了一页
		{
			loadNewPage(getCurPageNumber()+1);
		}else
		{
			loadNewPage(getCurPageNumber());
		}
	}
}
