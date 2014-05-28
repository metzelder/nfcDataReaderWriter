package com.oguz.nfcdatareaderwriter;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainPage extends Activity {

	private Button writeNFCButton;
	private TextView openingMessage;


	private AdView adView;
	  /* Your ad unit id. Replace with your actual ad unit id. */
	private static final String AD_UNIT_ID = "ca-app-pub-0095960687620150/2626462451";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main_page);

		  // Create an ad.
	    adView = new AdView(this);
	    adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(AD_UNIT_ID);

	    // Add the AdView to the view hierarchy. The view will have no size
	    // until the ad is loaded.
	    RelativeLayout layout = (RelativeLayout) findViewById(R.id.relative);
	    layout.addView(adView);

	    // Create an ad request. Check logcat output for the hashed device ID to
	    // get test ads on a physical device.
	    AdRequest adRequest = new AdRequest.Builder()
	        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	        .addTestDevice("4df1dccd39006f0f")
	        .build();

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);

		writeNFCButton = (Button) findViewById(R.id.writeNFC);
		openingMessage = (TextView) findViewById(R.id.openingMessage);

		checkNFC();

		writeNFCButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getApplicationContext(), nfcWrite.class);
				startActivity(myIntent);
			}
		});
		handleIntent(getIntent());
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public void handleIntent(Intent intent) {

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefRecord myRecord = ((NdefMessage) rawMsgs[0]).getRecords()[0];
			String nfcData = new String(myRecord.getPayload());
			Intent passIntent = new Intent(getApplicationContext(), nfcShow.class);
			passIntent.putExtra("data", nfcData);
			startActivity(passIntent);

		}

	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public void checkNFC() {
		NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
		NfcAdapter adapter = manager.getDefaultAdapter();
		if (adapter != null && adapter.isEnabled()) {
			openingMessage.setText("Please tap your card to phone to see what it stores...");
		}
		else {
			openingMessage.setText("Please enable NFC manually and come back again!");
			writeNFCButton.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		handleIntent(intent);
	}

}
