package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CasAuth extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cas_auth);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cas_auth, menu);
		return true;
	}

}
