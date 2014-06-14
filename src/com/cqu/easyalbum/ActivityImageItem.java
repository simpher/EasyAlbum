package com.cqu.easyalbum;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.cqu.bean.DataItem;
import com.cqu.bean.ImageItem;
import com.cqu.dao.DaoAlbum;
import com.cqu.dao.DaoImageItem;
import com.cqu.dialog.DialogAddSimpleItem;
import com.cqu.dialog.DialogListener;
import com.cqu.filepicker.FileFilterUtil;
import com.cqu.filepicker.FilePicker;
import com.cqu.imageviewer.SimpleImageViewer;
import com.cqu.listadapter.ItemListAdapterForImageItem;
import com.cqu.listadapter.OperationListener;

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
		intent.putExtra(FilePicker.KEY_FILE_FILTER, FileFilterUtil.FILTER_GENERAL_IMAGE);
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
				DataItem[] itemsToAdd=new DataItem[nameItemsSelected.length];
				for(int i=0;i<itemsToAdd.length;i++)
				{
					itemsToAdd[i]=new ImageItem(-1, nameItemsSelected[i], parent.getId(), dir);
				}
				
				int successCount=dao.addItems(dbManager, itemsToAdd);
				Toast.makeText(this, "新添加图片["+successCount+"/"+itemsToAdd.length+"]", Toast.LENGTH_SHORT).show();
				if(successCount>0)
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
	public void onDeleteItem(final DataItem item) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("消息").setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage("确认删除图片["+item.getName()+"]吗？");
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
	public void onEditItem(final DataItem item) {
		// TODO Auto-generated method stub
            DialogAddSimpleItem dialogAddAlbum=new DialogAddSimpleItem(this, new DialogListener() {
			
			@Override
			public boolean onOk(Intent data) {
				// TODO Auto-generated method stub
				String name=data.getStringExtra("name").trim();
				DaoAlbum dao=new DaoAlbum();
				int albumId=dao.existsByName(dbManager, new DataItem(-1, name));
				if(albumId==-1)
				{
					Toast.makeText(ActivityImageItem.this, "相册["+name+"]不存在", Toast.LENGTH_SHORT).show();
					return false;
				}
				DaoImageItem daoImageItem=new DaoImageItem();
				ImageItem imageItem=(ImageItem) item;
				int successCount=daoImageItem.addItem(dbManager, new ImageItem(imageItem.getId(), imageItem.getName(), albumId, imageItem.getDir()));
				if(successCount>=0)
				{
					Toast.makeText(ActivityImageItem.this, "添加至相册["+successCount+"/1]", Toast.LENGTH_SHORT).show();
					return true;
				}else
				{
					Toast.makeText(ActivityImageItem.this, "添加失败", Toast.LENGTH_SHORT).show();
					return true;
				}
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		}, "相册名");
		dialogAddAlbum.setTitle("添加至相册");
		dialogAddAlbum.show();
	}

	@Override
	protected BaseAdapter getListAdapter(Context context, DataItem[] dataItems,
			String headOperation, OperationListener opListener) {
		// TODO Auto-generated method stub
		return new ItemListAdapterForImageItem(context, dataItems, headOperation, opListener);
	}
}
