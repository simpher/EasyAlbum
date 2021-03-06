package com.cqu.bean;

public class ImageItem extends DataItem{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6276282867568001176L;
	
	private int albumId;
	private String dir;

	public ImageItem(DataItem item, int albumId, String path) {
		super(item);
		// TODO Auto-generated constructor stub
		this.albumId=albumId;
		this.dir=path;
	}
	
	public ImageItem(int id, String name, int albumId, String dir) {
		super(id, name);
		// TODO Auto-generated constructor stub
		this.albumId=albumId;
		this.dir=dir;
	}
	
	public int getAlbumId()
	{
		return this.albumId;
	}
	
	public String getDir()
	{
		return this.dir;
	}
	
	public void setName(String name)
	{
		this.name=name;
	}
}
