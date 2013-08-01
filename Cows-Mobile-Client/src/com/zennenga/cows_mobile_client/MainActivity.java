package com.zennenga.cows_mobile_client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.webkit.CookieManager;

import com.zennenga.utility.Utility;

/**
 * 
 * MainActivity
 * 
 * Program Entry Point, leads to CasAuth and RoomSelect.
 * 
 * @author its-zach
 * 
 */

public class MainActivity extends Activity {
	/**
	 * Main activity serves as the main menu for the app as a whole.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// TODO: remove the need for this policy
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		Utility.updateContext(this);
	}

	/**
	 * If the app closes, destroy all cookies as a failsafe
	 */
	@Override
	public void onDestroy() {
		CookieManager cm = CookieManager.getInstance();
		cm.removeAllCookie();
		cm.removeSessionCookie();
	}

	public void viewEvents(View v) {
		Intent i = new Intent(v.getContext(), RoomSelect.class);
		startActivity(i);
	}

	public void doEvent(View v) {
		Intent i = new Intent(v.getContext(), CasAuth.class);
		startActivity(i);
	}
}
