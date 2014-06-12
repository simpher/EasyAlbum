package com.cqu.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cqu.bean.DataItem;
import com.cqu.db.DBManager;
import com.cqu.db.DataModel;
import com.cqu.db.DataModel.PageModel;

public class DaoAlbum implements GeneralDaoInterface{
	
	@Override
	public DataModel getItems(DBManager dbManager, PageModel pageModel,
			int pageIndex, DataItem parent) {
		// TODO Auto-generated method stub
		DataModel dataModel=new DataModel(pageModel);
		int totalItemCount=0;
		int batchIndex=pageIndex/pageModel.pagePerBatch;
		int offset=batchIndex*pageModel.countPerBatch();
		
		SQLiteDatabase db=dbManager.getDB();
		Cursor c=null;
		try
		{
			String sql="select count(*) from Album";
			c=db.rawQuery(sql, null);
			if(c!=null&&c.getCount()>0)
			{
				c.move(1);
				totalItemCount=c.getInt(0);
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			if(c!=null)
			{
				c.close();
				c=null;
			}
		}
		
		
		List<Object[]> itemList=null;
		try
		{
			String sql="select id, name from Album limit "+pageModel.countPerBatch()+" offset "+ offset;
			c=db.rawQuery(sql, null);
			if(c!=null&&c.getCount()>0)
			{
				int pageCount=(c.getCount()+pageModel.countPerPage-1)/pageModel.countPerPage;
				int spaceCount=(pageModel.pagePerBatch-(pageCount-1))*pageModel.countPerPage;
				
				itemList=new ArrayList<Object[]>();
				for(int k=0;k<(pageCount-1);k++)
				{
					DataItem[] itemsOfAPage=new DataItem[pageModel.countPerPage];
					for(int i=0;i<pageModel.countPerPage;i++)
					{
						c.move(1);
						itemsOfAPage[i]=new DataItem(c.getInt(0), c.getString(1));
					}
					itemList.add(itemsOfAPage);
				}
				{
					int remainingCount=c.getCount()-(pageCount-1)*pageModel.countPerPage;
					spaceCount+=(pageModel.countPerPage-remainingCount);
					dataModel.setSpaceCount(spaceCount);
					if(remainingCount>0)
					{
						DataItem[] itemsOfLastPage=new DataItem[remainingCount];
						for(int i=0;i<remainingCount;i++)
						{
							c.move(1);
							itemsOfLastPage[i]=new DataItem(c.getInt(0), c.getString(1));
						}
						itemList.add(itemsOfLastPage);
					}
				}
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			if(c!=null)
			{
				c.close();
				c=null;
			}
		}
		
		if(itemList!=null&&itemList.size()>0)
		{
			dataModel.setData(itemList, totalItemCount, batchIndex);
			
			return dataModel;
		}else
		{
			return null;
		}
	}


	@Override
	public DataModel searchItem(DBManager dbManager, PageModel pageModel,
			int pageIndex, String nameFragment, DataItem parent) {
		// TODO Auto-generated method stub
		DataModel dataModel=new DataModel(pageModel);
		int totalItemCount=0;
		int batchIndex=pageIndex/pageModel.pagePerBatch;
		int offset=batchIndex*pageModel.countPerBatch();
		
		SQLiteDatabase db=dbManager.getDB();
		Cursor c=null;
		try
		{
			String sql="select count(*) from Album where name like '%"+nameFragment+"%'";
			c=db.rawQuery(sql, null);
			if(c!=null&&c.getCount()>0)
			{
				c.move(1);
				totalItemCount=c.getInt(0);
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			if(c!=null)
			{
				c.close();
				c=null;
			}
		}
		
		
		List<Object[]> itemList=null;
		try
		{
			String sql="select id, name from Album where name like '%"+nameFragment+"%' limit "+pageModel.countPerBatch()+" offset "+ offset;
			c=db.rawQuery(sql, null);
			if(c!=null&&c.getCount()>0)
			{
				int pageCount=(c.getCount()+pageModel.countPerPage-1)/pageModel.countPerPage;
				int spaceCount=(pageModel.pagePerBatch-(pageCount-1))*pageModel.countPerPage;
				
				itemList=new ArrayList<Object[]>();
				for(int k=0;k<(pageCount-1);k++)
				{
					DataItem[] itemsOfAPage=new DataItem[pageModel.countPerPage];
					for(int i=0;i<pageModel.countPerPage;i++)
					{
						c.move(1);
						itemsOfAPage[i]=new DataItem(c.getInt(0), c.getString(1));
					}
					itemList.add(itemsOfAPage);
				}
				{
					int remainingCount=c.getCount()-(pageCount-1)*pageModel.countPerPage;
					spaceCount+=(pageModel.countPerPage-remainingCount);
					dataModel.setSpaceCount(spaceCount);
					if(remainingCount>0)
					{
						DataItem[] itemsOfLastPage=new DataItem[remainingCount];
						for(int i=0;i<remainingCount;i++)
						{
							c.move(1);
							itemsOfLastPage[i]=new DataItem(c.getInt(0), c.getString(1));
						}
						itemList.add(itemsOfLastPage);
					}
				}
			}
			
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			if(c!=null)
			{
				c.close();
				c=null;
			}
		}
		
		if(itemList!=null&&itemList.size()>0)
		{
			dataModel.setData(itemList, totalItemCount, batchIndex);
			
			return dataModel;
		}else
		{
			return null;
		}
	}


	@Override
	public int exists(DBManager dbManager, String name, DataItem parent) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dbManager.getDB();
		Cursor c=null;
		try{
			String sql="select id from Album where name='"+name+"'";
			c=db.rawQuery(sql, null);
			if(c!=null&&c.getCount()>0)
			{
				c.move(1);
				return c.getInt(0);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			if(c!=null)
			{
				c.close();
				c=null;
			}
		}
		
		return -1;
	}


	@Override
	public boolean addItem(DBManager dbManager, DataItem itemToAdd) {
		// TODO Auto-generated method stub
		try{
			ContentValues cv=new ContentValues();
			cv.put("name", itemToAdd.getName());
			dbManager.getDB().insert("Album", null, cv);
			return true;
		}catch(SQLException e)
		{
			return false;
		}
	}
	
	@Override
	public boolean addItems(DBManager dbManager, DataItem[] itemsToAdd) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean updateItem(DBManager dbManager, DataItem itemNew) {
		// TODO Auto-generated method stub
		try{
			ContentValues cv=new ContentValues();
			cv.put("name", itemNew.getName());
			dbManager.getDB().update("Album", cv, "id=?", new String[]{itemNew.getId()+""});
			return true;
		}catch(SQLException e)
		{
			return false;
		}
	}


	@Override
	public boolean deleteItem(DBManager dbManager, int id, boolean isEmpty) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dbManager.getDB();
		try
		{
			if(isEmpty==false)
			{
				db.beginTransaction();
				try{
					DaoImageItem daoImageItem=new DaoImageItem();
					Cursor c=db.rawQuery("select id from ImageItem where albumid="+id, null);
					while(c.move(1))
					{
						daoImageItem.deleteImageItem(db, c.getInt(0));
					}
					
					DaoGeneral.deleteTableRecord(db, "Album", id);
					
					db.setTransactionSuccessful();
				}finally
				{
					db.endTransaction();
				}
			}else
			{
				DaoGeneral.deleteTableRecord(db, "DataTable", id);
			}
			
			return true;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean deleteItems(DBManager dbManager, int[] ids) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int queryImageCount(DBManager dbManager, int albumId)
	{
		SQLiteDatabase db=dbManager.getDB();
		Cursor c=null;
		int imageCount=0;
		try{
			String sql="select count(*) from ImageItem where albumid="+albumId;
			c=db.rawQuery(sql, null);
			if(c!=null&&c.getCount()>0)
			{
				c.move(1);
				imageCount=c.getInt(0);
			}
		}catch(SQLException e)
		{
			e.printStackTrace();
		}finally
		{
			if(c!=null)
			{
				c.close();
				c=null;
			}
		}
		
		return imageCount;
	}
}