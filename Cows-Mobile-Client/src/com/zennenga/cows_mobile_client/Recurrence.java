package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class Recurrence extends Activity {
	String recurrenceIn;
	String recurrenceOut = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recurrence);
		this.recurrenceIn = getIntent().getStringExtra("recurrenceIn");
		if (!this.recurrenceIn.equals(""))	{
			String[] pieces = this.recurrenceIn.split("&");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recurrence, menu);
		return true;
	}
	
	public void saveHandler(View v)	{
		Intent i = new Intent();
		i.putExtra("recurrenceOut", this.recurrenceOut);
		this.setResult(RESULT_OK,i);
		finish();
	}
	
	public void backHandler(View v)	{
		
	}

}
