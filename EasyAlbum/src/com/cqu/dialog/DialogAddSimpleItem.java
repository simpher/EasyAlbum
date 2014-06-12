package com.cqu.dialog;

import com.cqu.easyalbum.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DialogAddSimpleItem extends Dialog{
	
    private DialogListener listener;
	
	private EditText etName;
	private TextView tvCancel;
	private TextView tvOk;
	
	public DialogAddSimpleItem(Context context, DialogListener listener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.listener=listener;
		
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
	}
	
	public DialogAddSimpleItem(Context context, int theme, DialogListener listener) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.listener=listener;
		
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_add_simple_item);
		
		init();
	}
	
	private void init()
	{
		etName=(EditText) findViewById(R.id.etName);
		tvCancel=(TextView) findViewById(R.id.tvCancel);
		tvOk=(TextView) findViewById(R.id.tvOk);
		
		tvCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listener!=null)
				{
					listener.onCancel();
				}
				DialogAddSimpleItem.this.dismiss();
			}
		});
		
		tvOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name=etName.getText().toString().trim();
				
				if(name.length()>0)
				{
					Intent data=new Intent();
					data.putExtra("name", name);
					if(listener.onOk(data)==true)
					{
						DialogAddSimpleItem.this.dismiss();
					}
				}else
				{
					Toast.makeText(getContext(), "输入为空", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
