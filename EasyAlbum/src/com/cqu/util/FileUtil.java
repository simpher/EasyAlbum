package com.cqu.util;

public class FileUtil {
	
	public static String fileName(String filePath)
	{
		return filePath.substring(filePath.lastIndexOf('/')+1);
	}
}
