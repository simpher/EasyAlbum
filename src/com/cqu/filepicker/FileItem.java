package com.cqu.filepicker;

import java.io.Serializable;

public class FileItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6203276901376885111L;
	
	public static final int TYPE_DIRECTORY=0;
	public static final int TYPE_FILE=1;
	public static final int TYPE_IMAGE=2;
	public static final int TYPE_AUDIO=3;
	public static final int TYPE_VIDEO=4;
	
	private int fileType;
	private String name;
	
	public FileItem(int fileType, String name) {
		super();
		this.fileType = fileType;
		this.name = name;
	}
	
	public int getFileType() {
		return fileType;
	}
	
	public String getName() {
		return name;
	}
}
