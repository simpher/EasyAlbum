package com.cqu.listadapter;

import com.cqu.bean.DataItem;

public interface OperationListener {
	void onDeleteItem(DataItem item);
	void onEditItem(DataItem item);
}
