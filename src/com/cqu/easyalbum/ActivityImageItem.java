package com.cqu.easyalbum;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.cqu.bean.DataItem;
import com.cqu.bean.ImageItem;
import com.cqu.dao.DaoAlbum;
import com.cqu.dao.DaoImageItem;
import com.cqu.dialog.DialogChooseAlbum;
import com.cqu.dialog.DialogListener;
import com.cqu.filepicker.FileFilterUtil;
import com.cqu.filepicker.FilePicker;
import com.cqu.imageviewer.SimpleImageViewer;
import com.cqu.listadapter.ItemListAdapterForImageItem;
import com.cqu.listadapter.OperationListener;

public class ActivityImageItem extends SimpleItemListView{
	
	public static final String KEY_IMAGEITEM="KEY_IMAGEITEM";
	public static final int REQUEST_CODE_ADD_IMAGEITEM=101;
	
	private static final int HANDLER_WHAT_ADD_IMAGE_ITEMS=1001;
	private Dialog dialgTaskRunning;
	
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
		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("添加图片").setMessage("添加图片或目录？").setNegativeButton("取消", null);
		builder.setNeutralButton("目录", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(ActivityImageItem.this, FilePicker.class);
				intent.putExtra(FilePicker.KEY_FILE_FILTER, FileFilterUtil.FILTER_DIRECTORY_ONLY);
				intent.putExtra(FilePicker.KEY_MULTISELECTABLE, false);
				startActivityForResult(intent, REQUEST_CODE_ADD_IMAGEITEM);
			}
		});
		builder.setPositiveButton("图片", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setClass(ActivityImageItem.this, FilePicker.class);
				intent.putExtra(FilePicker.KEY_FILE_FILTER, FileFilterUtil.FILTER_GENERAL_IMAGE);
				intent.putExtra(FilePicker.KEY_MULTISELECTABLE, true);
				startActivityForResult(intent, REQUEST_CODE_ADD_IMAGEITEM);
			}
		});
		builder.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==REQUEST_CODE_ADD_IMAGEITEM)
		{
			if(resultCode==Activity.RESULT_OK)
			{
				boolean directoryOnly=data.getBooleanExtra("directoryOnly", false);
				String dir=data.getStringExtra("dir");
				String[] nameItemsSelected=(String[]) data.getStringArrayExtra("selected");
				final DataItem[] itemsToAdd;
				if(directoryOnly==true)
				{
					String imageDir=dir+"/"+nameItemsSelected[0];
					File f=new File(imageDir);
					File[] files=f.listFiles(FileFilterUtil.getFileFilter(FileFilterUtil.FILTER_GENERAL_IMAGE, false));
					if(files!=null&&files.length>0)
					{
						itemsToAdd=new DataItem[files.length];
						for(int i=0;i<itemsToAdd.length;i++)
						{
							itemsToAdd[i]=new ImageItem(-1, files[i].getName(), parent.getId(), imageDir);
						}
					}else
					{
						itemsToAdd=null;
						Toast.makeText(ActivityImageItem.this, "此目录中无图片文件", Toast.LENGTH_LONG).show();
						return;
					}
				}else//表示添加的是图片
				{
					itemsToAdd=new DataItem[nameItemsSelected.length];
					for(int i=0;i<itemsToAdd.length;i++)
					{
						itemsToAdd[i]=new ImageItem(-1, nameItemsSelected[i], parent.getId(), dir);
					}
				}
				
				//此处开启线程处理批量插入工作，并显示进度框
				dialgTaskRunning=new ProgressDialog(this);
				dialgTaskRunning.setTitle("添加图片中...");
				dialgTaskRunning.setCancelable(false);
				dialgTaskRunning.setCanceledOnTouchOutside(false);
				dialgTaskRunning.show();
				
				GeneralTaskThread task=new GeneralTaskThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						int successCount=dao.addItems(dbManager, itemsToAdd);
						
						Message msg=new Message();
						msg.what=HANDLER_WHAT_ADD_IMAGE_ITEMS;
						Bundle data=new Bundle();
						data.putInt("successCount", successCount);
						data.putInt("totalCount", itemsToAdd.length);
						msg.setData(data);
						
						handlerImageItemTask.sendMessage(msg);
					}
				});
				task.start();
			}
		}
		//super.onActivityResult(requestCode, resultCode, data);
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handlerImageItemTask=new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what==HANDLER_WHAT_ADD_IMAGE_ITEMS)
			{
				Bundle data=msg.getData();
				int successCount=data.getInt("successCount");
				int totalCount=data.getInt("totalCount");
				if(successCount>0)
				{
					Toast.makeText(ActivityImageItem.this, "新添加图片["+successCount+"/"+totalCount+"]", Toast.LENGTH_SHORT).show();
					itemAddedReset();
				}else
				{
					Toast.makeText(ActivityImageItem.this, "添加出错", Toast.LENGTH_SHORT).show();
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
	protected void itemSelected(int index, DataItem item) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(this, SimpleImageViewer.class);
		intent.putExtra("album", parent);
		intent.putExtra("pageModel", pageModel);
		intent.putExtra("curItemNumber", (getCurPageNumber()-1)*pageModel.countPerPage+index+1);
		intent.putExtra("curDataViewMode", this.getCurDataViewMode());
		if(this.getCurDataViewMode()==DataViewMode.MODE_SEARCH_DATA)
		{
			intent.putExtra("searchString", this.getSearchString());
		}
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
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	@Override
	public void onEditItem(final DataItem item) {
		// TODO Auto-generated method stub
		DialogChooseAlbum dialogChooseAlbum=new DialogChooseAlbum(this, BufferAlbumListChoosable.getAvailAlbums(this), "相册名", new DialogListener() {
			
			@Override
			public boolean onOk(Intent data) {
				// TODO Auto-generated method stub
				String name=data.getStringExtra("name").trim();
				String type=data.getStringExtra("type");
				DaoAlbum dao=new DaoAlbum();
				int albumId=dao.existsByName(dbManager, new DataItem(-1, name));
				if(albumId==-1)
				{
					Toast.makeText(ActivityImageItem.this, "相册["+name+"]不存在", Toast.LENGTH_SHORT).show();
					if(type.equals("selected"))
					{
						//清除无效的BufferedAlbumItemList item
						BufferAlbumListChoosable.removeInvalidAlbums(ActivityImageItem.this, name);
					}
					return false;
				}
				DaoImageItem daoImageItem=new DaoImageItem();
				ImageItem imageItem=(ImageItem) item;
				int successCount=daoImageItem.addItem(dbManager, new ImageItem(imageItem.getId(), imageItem.getName(), albumId, imageItem.getDir()));
				if(successCount>=0)
				{
					if(successCount==1)
					{
						BufferAlbumListChoosable.saveAvailAlbums(ActivityImageItem.this, name);
					}
					
					Toast.makeText(ActivityImageItem.this, "添加图片["+successCount+"/1]", Toast.LENGTH_SHORT).show();
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
		});
		dialogChooseAlbum.setTitle("添加至相册");
		dialogChooseAlbum.show();
	}

	@Override
	protected BaseAdapter getListAdapter(Context context, DataItem[] dataItems,
			String headOperation, OperationListener opListener) {
		// TODO Auto-generated method stub
		return new ItemListAdapterForImageItem(context, dataItems, headOperation, opListener);
	}
}
