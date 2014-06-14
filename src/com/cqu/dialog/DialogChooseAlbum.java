package com.cqu.dialog;

import com.cqu.easyalbum.R;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class DialogChooseAlbum extends Dialog{
	
	private ViewSwitcher vsChooseAlbum;
	private Spinner spinnerAlbumOptions;
	private TextView tvMoreOperationsA;
	private EditText etName;
	private TextView tvMoreOperationsB;
	
	private TextView tvCancel;
	private TextView tvOk;
	
	private String inputHint;
	private DialogListener listener;
	private String[] bufferedAlbums;

	public DialogChooseAlbum(Context context, String[] bufferedAlbums, String inputHint, DialogListener listener) {
		super(context);
		// TODO Auto-generated constructor stub
		this.bufferedAlbums=bufferedAlbums;
		this.inputHint=inputHint;
		this.listener=listener;
		
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_choose_album);
		
		init();
	}
	
	private void init()
	{
		vsChooseAlbum=(ViewSwitcher) findViewById(R.id.vsChooseAlbum);
		spinnerAlbumOptions=(Spinner) findViewById(R.id.spinnerAlbumOptions);
		tvMoreOperationsA=(TextView) findViewById(R.id.tvMoreOperationsA);
		etName=(EditText) findViewById(R.id.etName);
		tvMoreOperationsB=(TextView) findViewById(R.id.tvMoreOperationsB);
		tvCancel=(TextView) findViewById(R.id.tvCancel);
		tvOk=(TextView) findViewById(R.id.tvOk);
		
		tvMoreOperationsA.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vsChooseAlbum.setDisplayedChild(1);
			}
		});
		
       tvMoreOperationsB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vsChooseAlbum.setDisplayedChild(0);
			}
		});
		
		tvCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listener!=null)
				{
					listener.onCancel();
				}
				DialogChooseAlbum.this.dismiss();
			}
		});
		
		tvOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name="";
				Intent data=new Intent();
				if(vsChooseAlbum.getDisplayedChild()==0)
				{
					name=(String) spinnerAlbumOptions.getSelectedItem();
					data.putExtra("type", "selected");
				}else
				{
					String nameString=etName.getText().toString().trim();
					if(nameString.length()>0)
					{
						name=nameString;
					}else
					{
						Toast.makeText(getContext(), "输入为空", Toast.LENGTH_SHORT).show();
						return;
					}
					data.putExtra("type", "inputted");
				}
				data.putExtra("name", name);
				if(listener.onOk(data)==true)
				{
					DialogChooseAlbum.this.dismiss();
				}
			}
		});
		etName.setHint(inputHint);
		if(bufferedAlbums==null)
		{
			tvMoreOperationsB.setVisibility(View.GONE);
			vsChooseAlbum.setDisplayedChild(1);
		}else
		{
			spinnerAlbumOptions.setAdapter(new ArrayAdapter<String>(this.getContext(), 
					R.layout.simple_spinner_drop_down_item, bufferedAlbums));
		}
		
	}
}
