package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class Recurrence extends Activity {
	String recurrenceIn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recurrence);
		this.recurrenceIn = getIntent().getStringExtra("recurrenceIn");
		if (!this.recurrenceIn.equals(""))	{
			String[] pieces = this.recurrenceIn.split("&");
			//TODO set each existing form based on existing recurrence information
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recurrence, menu);
		return true;
	}
	
	public void saveHandler(View v)	{
		String recurrenceOut = "";
		//TODO: setup reccurrenceOut
		Intent i = new Intent();
		i.putExtra("recurrenceOut", recurrenceOut);
		this.setResult(RESULT_OK,i);
		finish();
	}
	
	public void backHandler(View v)	{
		Intent i = new Intent();
		i.putExtra("recurrenceOut", this.recurrenceIn);
		this.setResult(RESULT_OK,i);
		finish();
	}

}
