package com.cqu.listadapter;

import android.content.Context;

import com.cqu.bean.DataItem;
import com.cqu.easyalbum.R;

public class ItemListAdapterForImageItem extends ItemListAdapter{

	public ItemListAdapterForImageItem(Context context,
			DataItem[] dataItems, String headOperation,
			OperationListener opListener) {
		super(context, dataItems, headOperation, opListener);
		// TODO Auto-generated constructor stub
		this.adapterLayouId=R.layout.layout_listadapter_for_imageitem_list;
	}

}
