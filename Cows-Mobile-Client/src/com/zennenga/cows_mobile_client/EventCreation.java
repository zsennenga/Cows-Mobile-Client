package com.zennenga.cows_mobile_client;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
		int i = 0;
		while (!Utility.deauth())	{
			Utility.deauth();
			if (i > 10) break;
			i++;
		}
		finish();
	}
	
	public void submitHandler(View v)	{
		String tgc = getIntent().getStringExtra("TGC");
		String getString = "?ticket=" + tgc;
		HttpResponse out = null;
		//TODO: add event information to getString
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://128.120.151.3/development/CowsMobileProxy.php" + getString);
		
		try {
			out = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String[] pieces = out.toString().split(":", 1);
		
		if (!pieces[0].equals("0"))	{
			switch(Integer.parseInt(pieces[0]))	{
				case -1:
					break;
				case -2:
					break;
				case -3:
					break;
				case -4:
					break;
				default:
					break;
			}
		}
		
		Intent i = new Intent(v.getContext(), DoneOrMore.class);
		i.putExtra("TGC", tgc);
		startActivity(i);
	}

}
