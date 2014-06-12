package com.cqu.filepicker;

import java.util.HashMap;
import java.util.Map;

import com.cqu.easyalbum.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class FileListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	
	private FileItem[] fileItems;
	private Map<Integer, FileItem> filesSelected;
	private boolean multiSelectable=false;
	private FrameLayout lastSelectedItem;
	
	@SuppressLint("UseSparseArrays")
	public FileListAdapter(Context context, FileItem[] fileItems, boolean multiSelectable) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		this.fileItems=fileItems;
		this.multiSelectable=multiSelectable;
		
		filesSelected=new HashMap<Integer, FileItem>();
	}
	
	public FileItem[] selectedItems()
	{
		FileItem[] ret=new FileItem[filesSelected.values().size()];
		return filesSelected.values().toArray(ret);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fileItems.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return fileItems[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_icon_text_list_item, null);
			
			holder = new ViewHolder();
			holder.flBgSelection=(FrameLayout) convertView.findViewById(R.id.flBgSelection);
			holder.ivItemIcon=(ImageView) convertView.findViewById(R.id.ivIcon);
			holder.tvItemName = (TextView) convertView.findViewById(R.id.tvName);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final View converViewFinal=convertView;
		holder.flBgSelection.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("InlinedApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(filesSelected.containsKey(position)==false)
				{
					if(multiSelectable==false&&filesSelected.size()>=1)
					{
						int selected=-1;
						for(int pos : filesSelected.keySet())
						{
							selected=pos;
						}
						filesSelected.remove(selected);
						
						lastSelectedItem.setBackgroundColor(v.getResources().getColor(android.R.color.transparent));
					}
					filesSelected.put(position, fileItems[position]);
					converViewFinal.setBackgroundColor(v.getResources().getColor(android.R.color.holo_blue_light));
					
					lastSelectedItem=holder.flBgSelection;
				}else
				{
					filesSelected.remove(position);
					converViewFinal.setBackgroundColor(v.getResources().getColor(android.R.color.transparent));
				}
			}
		});
		
		holder.tvItemName.setText(fileItems[position].getName());
		if(fileItems[position].getFileType()==FileItem.TYPE_DIRECTORY)
		{
			holder.ivItemIcon.setImageResource(R.drawable.folder);
		}else
		{
			holder.ivItemIcon.setImageResource(R.drawable.file);
		}
		
		return convertView;
	}
	
	final class ViewHolder {
		public FrameLayout flBgSelection;
		public ImageView ivItemIcon;
		public TextView tvItemName;
	}
}
