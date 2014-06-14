package com.cqu.util;

public class FileUtil {
	
	/**
	 * 
	 * @param filePath
	 * @return {dir, name}
	 */
	public static String[] fileDirName(String filePath)
	{
		int index=filePath.lastIndexOf('/');
		return new String[]{filePath.substring(0, index), filePath.substring(index+1)};
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
