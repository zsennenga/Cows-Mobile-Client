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
	String tgc = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_creation);
		tgc = getIntent().getStringExtra("TGC");
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
		String response = doEvent(tgc);
		
		if (response == null)	{
			//State http/script error
			return;
		}
		
		String[] pieces = response.split(":", 1);
		
		if (!pieces[0].equals("0"))	{
			switch(Integer.parseInt(pieces[0]))	{
			case -1:
				break;
			case -2:
				Intent i = new Intent(v.getContext(), CasAuth.class);
				i.putExtra("retryingAuth",true);
				startActivity(i);
				//Wait for activity to finish, set this.tgc to Utility.tgc
				this.tgc = Utility.ticket;
				submitHandler(v);
				return;
			case -3:
				//try again, event wrong
				return;
			case -4:
				//Try again
				return;
			default:
				//Try again
				return;
			}
		}
		else	{
			Intent i = new Intent(v.getContext(), DoneOrMore.class);
			i.putExtra("TGC", tgc);	
			startActivity(i);
			finish();
		}
	}

	private String doEvent(String tgc) {
		String getString = "?ticket=" + tgc;
		HttpResponse out = null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://128.120.151.3/development/CowsMobileProxy.php" + getString);
		
		try {
			out = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return out.toString();
	}

}
