package com.zennenga.cows_mobile_client;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class EventCreation extends Activity {
	String tgc = "";
	String parameters = "";
	String recurrence = "";
	View temp = null;
	int tries = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_creation);
		tgc = getIntent().getStringExtra("TGC");
		recurrence = "";
		MultiSelectSpinner multiSpinner = ((MultiSelectSpinner) findViewById(R.id.Locations));
		multiSpinner.setItems(new String[] {"Display on Front TV","Homepage Event Listings"});
		multiSpinner.setSelection(0);
		multiSpinner = ((MultiSelectSpinner) findViewById(R.id.Categories));
		multiSpinner.setItems(new String[] {"Classes","Conferences","Meetings","Other","Seminars"});
		multiSpinner.setSelection(0);
		Spinner spinner = (Spinner) findViewById(R.id.eventType);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.EventTypes, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	@Override	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_creation, menu);
		return true;
	}
	/**
	 * Handle cas reauthentication and recurrence configuration
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent d)	{
		if (requestCode == 1)	{
			this.tgc = d.getStringExtra("tgc");
			submitHandler(this.temp);
		}
		else if (requestCode == 2)	{
			this.recurrence = d.getStringExtra("recurrence");
		}
		return;
	}
	/**
	 * Handle the back button
	 * @param v
	 */
	public void backHandler(View v)	{
		int i = 0;
		while (!Utility.deauth())	{
			Utility.deauth();
			if (i > 10) break;
			i++;
		}
		finish();
	}
	/**
	 * Handle the submit button
	 * @param v
	 */
	public void submitHandler(View v)	{
		String response = doEvent(tgc);
		
		if (response == null)	{
			setError("Invalid response from server.");
			return;
		}
		
		String[] pieces = response.split(":", 1);
		
		if (!pieces[0].equals("0"))	{
			Log.e("EventError", pieces[0]);
			switch(Integer.parseInt(pieces[0]))	{
			case -1:
				//Generic error
				setError("Error: " + pieces[1] + " Please retry your submission.");
				return;
			case -2:
				//Failed Auth
				Intent i = new Intent(v.getContext(), CasAuth.class);
				i.putExtra("retryingAuth",true);
				i.putExtra("error", pieces[1]);
				startActivityForResult(i, 1);
				this.temp = v;
				return;
			case -3:
				//Event Error
				setError("Event error: " + pieces[1] + " Please fix this error, and retry your submission.");
				return;
			case -4:
				//cURL error
				this.tries++;
				if (this.tries >= 5)	{
					setError("Network Error: " + pieces[1]);
					return;
				}
				else submitHandler(v);
				return;
			default:
				//Generic Error
				setError("Error: " + pieces[1]);
				return;
			}
		}
		else	{
			setError("");
			this.tries = 0;
			Intent i = new Intent(v.getContext(), DoneOrMore.class);
			i.putExtra("TGC", tgc);	
			startActivity(i);
			finish();
		}
	}
	
	private void setError(String error)	{
		((TextView)findViewById(R.id.error)).setText(error);
		return;
	}
	/**
	 * Setup recurrence
	 * 
	 * @param v
	 */
	public void doRecurrence(View v)	{
		Intent i = new Intent(v.getContext(), Recurrence.class);
		i.putExtra("recurrenceIn",this.recurrence);
		startActivityForResult(i, 2);
	}
	/**
	 * Sends the event request to the cows server (via Cows-Mobile-Server)
	 * 
	 * @param TicketGrantingCookie
	 * @return Response from Cows-Mobile-Server
	 */
	private String doEvent(String tgc) {
		String ticketString = "?ticket=" + tgc;
		if (!this.parseParameters()) return "-3:" + this.parameters;
		HttpResponse out = null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet("http://128.120.151.3/development/CowsMobileProxy.php" + ticketString + setupRecurrence() + this.parameters);
		
		try {
			out = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return Utility.httpResponseToString(out);
	}

	private String setupRecurrence() {
		if (this.recurrence.equals("")) {
			String retString = "";
			//TODO: setup default retString
			return retString;
		}
		else return this.recurrence;
	}

	private boolean parseParameters() {
		if (!parseAndValidateText(R.id.Title)) return false;
		if (!parseAndValidateText(R.id.Description)) return false;
		if (!parseAndValidateSpinner(R.id.eventType)) return false;
		if (!parseAndValidateSpinner(R.id.Categories)) return false;
		if (!parseAndValidatePhone(R.id.Phone)) return false;
		if (!parseAndValidateTimes(R.id.StartTime,R.id.EndTime)) return false;
		if (!parseAndValidateDates(R.id.StartDate,R.id.EndDate)) return false;
		return true;
	}

	private boolean parseAndValidateDates(int startdate, int enddate) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean parseAndValidateTimes(int starttime, int endtime) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean parseAndValidatePhone(int field) {
		// TODO write this function
		return false;
	}

	private boolean parseAndValidateSpinner(int field) {
		//TODO: write this function
		return true;
	}

	private boolean parseAndValidateText(int field) {
		String value = ((TextView)findViewById(field)).getText().toString();
		if (value.equals(""))	{
				this.parameters = " cannot be blank";
				return false;
		}
		else	{
			this.parameters += Utility.getString(findViewById(field).getTag().toString(), value);
			return true;
		}
	}

}
