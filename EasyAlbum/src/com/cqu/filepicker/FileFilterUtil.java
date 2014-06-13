package com.cqu.filepicker;

import java.io.File;
import java.io.FileFilter;

public class FileFilterUtil {
	
	public static final String DIRECTORY="*";
	public static final String ALLFILE=".*";
	public static final String[] IMAGE_GENERAL=new String[]{".jpg", ".png", ".gif"};
	
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
				if(filterExts[0].equals(DIRECTORY))
				{
					return false;
				}else if(filterExts[0].equals(ALLFILE))
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
