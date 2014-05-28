package com.oguz.nfcdatareaderwriter;

import java.io.IOException;
import java.nio.charset.Charset;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class nfcWrite extends Activity {

	
	private EditText et1;
	private Button button;
	private String myData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfcwrite);
		
		et1=(EditText) findViewById(R.id.et);
		button= (Button) findViewById(R.id.button1);
		
		 button.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					String myData=et1.getText().toString();
					Intent intent=new Intent(getApplicationContext(),writeOp.class);
					intent.putExtra("data",myData);
					startActivity(intent);
				}
			});
		
	}

	
}

