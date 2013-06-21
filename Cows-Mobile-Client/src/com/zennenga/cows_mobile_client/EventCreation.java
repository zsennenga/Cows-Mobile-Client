package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class EventCreation extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_creation);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_creation, menu);
		return true;
	}
	
	public void backHandler(View v)	{
		Utility.deauth();
		finish();
	}
	
	public void submitHandler(View v)	{
		finish();
	}

}
