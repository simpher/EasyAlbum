package com.cqu.imageviewer;

import com.cqu.bean.DataItem;
import com.cqu.bean.ImageItem;
import com.cqu.customizedview.ImageViewZoomable;
import com.cqu.dao.DaoImageItem;
import com.cqu.dao.GeneralDaoInterface;
import com.cqu.db.DBManager;
import com.cqu.db.DataModel;
import com.cqu.db.DataModel.PageModel;
import com.cqu.easyalbum.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SimpleImageViewer extends Activity{
	
	private ImageViewZoomable ivImageViewer;
	private TextView tvPrevious;
	private TextView tvNext;
	private TextView tvItemIndicator;
	
	protected DBManager dbManager;
	protected GeneralDaoInterface dao;
	
    private DataModel dataModel=null;
	private DataItem album;
	private PageModel pageModel;
	private int totalPage=0;
	private DataItem[] itemPage=null;
	private int totalItem=0;
	private int curItemNumber;
	
	private Bitmap image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_simple_image_viewer);
		
		album=(DataItem) getIntent().getSerializableExtra("album");
		pageModel=(PageModel) getIntent().getSerializableExtra("pageModel");
		curItemNumber=getIntent().getIntExtra("curItemNumber", 1);
		
		dbManager=new DBManager(this);
		dao=new DaoImageItem();
		
		init();
	}
	
	private void init()
	{
		ivImageViewer=(ImageViewZoomable) findViewById(R.id.ivImageViewer);
		tvPrevious=(TextView) findViewById(R.id.tvPrevious);
		tvNext=(TextView) findViewById(R.id.tvNext);
		tvItemIndicator=(TextView) findViewById(R.id.tvCurrentItemNumber);
		
		tvPrevious.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(curItemNumber>1)
				{
				    loadItem(curItemNumber-1);
				}
			}
		});
		
        tvNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(curItemNumber<totalItem)
				{
					loadItem(curItemNumber+1);
				}
			}
		});
        
        loadItem(curItemNumber);
	}
	
	private void loadItem(int itemNumber)
	{
		ImageItem imageItem=null;
		if(itemPage==null)
		{
			itemPage=loadAllDataPage((itemNumber-1)/this.pageModel.countPerPage+1);
		}
		if(itemPage!=null)
		{
			imageItem=(ImageItem) itemPage[(itemNumber-1)%this.pageModel.countPerPage];
		}
		if(imageItem!=null)
		{
			image=BitmapFactory.decodeFile(imageItem.getDir()+"/"+imageItem.getName());
			ivImageViewer.setImageBitmap(image);
			ivImageViewer.init(image.getWidth(), image.getHeight());
			
			refreshItemNumber(itemNumber, this.totalItem);
		}
	}
	
	private DataItem[] loadAllDataPage(int pageNumber)
	{
		int pageIndex=pageNumber-1;
		DataItem[] itemPage=null;
		
		if(dataModel==null)
		{
			dataModel=dao.getItems(dbManager, pageModel, pageIndex, album);
			if(dataModel!=null)
			{
				itemPage=(DataItem[]) dataModel.getPage(pageIndex);
			}
		}else
		{
			itemPage=(DataItem[]) dataModel.getPage(pageIndex);
			if(itemPage==null)
			{
				dataModel=dao.getItems(dbManager, pageModel, pageIndex, album);
				
				itemPage=(DataItem[]) dataModel.getPage(pageIndex);
			}
		}
		if(itemPage==null)
		{
			refreshPageNumber(0, dataModel.getTotalItemCount());
			return null;
		}else
		{
			refreshPageNumber(dataModel.getTotalPageCount(), dataModel.getTotalItemCount());
			return itemPage;
		}
	}
	
	private void refreshPageNumber(int totalPage_, int totalItem_)
	{
		if(this.totalPage!=totalPage_)
		{
			this.totalPage=totalPage_;
		}
		
		if(this.totalItem!=totalItem_)
		{
			refreshItemNumber(this.curItemNumber, totalItem_);
		}
	}
	
	private void refreshItemNumber(int curItem_, int totalItem_)
	{
		this.curItemNumber=curItem_;
		if(this.totalItem!=totalItem_)
		{
			this.totalItem=totalItem_;
		}
		
		this.tvItemIndicator.setText("第"+curItemNumber+"幅 "+"共"+totalItem+"幅");
	}
}
