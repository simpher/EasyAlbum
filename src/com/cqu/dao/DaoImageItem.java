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
			String sql="select id, name, dir from ImageItem where albumid="+parent.getId()+" limit "+pageModel.countPerBatch()+" offset "+ offset;
			c=db.rawQuery(sql, null);
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
						itemsOfAPage[i]=new ImageItem(c.getInt(0), c.getString(1), parent.getId(), c.getString(2));
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
							itemsOfLastPage[i]=new ImageItem(c.getInt(0), c.getString(1), parent.getId(), c.getString(2));
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
			String sql="select id, name, dir from ImageItem where albumid="+parent.getId()+" and name like '%"+nameFragment+"%' limit "+pageModel.countPerBatch()+" offset "+ offset;
			c=db.rawQuery(sql, null);
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
						itemsOfAPage[i]=new ImageItem(c.getInt(0), c.getString(1), parent.getId(), c.getString(2));
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
							itemsOfLastPage[i]=new ImageItem(c.getInt(0), c.getString(1), parent.getId(), c.getString(2));
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
	
	public boolean existsByName(SQLiteDatabase db, ImageItem imageItem, DataItem parent) {
		Cursor c=null;
		try{
			String sql="select id from ImageItem where albumid="+parent.getId()+" and dir='"+imageItem.getDir()+"' and name='"+imageItem.getName()+"'";
			c=db.rawQuery(sql, null);
			if(c!=null&&c.getCount()>0)
			{
				c.move(1);
				return true;
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
		
		return false;
	}

	@Override
	public int addItem(DBManager dbManager, DataItem itemToAdd) {
		// TODO Auto-generated method stub
		ImageItem imageItem=(ImageItem) itemToAdd;
		SQLiteDatabase db=dbManager.getDB();
		if(this.existsByName(db, imageItem, new DataItem(imageItem.getAlbumId(), ""))==true)
		{
			return 0;
		}
		try{
			ContentValues cv=new ContentValues();
			cv.put("albumid", imageItem.getAlbumId());
			cv.put("dir", imageItem.getDir());
			cv.put("name", imageItem.getName());
			db.insert("ImageItem", null, cv);
			return 1;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	@Override
	public int addItems(DBManager dbManager, DataItem[] itemsToAdd) {
		// TODO Auto-generated method stub
		int successCount=0;
		SQLiteDatabase db=dbManager.getDB();
		db.beginTransaction();
		try{
			ImageItem imageItem=null;
			for(DataItem item : itemsToAdd)
			{
				imageItem=(ImageItem) item;
				if(this.existsByName(db, imageItem, new DataItem(imageItem.getAlbumId(), ""))==false)
				{
					ContentValues cv=new ContentValues();
					cv.put("albumid", imageItem.getAlbumId());
					cv.put("dir", imageItem.getDir());
					cv.put("name", imageItem.getName());
					db.insert("ImageItem", null, cv);
					
					successCount++;
				}
			}
			db.setTransactionSuccessful();
			return successCount;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}finally
		{
			db.endTransaction();
		}
	}

	@Override
	public int updateItem(DBManager dbManager, DataItem itemNew) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dbManager.getDB();
		ImageItem imageItem=(ImageItem) itemNew;
		try{
			if(this.existsByName(db, imageItem, new DataItem(imageItem.getAlbumId(), ""))==true)
			{
				return 0;
			}
			ContentValues cv=new ContentValues();
			cv.put("name", imageItem.getName());
			db.update("ImageItem", cv, "id=?", new String[]{imageItem.getId()+""});
			return 1;
		}catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public boolean deleteItem(DBManager dbManager, DataItem item, boolean isParentEmpty) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dbManager.getDB();
		try
		{
			DaoGeneral.deleteTableRecord(db, "ImageItem", item.getId());
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
	public boolean deleteItems(DBManager dbManager, DataItem[] items) {
		// TODO Auto-generated method stub
		SQLiteDatabase db=dbManager.getDB();
		db.beginTransaction();
		try
		{
			for(DataItem item : items)
			{
				DaoGeneral.deleteTableRecord(db, "ImageItem", item.getId());
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
