package com.cqu.easyalbum;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.cqu.bean.DataItem;
import com.cqu.dao.GeneralDaoInterface;
import com.cqu.db.DBManager;
import com.cqu.db.DataModel;
import com.cqu.listadapter.OperationListener;

public abstract class SimpleItemListView extends ItemListPageView{
	
	protected ListAdapter itemListAdapter;
	protected String headOperation="";
	private final String HEAD_OPERATION_FOR_VIEW_MODE_SEARCH_DATA="退出搜索视图";
	
	protected DataModel.PageModel pageModel=null;
	private DataModel dataModel=null;
	
	protected DBManager dbManager;
	
	private int curDataViewMode=DataViewMode.MODE_ALL_DATA;
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
	
	protected int getCurDataViewMode()
	{
		return this.curDataViewMode;
	}
	
	protected abstract void initData();
	
	@Override
	protected boolean loadNewPage(int pageNumber) {
		// TODO Auto-generated method stub
		
		if(curDataViewMode==DataViewMode.MODE_SEARCH_DATA)
		{
			return loadSearchingDataPage(pageNumber);
		}else
		{
			return loadAllDataPage(pageNumber);
		}
	}
	
	protected String getSearchString()
	{
		return this.searchString;
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
			curDataViewMode=DataViewMode.MODE_SEARCH_DATA;
			
			loadSearchingDataPage(1);
		}
	}
	
	protected abstract BaseAdapter getListAdapter(Context context, DataItem[] dataItems, String headOperation, OperationListener opListener);
	
	private boolean loadAllDataPage(int pageNumber)
	{
		int pageIndex=pageNumber-1;
		DataItem[] itemPage=null;
		
		if(dataModel==null)
		{
			dataModel=dao.getItems(dbManager, pageModel, pageIndex, parent);
			if(dataModel!=null)
			{
				itemPage=(DataItem[]) dataModel.getPage(pageIndex);
			}
		}else
		{
			itemPage=(DataItem[]) dataModel.getPage(pageIndex);
			if(itemPage==null)
			{
				dataModel=dao.getItems(dbManager, pageModel, pageIndex, parent);
				
				itemPage=(DataItem[]) dataModel.getPage(pageIndex);
			}
		}
		if(itemPage==null)
		{
			itemListAdapter=this.getListAdapter(this, new DataItem[]{}, headOperation, this);
			lvDataList.setAdapter(itemListAdapter);
			
			refreshPageNumber(0, 0);
			return false;
		}else
		{
			itemListAdapter=this.getListAdapter(this, itemPage, headOperation, this);
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
			itemListAdapter=this.getListAdapter(this, new DataItem[]{}, HEAD_OPERATION_FOR_VIEW_MODE_SEARCH_DATA, this);
			lvDataList.setAdapter(itemListAdapter);
			
			refreshPageNumber(0, 0);
			return false;
		}else
		{
			itemListAdapter=this.getListAdapter(this, itemPage, HEAD_OPERATION_FOR_VIEW_MODE_SEARCH_DATA, this);
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
			if(curDataViewMode==DataViewMode.MODE_SEARCH_DATA)
			{
				dataModel=null;
				curDataViewMode=DataViewMode.MODE_ALL_DATA;
				loadNewPage(1);
				
				enableSearchingView(true);
				return;
			}
			addItem();
		}else
		{
			//由于pos=0是添加命令，故需要-1
			itemSelected(pos-1, item);
		}
	}
	
	protected abstract void addItem();
	protected abstract void itemSelected(int index, DataItem item);
	
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
		if(dataModel==null)
		{
			loadNewPage(1);
		}else
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
}
