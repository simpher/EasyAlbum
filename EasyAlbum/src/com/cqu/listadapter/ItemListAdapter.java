package com.cqu.listadapter;

import com.cqu.bean.DataItem;
import com.cqu.easyalbum.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemListAdapter extends BaseAdapter{
	
private LayoutInflater mInflater;
	
	private OperationListener opListener;
	
	private DataItem[] dataItems;
	private String headOperation="";
	
	public ItemListAdapter(Context context, DataItem[] dataItems, String headOperation, OperationListener opListener) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		this.dataItems=dataItems;
		this.headOperation=headOperation;
		this.opListener=opListener;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataItems.length+1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position==0)
		{
			return null;
		}else
		{
			return dataItems[position-1];
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.layout_listadapter_item, null);
			
			holder = new ViewHolder();
			holder.tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
			holder.ivDeleteItem=(ImageView) convertView.findViewById(R.id.ivDeleteItem);
			holder.ivEditItem=(ImageView) convertView.findViewById(R.id.ivEditItem);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(position==0)
		{
			holder.tvItemName.setText(headOperation);
			holder.ivDeleteItem.setVisibility(View.INVISIBLE);
			holder.ivEditItem.setVisibility(View.INVISIBLE);
		}else
		{
			holder.ivDeleteItem.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(opListener!=null)
					{
						opListener.onDeleteItem(dataItems[position-1]);
					}
				}
			});
			
			holder.ivEditItem.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(opListener!=null)
					{
						opListener.onEditItem(dataItems[position-1]);
					}
				}
			});
			
			holder.tvItemName.setText(dataItems[position-1].getName());
			holder.ivDeleteItem.setVisibility(View.VISIBLE);
			holder.ivEditItem.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	final class ViewHolder {
		public TextView tvItemName;
		public ImageView ivDeleteItem;
		public ImageView ivEditItem;
	}
}
