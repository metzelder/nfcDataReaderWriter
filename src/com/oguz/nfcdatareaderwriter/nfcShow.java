package com.oguz.nfcdatareaderwriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class nfcShow extends Activity{

	private TextView TextViewData;
	private String data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfcshow);
		TextViewData= (TextView) findViewById(R.id.data);
		
		Intent intent=getIntent();
		String data=intent.getStringExtra("data");
		TextViewData.setText("Data on the tag: "+data);
		
	}
	

}
