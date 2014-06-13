package com.cqu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME="EasyAlbum.db";
	private static final int DATTABASE_VERSION=1;
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATTABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sqlAlbum="create table Album(id integer primary key autoincrement, name varchar(50) not null)";
		String sqlImageItem="create table ImageItem(id integer primary key autoincrement, albumid integer not null, path varchar(200) not null, foreign key(albumid) references Album(id))";
		
		db.execSQL(sqlAlbum);
		db.execSQL(sqlImageItem);
		
		initData(db);
	}
	
	private void initData(SQLiteDatabase db)
	{
		db.beginTransaction();
		try
		{
			for(int i=0;i<200;i++)
			{
				ContentValues cv=new ContentValues();
				cv.put("name", "相册"+(i+1));
				db.insert("Album", null, cv);
			}
			db.setTransactionSuccessful();
		}finally
		{
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
