package com.cqu.easyalbum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

import com.cqu.bean.DataItem;
import com.cqu.dao.DaoImageItem;

public class ActivityImageItem extends SimpleItemListView{
	
	public static final String KEY_IMAGEITEM="KEY_IMAGEITEM";
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		dao=new DaoImageItem();
		parent=(DataItem) getIntent().getSerializableExtra(ActivityAlbum.KEY_ALBUM);
	}

	@Override
	protected void addItem() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void itemSelected(DataItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void deleteItem(final DataItem item) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("消息").setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage("确认删除图片[+"+item.getName()+"]吗？");
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(dao.deleteItem(dbManager, item.getId(), true)==true)
				{
					itemDeletedReset();
				}
			}
		});
		builder.show();
	}

	@Override
	protected void editItem(DataItem item) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(this, ActivityEditImageItem.class);
		intent.putExtra(ActivityImageItem.KEY_IMAGEITEM, item);
		startActivity(intent);
	}
}