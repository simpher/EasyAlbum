package com.cqu.bean;

import java.io.Serializable;

public class DataItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3224923121763482699L;
	
	protected int id;
	protected String name;
	
	public DataItem(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public DataItem(DataItem item)
	{
		super();
		this.id=item.id;
		this.name=item.name;
	}
	
	public void setId(int id)
	{
		this.id=id;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
}
