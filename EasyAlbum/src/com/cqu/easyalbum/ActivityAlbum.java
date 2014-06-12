package com.cqu.easyalbum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.widget.Toast;

import com.cqu.bean.DataItem;
import com.cqu.dao.DaoAlbum;
import com.cqu.dialog.DialogAddSimpleItem;
import com.cqu.dialog.DialogListener;

public class ActivityAlbum extends SimpleItemListView {

	public final static String KEY_ALBUM="KEY_ALBUM";
	
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
				String name=data.getStringExtra("name");
				DaoAlbum dao=new DaoAlbum();
				if(dao.exists(dbManager, name, null)==-1)
				{
					dao.addItem(dbManager, new DataItem(-1, name));
					
					itemAddedReset();
					return true;
				}else
				{
					Toast.makeText(ActivityAlbum.this, "相册["+name+"]已存在", Toast.LENGTH_SHORT).show();
					return false;
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
	protected void itemSelected(DataItem item) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(this, ActivityImageItem.class);
		intent.putExtra(ActivityAlbum.KEY_ALBUM, item);
		startActivity(intent);
	}

	@Override
	protected void deleteItem(final DataItem item) {
		// TODO Auto-generated method stub
		final int imageCount=((DaoAlbum)dao).queryImageCount(dbManager, item.getId());
		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle("消息").setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage("相册["+item.getName()+"]已包含"+imageCount+"张图片，确认删除吗？");
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(dao.deleteItem(dbManager, item.getId(), imageCount>0)==true)
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
		intent.setClass(this, ActivityEditAlbum.class);
		intent.putExtra(ActivityAlbum.KEY_ALBUM, item);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		dbManager.close();
	}
}
