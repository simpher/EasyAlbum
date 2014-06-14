package com.cqu.dao;

import com.cqu.bean.DataItem;
import com.cqu.db.DBManager;
import com.cqu.db.DataModel;

public interface GeneralDaoInterface {
	DataModel getItems(DBManager dbManager, DataModel.PageModel pageModel, int pageIndex, DataItem parent);
	DataModel searchItem(DBManager dbManager, DataModel.PageModel pageModel, int pageIndex, String nameFragment, DataItem parent);
	
	/**
	 * 
	 * @param dbManager
	 * @param itemToAdd
	 * @return -1表示失败，其它表示添加成功的数量
	 */
	int addItem(DBManager dbManager, DataItem itemToAdd);
	/**
	 * 
	 * @param dbManager
	 * @param itemsToAdd
	 * @return -1表示失败，其它表示添加成功的数量
	 */
	int addItems(DBManager dbManager, DataItem[] itemsToAdd);
	boolean updateItem(DBManager dbManager, DataItem itemNew);
	boolean deleteItem(DBManager dbManager, DataItem item, boolean isParentEmpty);
	boolean deleteItems(DBManager dbManager, DataItem[] items);
}
