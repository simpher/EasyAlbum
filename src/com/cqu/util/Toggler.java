package com.cqu.util;

public class Toggler {
	
	private boolean off=true;
	
	public void toggle()
	{
		off=!off;
	}
	
	public boolean isOff()
	{
		return off==true;
	}
}
