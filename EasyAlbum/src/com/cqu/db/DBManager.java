package com.cqu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBManager{
	
	private DBManager dbManager=null;
	
	private DBHelper dbHelper=null;
	private SQLiteDatabase db=null;
	
	public DBManager(Context context) {
		// TODO Auto-generated constructor stub
		dbHelper=new DBHelper(context);
		db=dbHelper.getWritableDatabase();
	}
	
	public SQLiteDatabase getDB()
	{
		return db;
	}
	
	private void closeDB(){
		if (this.db != null)
		{
			this.db.close();
			this.db=null;
		}
	}
	
	private void closeDBHelper()
	{
		if (this.dbHelper != null)
		{
			this.dbHelper.close();
			this.dbHelper=null;
		}
	}
	
	public void close()
	{
		if(dbManager!=null)
		{
			dbManager.closeDB();
			dbManager.closeDBHelper();
			dbManager=null;
		}
	}
}
