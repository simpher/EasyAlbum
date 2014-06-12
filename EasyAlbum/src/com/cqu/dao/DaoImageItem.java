package com.cqu.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cqu.bean.DataItem;
import com.cqu.bean.ImageItem;
import com.cqu.db.DBManager;
import com.cqu.db.DataModel;
import com.cqu.db.DataModel.PageModel;
import com.cqu.util.FileUtil;

public class DaoImageItem implements GeneralDaoInterface{
	
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
			String sql="select count(*) from ImageItem where albumid="+parent.getId();
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
			String sql="select id, path from ImageItem where albumid="+parent.getId()+" limit "+pageModel.countPerBatch()+" offset "+ offset;
			c=db.rawQuery(sql, null);
			String path="";
			if(c!=null&&c.getCount()>0)
			{
				int pageCount=(c.getCount()+pageModel.countPerPage-1)/pageModel.countPerPage;
				int spaceCount=(pageModel.pagePerBatch-(pageCount-1))*pageModel.countPerPage;
				
				itemList=new ArrayList<Object[]>();
				for(int k=0;k<(pageCount-1);k++)
				{
					ImageItem[] itemsOfAPage=new ImageItem[pageModel.countPerPage];
					for(int i=0;i<pageModel.countPerPage;i++)
					{
						c.move(1);
						path=c.getString(1);
						itemsOfAPage[i]=new ImageItem(c.getInt(0), FileUtil.fileName(path), parent.getId(), path);
					}
					itemList.add(itemsOfAPage);
				}
				{
					int remainingCount=c.getCount()-(pageCount-1)*pageModel.countPerPage;
					spaceCount+=(pageModel.countPerPage-remainingCount);
					dataModel.setSpaceCount(spaceCount);
					if(remainingCount>0)
					{
						ImageItem[] itemsOfLastPage=new ImageItem[remainingCount];
						for(int i=0;i<remainingCount;i++)
						{
							c.move(1);
							path=c.getString(1);
							itemsOfLastPage[i]=new ImageItem(c.getInt(0), FileUtil.fileName(path), parent.getId(), path);
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
			String sql="select count(*) from ImageItem where albumid="+parent.getId()+" and name like '%"+nameFragment+"%'";
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
			String sql="select id, path from ImageItem where albumid="+parent.getId()+" and name like '%"+nameFragment+"%' limit "+pageModel.countPerBatch()+" offset "+ offset;
			c=db.rawQuery(sql, null);
			String path="";
			if(c!=null&&c.getCount()>0)
			{
				int pageCount=(c.getCount()+pageModel.countPerPage-1)/pageModel.countPerPage;
				int spaceCount=(pageModel.pagePerBatch-(pageCount-1))*pageModel.countPerPage;
				
				itemList=new ArrayList<Object[]>();
				for(int k=0;k<(pageCount-1);k++)
				{
					ImageItem[] itemsOfAPage=new ImageItem[pageModel.countPerPage];
					for(int i=0;i<pageModel.countPerPage;i++)
					{
						c.move(1);
						path=c.getString(1);
						itemsOfAPage[i]=new ImageItem(c.getInt(0), FileUtil.fileName(path), parent.getId(), path);
					}
					itemList.add(itemsOfAPage);
				}
				{
					int remainingCount=c.getCount()-(pageCount-1)*pageModel.countPerPage;
					spaceCount+=(pageModel.countPerPage-remainingCount);
					dataModel.setSpaceCount(spaceCount);
					if(remainingCount>0)
					{
						ImageItem[] itemsOfLastPage=new ImageItem[remainingCount];
						for(int i=0;i<remainingCount;i++)
						{
							c.move(1);
							path=c.getString(1);
							itemsOfLastPage[i]=new ImageItem(c.getInt(0), FileUtil.fileName(path), parent.getId(), path);
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
			String sql="select id from ImageItem where albumId="+parent.getId()+" and path='"+name+"'";
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
		SQLiteDatabase db=dbManager.getDB();
		ImageItem imageItem=(ImageItem) itemToAdd;
		try{
			ContentValues cv=new ContentValues();
			cv.put("albumid", imageItem.getAlbumId());
			cv.put("path", imageItem.getPath());
			db.insert("DataField", null, cv);
			return true;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean addItems(DBManager dbManager, DataItem[] itemsToAdd) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dbManager.getDB();
		db.beginTransaction();
		try{
			ImageItem imageItem=null;
			for(DataItem item : itemsToAdd)
			{
				imageItem=(ImageItem) item;
				ContentValues cv=new ContentValues();
				cv.put("albumid", imageItem.getAlbumId());
				cv.put("path", imageItem.getPath());
				db.insert("ImageItem", null, cv);
			}
			db.setTransactionSuccessful();
			return true;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}finally
		{
			db.endTransaction();
		}
	}

	@Override
	public boolean updateItem(DBManager dbManager, DataItem itemNew) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dbManager.getDB();
		ImageItem imageItem=(ImageItem) itemNew;
		try{
			ContentValues cv=new ContentValues();
			cv.put("path", imageItem.getPath());
			db.update("ImageItem", cv, "id=?", new String[]{imageItem.getId()+""});
			return true;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteItem(DBManager dbManager, int id, boolean isEmpty) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dbManager.getDB();
		try
		{
			DaoGeneral.deleteTableRecord(db, "ImageItem", id);
			return true;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public void deleteImageItem(SQLiteDatabase db, int imageItemId)
	{
		DaoGeneral.deleteTableRecord(db, "ImageItem", imageItemId);
	}
	
	@Override
	public boolean deleteItems(DBManager dbManager, int[] ids) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dbManager.getDB();
		db.beginTransaction();
		try
		{
			for(int id : ids)
			{
				DaoGeneral.deleteTableRecord(db, "ImageItem", id);
			}
			db.setTransactionSuccessful();
			
			return true;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return false;
		}finally
		{
			db.endTransaction();
		}
	}
}
