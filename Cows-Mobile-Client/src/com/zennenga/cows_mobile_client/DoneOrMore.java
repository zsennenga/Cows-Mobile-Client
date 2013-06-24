package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class DoneOrMore extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_done_or_more);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.done_or_more, menu);
		return true;
	}
	
	public void doneHander(View v)	{
		int i = 0;
		while (!Utility.deauth())	{
			Utility.deauth();
			if (i > 10) break;
			i++;
		}
		finish();
	}
	
	public void moreHandler(View v)	{
		Intent i = new Intent(v.getContext(), EventCreation.class);
		i.putExtra("TGC", getIntent().getStringExtra("TGC"));
		startActivity(i);
		finish();
	}
}
