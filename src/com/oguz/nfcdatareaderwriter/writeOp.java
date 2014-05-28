package com.oguz.nfcdatareaderwriter;

import java.io.IOException;
import java.nio.charset.Charset;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class writeOp extends Activity{
	
	private String dataToBeWritten;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.writeop);
		
		Intent intent=getIntent();
		dataToBeWritten= intent.getStringExtra("data");
	}
	
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		 Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		    String nfcMessage = intent.getStringExtra("nfcMessage");
		 
		    if(nfcMessage != "") {
		        writeTag(this, tag, nfcMessage);
		    }
		    else{
		    	Toast.makeText(getApplicationContext(),"Please fill the data field",Toast.LENGTH_LONG).show();
		    }
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setupIntent();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	public void setupIntent() {
		String nfcMessage = dataToBeWritten;
	
		// When an NFC tag comes into range, call the main activity which
		// handles writing the data to the tag
		NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		Intent nfcIntent = new Intent(this, writeOp.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		nfcIntent.putExtra("nfcMessage", nfcMessage);
		PendingIntent pi = PendingIntent.getActivity(this, 0, nfcIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

		nfcAdapter.enableForegroundDispatch((Activity) this, pi, new IntentFilter[] { tagDetected }, null);
	}
	
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public boolean writeTag(Context context, Tag tag, String data) {
		// Record to launch Play Store if app is not installed
		NdefRecord appRecord = NdefRecord.createApplicationRecord(context.getPackageName());

		// Record with actual data we care about
		NdefRecord relayRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
				new String("application/" + context.getPackageName()).getBytes(Charset.forName("US-ASCII")),
				null, data.getBytes());

		// Complete NDEF message with both records
		NdefMessage message = new NdefMessage(new NdefRecord[] { relayRecord, appRecord });

		try {
			// If the tag is already formatted, just write the message to it
			Ndef ndef = Ndef.get(tag);
			if (ndef != null) {
				ndef.connect();

				// Make sure the tag is writable
				if (!ndef.isWritable()) {
					Toast.makeText(getApplicationContext(), "The tag couldnt be written...", Toast.LENGTH_SHORT).show();
					return false;
				}

				// Check if there's enough space on the tag for the message
				int size = message.toByteArray().length;
				if (ndef.getMaxSize() < size) {
					Toast.makeText(getApplicationContext(), "No space on the tag...", Toast.LENGTH_SHORT).show();
					finish();
					return false;
				}

				try {
					// Write the data to the tag
					ndef.writeNdefMessage(message);
					Toast.makeText(getApplicationContext(), "Writing successful", Toast.LENGTH_LONG).show();
					finish();
					return true;
				} catch (TagLostException tle) {
					return false;
				} catch (IOException ioe) {
					return false;
				} catch (FormatException fe) {
					return false;
				}
				// If the tag is not formatted, format it with the message
			} else {
				NdefFormatable format = NdefFormatable.get(tag);
				if (format != null) {
					try {
						format.connect();
						format.format(message);

						return true;
					} catch (TagLostException tle) {
						return false;
					} catch (IOException ioe) {

						return false;
					} catch (FormatException fe) {

						return false;
					}
				} else {

					return false;
				}
			}
		} catch (Exception e) {

		}

		return false;
	}

	


}
