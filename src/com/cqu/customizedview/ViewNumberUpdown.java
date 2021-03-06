package com.cqu.customizedview;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class ViewNumberUpdown {
	
	private TextView tvDecreaseNumber;
	private TextView tvIncreaseNumber;
	private TextView tvNumber;
	
	private int value=0;
	private int valueMin=0;
	private int valueMax=0;
	private int step=1;
	private int largerStep=10;
	
	public ViewNumberUpdown(int value_init, int value_min, int value_max, int step, int largerStep) {
		// TODO Auto-generated constructor stub
		if(value_init>=value_min&&value_init<=value_max)
		{
			this.value=value_init;
		}else
		{
			this.value=value_min;
		}
		this.valueMin=value_min;
		this.valueMax=value_max;
		this.step=step;
	}
	
	public void setViews(TextView tvDecreaseNumber, TextView tvIncreaseNumber, TextView tvNumber)
	{
		this.tvDecreaseNumber=tvDecreaseNumber;
		this.tvIncreaseNumber=tvIncreaseNumber;
		this.tvNumber=tvNumber;
		
		init();
	}
	
	private void init()
	{
		refreshValue();
		
		tvDecreaseNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(value==valueMin)
				{
					return;
				}
				if((value-step)<valueMin)
				{
					value=valueMin;
				}else
				{
					value-=step;
				}
				refreshValue();
			}
		});
		
		tvDecreaseNumber.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(value==valueMin)
				{
					return false;
				}
				if((value-largerStep)<valueMin)
				{
					value=valueMin;
				}else
				{
					value-=largerStep;
				}
				refreshValue();
				return true;
			}
		});
		
		tvIncreaseNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(value==valueMax)
				{
					return;
				}
				if((value+step)>valueMax)
				{
					value=valueMax;
				}else
				{
					value+=step;
				}
				refreshValue();
			}
		});
		
		tvIncreaseNumber.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(value==valueMax)
				{
					return false;
				}
				if((value+largerStep)>valueMax)
				{
					value=valueMax;
				}else
				{
					value+=largerStep;
				}
				refreshValue();
				return true;
			}
		});
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setMin(int valueMin)
	{
		this.valueMin=valueMin;
		if(value<valueMin)
		{
			value=valueMin;
			refreshValue();
		}
	}
	
	public void setMax(int valueMax)
	{
		this.valueMax=valueMax;
		if(value>valueMax)
		{
			value=valueMax;
			refreshValue();
		}
	}
	
	public void setStep(int step)
	{
		this.step=step;
	}
	
	private void refreshValue()
	{
		this.tvNumber.setText(this.value+"");
	}
}
