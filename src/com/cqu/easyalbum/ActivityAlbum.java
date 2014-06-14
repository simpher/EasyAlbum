package com.cqu.easyalbum;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.cqu.bean.DataItem;
import com.cqu.dao.DaoAlbum;
import com.cqu.dialog.DialogAddSimpleItem;
import com.cqu.dialog.DialogListener;
import com.cqu.listadapter.ItemListAdapter;
import com.cqu.listadapter.OperationListener;

public class ActivityAlbum extends SimpleItemListView {

	public final static String KEY_ALBUM="KEY_ALBUM";
	
	private static final int HANDLER_WHAT_DELETE_ALBUM=1001;
	private Dialog dialgTaskRunning;
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		dao=new DaoAlbum();
		parent=null;
		headOperation="添加相册";
	}

	@Override
	protected void addItem() {
		// TODO Auto-generated method stub
		DialogAddSimpleItem dialogAddAlbum=new DialogAddSimpleItem(this, new DialogListener() {
			
			@Override
			public boolean onOk(Intent data) {
				// TODO Auto-generated method stub
				String name=data.getStringExtra("name").trim();
				DaoAlbum dao=new DaoAlbum();
				int successCount=dao.addItem(dbManager, new DataItem(-1, name));
				if(successCount==0)
				{
					Toast.makeText(ActivityAlbum.this, "相册["+name+"]已存在", Toast.LENGTH_SHORT).show();
					return false;
				}else
				{
					itemAddedReset();
					return true;
				}
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		}, "相册名");
		dialogAddAlbum.setTitle("添加相册");
		dialogAddAlbum.show();
	}

	@Override
	protected void itemSelected(int index, DataItem item) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(this, ActivityImageItem.class);
		intent.putExtra(ActivityAlbum.KEY_ALBUM, item);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		dbManager.close();
	}

	@Override
	public void onDeleteItem(final DataItem item) {
		// TODO Auto-generated method stub
        final int imageCount=((DaoAlbum)dao).queryImageCount(dbManager, item.getId());
		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("消息").setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage("相册["+item.getName()+"]已包含"+imageCount+"张图片，确认删除吗？");
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialgTaskRunning=new ProgressDialog(ActivityAlbum.this);
				dialgTaskRunning.setTitle("删除相册中...");
				dialgTaskRunning.setCancelable(false);
				dialgTaskRunning.setCanceledOnTouchOutside(false);
				dialgTaskRunning.show();
				
				startDeleteTask(item, imageCount>0);
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}
	
	private void startDeleteTask(final DataItem item, final boolean isParentEmpty)
	{
		GeneralTaskThread task=new GeneralTaskThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean ret=dao.deleteItem(dbManager, new DataItem(item.getId(),""), isParentEmpty);
				
				Message msg=new Message();
				msg.what=HANDLER_WHAT_DELETE_ALBUM;
				Bundle data=new Bundle();
				data.putBoolean("success", ret);
				data.putSerializable("item", item);
				msg.setData(data);
				
				handlerAlbumTask.sendMessage(msg);
			}
		});
		task.start();
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handlerAlbumTask=new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what==HANDLER_WHAT_DELETE_ALBUM)
			{
				Bundle data=msg.getData();
				boolean success=data.getBoolean("success");
				DataItem item=(DataItem) data.getSerializable("item");
				if(success==true)
				{
					Toast.makeText(ActivityAlbum.this, "删除相册["+item.getName()+"]成功", Toast.LENGTH_SHORT).show();
					
					itemDeletedReset();
				}else
				{
					Toast.makeText(ActivityAlbum.this, "删除相册["+item.getName()+"]出错", Toast.LENGTH_LONG).show();
				}
				if(dialgTaskRunning!=null)
				{
					dialgTaskRunning.dismiss();
					dialgTaskRunning=null;
				}
			}
		};
	};

	@Override
	public void onEditItem(final DataItem item) {
		// TODO Auto-generated method stub
            DialogAddSimpleItem dialogAddAlbum=new DialogAddSimpleItem(this, new DialogListener() {
			
			@Override
			public boolean onOk(Intent data) {
				// TODO Auto-generated method stub
				String name=data.getStringExtra("name").trim();
				if(name.equals(item.getName()))
				{
					return true;
				}
				DaoAlbum dao=new DaoAlbum();
				int successCount=dao.updateItem(dbManager, new DataItem(item.getId(), name));
				if(successCount==1)
				{
					item.setName(name);//更新本地存储(缓存)
					((BaseAdapter)itemListAdapter).notifyDataSetChanged();//更新显示
					Toast.makeText(ActivityAlbum.this, "更新成功", Toast.LENGTH_SHORT).show();
					return true;
				}else if(successCount==0)
				{
					Toast.makeText(ActivityAlbum.this, "相册["+name+"]已存在", Toast.LENGTH_SHORT).show();
					return false;
				}else
				{
					Toast.makeText(ActivityAlbum.this, "更新失败", Toast.LENGTH_SHORT).show();
					return true;
				}
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		}, "相册名");
		dialogAddAlbum.setTitle("修改相册名");
		dialogAddAlbum.show();
	}

	@Override
	protected BaseAdapter getListAdapter(Context context, DataItem[] dataItems,
			String headOperation, OperationListener opListener) {
		// TODO Auto-generated method stub
		return new ItemListAdapter(context, dataItems, headOperation, opListener);
	}
}
