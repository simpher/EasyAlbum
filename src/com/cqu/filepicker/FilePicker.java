package com.cqu.filepicker;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.cqu.easyalbum.R;
import com.cqu.util.FileUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FilePicker extends Activity{
	
	public static final String KEY_FILE_FILTER="KEY_FILE_FILTER";
	public static final String KEY_MULTISELECTABLE="KEY_MULTISELECTABLE";
	
	private TextView tvCurPath;
	private TextView tvBackToParent;
	private ListView lvFiles;
	private TextView tvOk;
	private TextView tvCancel;
	
	private String initPath;
	private String curPath;
	private BaseAdapter filesListAdapter;
	
	private FileFilter filter;
	private boolean multiSelectable=false;
	private boolean directoryOnly=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_file_picker);
		
		Intent intent=getIntent();
		if(intent.getSerializableExtra(KEY_FILE_FILTER)!=null)
		{
			String[] filterExts=intent.getStringArrayExtra(KEY_FILE_FILTER);
			if(filterExts[0].equals(FileFilterUtil.FLAG_DIRECTORY_ONLY))
			{
				directoryOnly=true;
			}
			filter=FileFilterUtil.getFileFilter(filterExts, true);
		}
		multiSelectable=intent.getBooleanExtra(KEY_MULTISELECTABLE, false);
		
		this.initPath=Environment.getExternalStorageDirectory().getPath();
		this.curPath=this.initPath;
		
		init();
	}
	
	private void init()
	{
		tvCurPath=(TextView) findViewById(R.id.tvCurPath);
		tvBackToParent=(TextView) findViewById(R.id.tvBackToparent);
		lvFiles=(ListView) findViewById(R.id.listFiles);
		tvOk=(TextView) findViewById(R.id.tvOk);
		tvCancel=(TextView) findViewById(R.id.tvCancel);
		
		tvBackToParent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				backToParent();
			}
		});
		
		tvOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent data=new Intent();
				data.putExtra("directoryOnly", directoryOnly);
				data.putExtra("dir", curPath);
				String[] items=((FileListAdapter)filesListAdapter).selectedItems();
				if(items.length==0)
				{
					Toast.makeText(FilePicker.this, "nothing selected", Toast.LENGTH_SHORT).show();
					return;
				}
				data.putExtra("selected", items);
				setResult(Activity.RESULT_OK, data);
				
				finish();
			}
		});
		
		tvCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(Activity.RESULT_CANCELED);
				finish();
			}
		});
		
		if(directoryOnly==true)
		{
			lvFiles.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent,
						View view, int position, long id) {
					// TODO Auto-generated method stub
					((FileListAdapter)filesListAdapter).setSelected(position);
					return true;
				}
			});
		}

		lvFiles.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				FileItem item=(FileItem) lvFiles.getItemAtPosition(position);
				if(item.getFileType()==FileItem.TYPE_DIRECTORY)
				{
					curPath=curPath+"/"+item.getName();
					tvCurPath.setText(curPath);
					loadFilesList();
				}
			}
		});
		
		tvCurPath.setText(curPath);
		loadFilesList();
	}
	
	private void loadFilesList()
	{
		File f=new File(curPath);
		File[] files=f.listFiles(filter);
		
		if(files!=null&&files.length>0)
		{
			List<FileItem> items=new ArrayList<FileItem>();
			for(int i=0;i<files.length;i++)
			{
				items.add(new FileItem(files[i].isDirectory()?FileItem.TYPE_DIRECTORY:FileItem.TYPE_FILE ,files[i].getName()));
			}
			
			Collections.sort(items, new FileItemComparator());
			
			filesListAdapter=new FileListAdapter(this, items, multiSelectable, directoryOnly);
			this.lvFiles.setAdapter(filesListAdapter);
		}else
		{
			filesListAdapter=new FileListAdapter(this, new ArrayList<FileItem>(), multiSelectable, directoryOnly);
			this.lvFiles.setAdapter(filesListAdapter);
		}
	}
	
	private boolean backToParent()
	{
		if(curPath.equals(initPath)==true)
		{
			return false;
		}else
		{
			curPath=FileUtil.parentPath(curPath);
			tvCurPath.setText(curPath);
			loadFilesList();
			return true;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(backToParent()==true)
			{
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
