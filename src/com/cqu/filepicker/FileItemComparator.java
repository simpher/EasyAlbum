package com.cqu.filepicker;

import java.util.Comparator;

public class FileItemComparator implements Comparator<FileItem>{

	@Override
	public int compare(FileItem lhs, FileItem rhs) {
		// TODO Auto-generated method stub
		return lhs.getName().compareTo(rhs.getName());
	}

}
