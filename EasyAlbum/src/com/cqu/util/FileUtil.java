package com.cqu.util;

public class FileUtil {
	
	public static String fileName(String filePath)
	{
		return filePath.substring(filePath.lastIndexOf('/')+1);
	}
	
	public static String parentPath(String path)
	{
		int index=path.lastIndexOf('/');
		if(index!=-1&&index!=0)
		{
			return path.substring(0, index);
		}
		return "/";
	}
}
