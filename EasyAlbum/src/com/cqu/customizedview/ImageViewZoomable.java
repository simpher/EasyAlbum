package com.cqu.customizedview;

import com.cqu.imageviewer.DrawableArea;
import com.cqu.util.Toggler;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ImageViewZoomable extends ImageView {

	private static final int EVENT_NONE = 0;
	private static final int EVENT_DRAG = 1;
	private static final int EVENT_ZOOM = 2;
	private static final int EVENT_ZOOM_LARGER = 3;
	private static final int EVENT_ZOOM_SMALLER = 4;

	private int curEvent = EVENT_NONE;
	private float distanceOld = 0.0f;
	private float distanceNew = 0.0f;
	private float zoomScale = 0.04f;

	private Point translateStartPoint=new Point();
	private DrawableArea drawableArea = null;
	private int imageWidth=0;
	private int imageHeight=0;
	private long firstClickTime=0;
	
	private Toggler toggler;

	public ImageViewZoomable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public ImageViewZoomable(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public ImageViewZoomable(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void init(int bmpWidth,int bmpHeight)
	{
		this.imageHeight=bmpHeight;
		this.imageWidth=bmpWidth;
	}

	private float zoomDistance(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(drawableArea==null)
		{
			View parentView=(View) this.getParent();
			drawableArea=new DrawableArea(parentView.getWidth(), parentView.getHeight());
		}
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
		{
			long secondClickTime=System.currentTimeMillis();
			if((secondClickTime-firstClickTime)<=300)
			{
				onDoubleClick();
				firstClickTime=secondClickTime;
				return true;
			}
			firstClickTime=secondClickTime;
			curEvent = EVENT_DRAG;
			translateStartPoint.x = (int) event.getX();
			translateStartPoint.y = (int) event.getY();
			if (event.getPointerCount() == 2)
			{
				distanceOld = zoomDistance(event);
			}
		}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		{
			float dis = zoomDistance(event);
			if (dis > 10f) {
				curEvent = EVENT_ZOOM;
				distanceOld = dis;
			}
		}
			break;
		case MotionEvent.ACTION_MOVE:
		{
			if (curEvent == EVENT_DRAG) {
				int[] top_left_pos=new int[2];
				getLocationOnScreen(top_left_pos);
				Rect rect=new Rect(top_left_pos[0],top_left_pos[1],top_left_pos[0]+getWidth(),top_left_pos[1]+getHeight());
				if(rect.contains((int) event.getRawX(), (int) event.getRawY())==false)
				{
					return true;
				}
				int offsetX = (int)event.getX() - translateStartPoint.x;
				int offsetY = (int)event.getY() - translateStartPoint.y;
				setPosition(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
			} else if (curEvent == EVENT_ZOOM) {
				float dis = zoomDistance(event);
				if (dis > 10f) {
					distanceNew = dis;
					float offset = distanceNew - distanceOld;
					if (offset == 0) {
						break;
					} else if (Math.abs(offset) > 5f) {
						if (offset > 0) {
							setScale(zoomScale, EVENT_ZOOM_LARGER);
						} else {
							setScale(zoomScale, EVENT_ZOOM_SMALLER);
						}
						distanceOld = distanceNew;
					}
				}
			}
		}
			break;
		case MotionEvent.ACTION_POINTER_UP:
			curEvent = EVENT_NONE;
			break;
		case MotionEvent.ACTION_UP:
			curEvent = EVENT_NONE;
			break;
		}
		return true;
	}
	
	private void onDoubleClick()
	{
		scaleToOriginalSize();
	}

	private void setScale(float zoomScale, int eventFlag) {
		int curWidth=getWidth();
		int curHeight=getHeight();
		if(eventFlag == EVENT_ZOOM_LARGER&&(curWidth/imageWidth>2||curHeight/imageWidth>2))
		{
			return;
		}
		if(eventFlag == EVENT_ZOOM_SMALLER&&(imageWidth/curWidth>2||imageHeight/curHeight>2))
		{
			return;
		}
		if (eventFlag == EVENT_ZOOM_LARGER) {
			this.setFrame(this.getLeft() - (int) (zoomScale * this.getWidth()),
					this.getTop() - (int) (zoomScale * this.getHeight()),
					this.getRight() + (int) (zoomScale * this.getWidth()),
					this.getBottom() + (int) (zoomScale * this.getHeight()));
		} else if (eventFlag == EVENT_ZOOM_SMALLER) {
			this.setFrame(this.getLeft() + (int) (zoomScale * this.getWidth()),
					this.getTop() + (int) (zoomScale * this.getHeight()),
					this.getRight() - (int) (zoomScale * this.getWidth()),
					this.getBottom() - (int) (zoomScale * this.getHeight()));
		}
	}
	
	private void scaleToOriginalSize()
	{
		int[] rect=null;
		if(toggler.isOff()==true)
		{
			rect=drawableArea.centerNoLimit(imageWidth, imageHeight);
		}else
		{
			rect=drawableArea.centerLimitInside(imageWidth, imageHeight);
		}
		setFrame(rect[0], rect[1], rect[2], rect[3]);
		toggler.toggle();
		
	}

	private void setPosition(int left, int top, int right, int bottom) {
		int width=right-left;
		int height=bottom-top;
		if(left>0)
		{
			left=0;
			right=left+width;
		}
		if(right>drawableArea.getWidth())
		{
			right=drawableArea.getWidth();
			left=right-width;
		}
		if(top>0)
		{
			top=0;
			bottom=top+height;
		}
		if(bottom>drawableArea.getHeight())
		{
			bottom=drawableArea.getHeight();
			top=bottom-height;
		}
		layout(left, top, right, bottom);
	}
}
