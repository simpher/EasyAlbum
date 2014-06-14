package com.cqu.easyalbum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

import com.cqu.bean.DataItem;
import com.cqu.bean.ImageItem;
import com.cqu.dao.DaoImageItem;
import com.cqu.filepicker.FileFilterUtil;
import com.cqu.filepicker.FilePicker;
import com.cqu.imageviewer.SimpleImageViewer;

public class ActivityImageItem extends SimpleItemListView{
	
	public static final String KEY_IMAGEITEM="KEY_IMAGEITEM";
	public static final int REQUEST_CODE_ADD_IMAGEITEM=101;
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		dao=new DaoImageItem();
		parent=(DataItem) getIntent().getSerializableExtra(ActivityAlbum.KEY_ALBUM);
		headOperation="添加图片";
	}

	@Override
	protected void addItem() {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(this, FilePicker.class);
		intent.putExtra(FilePicker.KEY_FILE_FILTER, FileFilterUtil.IMAGE_GENERAL);
		intent.putExtra(FilePicker.KEY_MULTISELECTABLE, true);
		startActivityForResult(intent, REQUEST_CODE_ADD_IMAGEITEM);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE_ADD_IMAGEITEM)
		{
			if(resultCode==Activity.RESULT_OK)
			{
				String dir=data.getStringExtra("dir");
				String[] nameItemsSelected=(String[]) data.getStringArrayExtra("selected");
				String nameItem=nameItemsSelected[0];
				if(dao.addItem(dbManager, new ImageItem(-1, nameItem, parent.getId(), dir))==true)
				{
					itemAddedReset();
				}
			}
		}
		//super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void itemSelected(int index, DataItem item) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(this, SimpleImageViewer.class);
		intent.putExtra("album", parent);
		intent.putExtra("pageModel", pageModel);
		intent.putExtra("curItemNumber", (getCurPageNumber()-1)*pageModel.countPerPage+index+1);
		startActivity(intent);
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
				if(dao.deleteItem(dbManager, new DataItem(item.getId(), ""), true)==true)
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
