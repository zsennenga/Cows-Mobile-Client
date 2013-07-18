package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class Recurrence extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recurrence);
		
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
		this.setResult(RESULT_OK,i);
		finish();
	}

}
