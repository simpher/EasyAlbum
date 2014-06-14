package com.cqu.filepicker;

import java.io.File;
import java.io.FileFilter;

public class FileFilterUtil {
	
	public static final String FLAG_DIRECTORY_ONLY="*";
	private static final String FLAG_ALL_FILE=".*";
	
	public static final String[] FILTER_ALL_FILE=new String[]{FLAG_ALL_FILE};
	public static final String[] FILTER_DIRECTORY_ONLY=new String[]{FLAG_DIRECTORY_ONLY};
	public static final String[] FILTER_GENERAL_IMAGE=new String[]{".jpg", ".png", ".gif"};
	
	public static FileFilter getFileFilter(final String[] filterExts)
	{
		FileFilter filter=new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if(pathname.isDirectory())
				{
					return true;
				}
				if(filterExts[0].equals(FLAG_DIRECTORY_ONLY))
				{
					return false;
				}else if(filterExts[0].equals(FLAG_ALL_FILE))
				{
					return true;
				}
				String name=pathname.getName();
				for(String ext : filterExts)
				{
					if(name.endsWith(ext)==true)
					{
						return true;
					}
				}
				return false;
			}
		};
		return filter;
	}
}
