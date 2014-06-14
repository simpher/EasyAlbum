package com.cqu.filepicker;

import java.util.ArrayList;
import java.util.List;
import com.cqu.easyalbum.R;
import com.cqu.util.ArrayUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileListAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	
	private List<FileItem> fileItems;
	private boolean[] itemSelected;
	private boolean multiSelectable=false;
	private boolean directoryOnly=false;
	private int lastSelectedItemPosition;
	private int selectedItemCount=0;
	
	@SuppressLint("UseSparseArrays")
	public FileListAdapter(Context context, List<FileItem> fileItems, boolean multiSelectable, boolean directoryOnly) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		this.fileItems=fileItems;
		this.multiSelectable=multiSelectable;
		this.directoryOnly=directoryOnly;
		
		itemSelected=new boolean[fileItems.size()];
		ArrayUtil.initArray(itemSelected, false);
	}
	
	public String[] selectedItems()
	{
		List<String> list=new ArrayList<String>();
		for(int i=0;i<itemSelected.length;i++)
		{
			if(itemSelected[i]==true)
			{
				list.add(fileItems.get(i).getName());
			}
		}
		String[] ret=new String[list.size()];
		return list.toArray(ret);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fileItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return fileItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InlinedApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_icon_text_list_item, null);
			
			holder = new ViewHolder();
			holder.tvBgSelection=(TextView) convertView.findViewById(R.id.tvBgSelection);
			holder.ivItemIcon=(ImageView) convertView.findViewById(R.id.ivIcon);
			holder.tvItemName = (TextView) convertView.findViewById(R.id.tvName);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(directoryOnly==false)
		{
			if(fileItems.get(position).getFileType()==FileItem.TYPE_DIRECTORY)
			{
				holder.tvBgSelection.setVisibility(View.INVISIBLE);
			}else
			{
				holder.tvBgSelection.setVisibility(View.VISIBLE);
				setOnClickListener(holder, position);
			}
		}else
		{
			if(fileItems.get(position).getFileType()==FileItem.TYPE_DIRECTORY)
			{
				holder.tvBgSelection.setVisibility(View.VISIBLE);
                setOnClickListener(holder, position);
			}else
			{
				holder.tvBgSelection.setVisibility(View.INVISIBLE);
			}
		}
		
		
		if(itemSelected[position]==true)
		{
			holder.tvBgSelection.setBackgroundColor(convertView.getResources().getColor(android.R.color.holo_blue_light));
		}else
		{
			holder.tvBgSelection.setBackgroundColor(convertView.getResources().getColor(android.R.color.transparent));
		}
		
		holder.tvItemName.setText(fileItems.get(position).getName());
		if(fileItems.get(position).getFileType()==FileItem.TYPE_DIRECTORY)
		{
			holder.ivItemIcon.setImageResource(R.drawable.folder);
		}else
		{
			holder.ivItemIcon.setImageResource(R.drawable.file);
		}
		
		return convertView;
	}
	
	private void setOnClickListener(final ViewHolder holder, final int position)
	{
		holder.tvBgSelection.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("InlinedApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(itemSelected[position]==false)
				{
					if(multiSelectable==false&&selectedItemCount>=1)
					{
						itemSelected[lastSelectedItemPosition]=false;
						selectedItemCount--;
					}
					itemSelected[position]=true;
					selectedItemCount++;
					
					lastSelectedItemPosition=position;
				}else
				{
					itemSelected[position]=false;
					selectedItemCount--;
				}
				notifyDataSetChanged();
			}
		});
	}
	
	final class ViewHolder {
		public TextView tvBgSelection;
		public ImageView ivItemIcon;
		public TextView tvItemName;
	}
}
