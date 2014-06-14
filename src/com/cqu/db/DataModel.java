package com.cqu.db;

import java.io.Serializable;
import java.util.List;

public class DataModel {
	
	private PageModel pageModel;
	
	private int totalItemCount=-1;
	private int totalPageCount=-1;
	
	private List<Object[]> dataItemList;
	private int curBatchIndex=-1;
	
	private int spaceCount=0;
	
	public DataModel(PageModel model) {
		// TODO Auto-generated constructor stub
		this.pageModel=model;
	}

	public int getSpaceCount() {
		return spaceCount;
	}

	public void setSpaceCount(int spaceCount) {
		this.spaceCount = spaceCount;
	}

	public void setData(List<Object[]> dataItemList, int totalItemCount, int batchIndex)
	{
		this.dataItemList=dataItemList;
		
		this.totalItemCount=totalItemCount;
		this.totalPageCount=(this.totalItemCount+pageModel.countPerPage-1)/pageModel.countPerPage;
		
		this.curBatchIndex=batchIndex;
	}
	
	public Object[] getPage(int pageIndex)
	{
		int availPageFrom=curBatchIndex*pageModel.pagePerBatch;
		int availPageTo=(curBatchIndex+1)*pageModel.pagePerBatch-1;
		if(pageIndex>=availPageFrom&&pageIndex<=availPageTo)
		{
			return dataItemList.get(pageIndex%pageModel.pagePerBatch);
		}else
		{
			return null;
		}
	}
	
	public int getTotalPageCount()
	{
		return this.totalPageCount;
	}
	
	public int getTotalItemCount()
	{
		return this.totalItemCount;
	}
	
	public static class PageModel implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1513678064703315734L;
		
		public final int pagePerBatch;
		public final int countPerPage;
		
		public PageModel() {
			// TODO Auto-generated constructor stub
			this.pagePerBatch=5;
			this.countPerPage=10;
		}
		
		public PageModel(int pagePerBatch, int countPerPage) {
			// TODO Auto-generated constructor stub
			this.pagePerBatch=pagePerBatch;
			this.countPerPage=countPerPage;
		}
		
		public int countPerBatch()
		{
			return pagePerBatch*countPerPage;
		}
	}
}
