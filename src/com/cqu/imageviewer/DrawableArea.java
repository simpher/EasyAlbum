package com.cqu.imageviewer;

public class DrawableArea {
	
	private int width;
	private int height;
	
	public DrawableArea(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public double withHeightRatio()
	{
		return 1.0*this.width/this.height;
	}
	
	/**
	 * 将with=objectWidth, height=objectHeight的对象放入
	 * 此区域，并限制在区域内居中，可能会缩小对象
	 * @param objectWidth
	 * @param objectHeight
	 * @return {leftAxis, topAxis, rightAxis, bottomAxis}
	 */
	public int[] centerLimitInside(int objectWidth, int objectHeight)
	{
		int[] ret=new int[4];
		if(objectWidth<=this.width&&objectHeight<=this.height)
		{
			ret[0]=(this.width-objectWidth)/2;
			ret[1]=(this.height-objectHeight)/2;
			ret[2]=ret[0]+objectWidth;
			ret[3]=ret[1]+objectHeight;
			return ret;
		}
		
		double areaRatio=this.withHeightRatio();
		double objectRatio=1.0*objectWidth/objectHeight;
		if(objectRatio>areaRatio)
		{
			int scaledHeight=(int)(this.width/objectRatio);
			ret[0]=0;
			ret[1]=(this.height-scaledHeight)/2;
			ret[2]=this.width;
			ret[3]=ret[1]+scaledHeight;
		}else
		{
			int scaledWidth=(int)(this.height*objectRatio);
			ret[0]=(this.width-scaledWidth)/2;
			ret[1]=0;
			ret[2]=ret[0]+scaledWidth;
			ret[3]=this.height;
		}
		return ret;
	}
	
	/**
	 * 将with=objectWidth, height=objectHeight的对象放入
	 * 此区域，并居中,保持对象原始大小
	 * @param objectWidth
	 * @param objectHeight
	 * @return {leftAxis, topAxis, rightAxis, bottomAxis}
	 */
	public int[] centerNoLimit(int objectWidth, int objectHeight)
	{
		int[] ret=new int[4];
		ret[0]=(this.width-objectWidth)/2;
		ret[1]=(this.height-objectHeight)/2;
		ret[2]=ret[0]+objectWidth;
		ret[3]=ret[1]+objectHeight;
		return ret;
	}
}
