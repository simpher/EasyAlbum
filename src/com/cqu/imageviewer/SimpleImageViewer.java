package com.cqu.imageviewer;

import com.cqu.bean.ImageItem;
import com.cqu.customizedview.ImageViewZoomable;
import com.cqu.easyalbum.ActivityImageItem;
import com.cqu.easyalbum.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class SimpleImageViewer extends Activity{
	
	private ImageItem imageItem;
	private ImageViewZoomable ivImageViewer;
	private Bitmap image;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_simple_image_viewer);
		
		imageItem=(ImageItem) getIntent().getSerializableExtra(ActivityImageItem.KEY_IMAGEITEM);
		
		init();
	}
	
	private void init()
	{
		ivImageViewer=(ImageViewZoomable) findViewById(R.id.ivImageViewer);
		
		image=BitmapFactory.decodeFile(imageItem.getPath());
		ivImageViewer.setImageBitmap(image);
		ivImageViewer.init(image.getWidth(), image.getHeight());
	}
}
