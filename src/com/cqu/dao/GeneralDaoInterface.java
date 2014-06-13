package com.cqu.dao;

import com.cqu.bean.DataItem;
import com.cqu.db.DBManager;
import com.cqu.db.DataModel;

public interface GeneralDaoInterface {
	DataModel getItems(DBManager dbManager, DataModel.PageModel pageModel, int pageIndex, DataItem parent);
	DataModel searchItem(DBManager dbManager, DataModel.PageModel pageModel, int pageIndex, String nameFragment, DataItem parent);
	int exists(DBManager dbManager, String name, DataItem parent);
	
	boolean addItem(DBManager dbManager, DataItem itemToAdd);
	boolean addItems(DBManager dbManager, DataItem[] itemsToAdd);
	boolean updateItem(DBManager dbManager, DataItem itemNew);
	boolean deleteItem(DBManager dbManager, int id, boolean isEmpty);
	boolean deleteItems(DBManager dbManager, int[] ids);
}
