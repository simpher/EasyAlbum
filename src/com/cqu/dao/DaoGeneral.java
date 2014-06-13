package com.cqu.dao;

import android.database.sqlite.SQLiteDatabase;

public class DaoGeneral {
	
	public static void deleteTableRecord(SQLiteDatabase db, String tableName, int id)
	{
		String sql="delete from "+tableName+" where id="+id;
		db.execSQL(sql);
	}
}
