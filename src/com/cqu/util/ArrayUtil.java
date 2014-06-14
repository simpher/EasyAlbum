package com.cqu.util;

public class ArrayUtil {
	
	public static void initArray(boolean[] arr, boolean defaultValue)
	{
		for(int i=0;i<arr.length;i++)
		{
			arr[i]=defaultValue;
		}
	}
	
	public static int inArray(String[] arr, String e)
	{
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i].equals(e)==true)
			{
				return i;
			}
		}
		return -1;
	}
}
