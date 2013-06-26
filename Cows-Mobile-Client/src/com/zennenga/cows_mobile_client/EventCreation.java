package com.zennenga.cows_mobile_client;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
		//TODO integrate building/room spinners
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
		
		String[] pieces = response.split(":", 2);
		
		if (!pieces[0].equals("0"))	{
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
			//Generate Alert dialog to tell the user they can create another activity
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			final Intent i = new Intent(v.getContext(), EventCreation.class);
			builder.setMessage("Event Creation complete! Would you like to create another?").setTitle("Success!");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   	i.putExtra("TGC", tgc);	
		   				startActivity(i);
		           }
		       });
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		           }
		       });
			finish();
		}
	}
	
	private void setError(String error)	{
		//((TextView)findViewById(R.id.error)).setText(error);
		Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
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
		//TODO: change to dev.its.ucdavis.edu
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
	/**
	 * Generate default recurrence string if one has not been generated by doRecurrence
	 * @return Recurrence parameter string
	 */
	private String setupRecurrence() {
		if (this.recurrence.equals("")) {
			String retString = "";
			retString += Utility.getString("IsRepeating","false");
			retString += Utility.getString("RecurrenceAppliesTo", "None");
			retString += Utility.getString("RecurrenceMonday","false");
			retString += Utility.getString("RecurrenceTuesday","false");
			retString += Utility.getString("RecurrenceWednesday","false");
			retString += Utility.getString("RecurrenceThursday","false");
			retString += Utility.getString("RecurrenceFriday","false");
			retString += Utility.getString("RecurrenceIsDayOfMonth","false");
			retString += Utility.getString("RecurrenceStartDate","7/5/2013");
			retString += Utility.getString("RecurrenceEndDate","8/4/2013");
			retString += Utility.getString("RecurrenceFrequency","1");
			retString += Utility.getString("RecurrenceType","D");
			return retString;
		}
		else return this.recurrence;
	}
	/**
	 * 
	 * Parses all fields and generates a GET parameter string
	 * 
	 * @return if a valid parameter string could be generated
	 */
	private boolean parseParameters() {
		if (!parseAndValidateText(R.id.Title)) return false;
		if (!parseAndValidateText(R.id.Description)) return false;
		if (!parseAndValidateSpinner(R.id.eventType)) return false;
		if (!parseAndValidateMultiSpinner(R.id.Categories,true)) return false;
		parseAndValidateMultiSpinner(R.id.Locations,false);
		if (!parseAndValidateText(R.id.Phone)) return false;
		if (!parseAndValidateTimes(R.id.StartTime,R.id.EndTime)) return false;
		if (!parseAndValidateDates(R.id.Date)) return false;
		this.parameters += Utility.getString("Notes",((TextView)findViewById(R.id.Notes)).getText().toString());
		
		//Static Parameters
		//No idea
		this.parameters += Utility.getString("ByRoom","true");
		this.parameters += Utility.getString("IsOffRecurrence","false");
		//Only relevant for editing
		this.parameters += Utility.getString("WasRepeating","false");
		//This is an app for its
		this.parameters += Utility.getString("SiteId","its");
		//No events on weekends
		this.parameters += Utility.getString("RecurrenceSaturday","false");
		this.parameters += Utility.getString("RecurrenceSunday","false");
		return true;
	}

	private boolean parseAndValidateMultiSpinner(int id, boolean validate) {
		MultiSelectSpinner spinner = (MultiSelectSpinner) findViewById(id);
		List<String> selected = spinner.getSelectedStrings();
		if (validate)	{
			if(selected.size() == 0)	{
				this.parameters = spinner.getTag().toString() + " requires at least 1 option to be selected";
				return false;
			}
		}
		String out = "";
		for (String cat : selected)	{
			out += cat + "&";
		}
		this.parameters += Utility.getString(spinner.getTag().toString(),out.substring(0, out.length()-2));
		return true;
	}

	/**
	 * Checks if date is valid, and if it is, constructs the GET parameters for the date
	 * 
	 * @param startdate
	 * @return
	 */
	private boolean parseAndValidateDates(int date) {
		DatePicker dateView = (DatePicker) findViewById(date);
		int day = dateView.getDayOfMonth();
		int month = dateView.getMonth();
		int year = dateView.getYear();
		Calendar cal = Calendar.getInstance();
		
		if (year < cal.get(Calendar.YEAR))	{
			this.parameters = "Invalid Date";
			return false;
		}
		if (month < cal.get(Calendar.MONTH))	{
			this.parameters = "Invalid Date";
			return false;
		}
		if (month == cal.get(Calendar.MONTH) && day < cal.get(Calendar.DAY_OF_MONTH))	{
			this.parameters = "Invalid Date";
			return false;
		}
		this.parameters += Utility.getString("StartDate", month + "/" + day + "/" + year);
		this.parameters += Utility.getString("EndDate", month + "/" + day + "/" + year);
		return true;
	}
	/**
	 * Checks if times are valid (Mostly that starttime < endtime) and generates the get string if the times are valid
	 * 
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	private boolean parseAndValidateTimes(int starttime, int endtime) {
		TimePicker start = (TimePicker) findViewById(starttime);
		TimePicker end = (TimePicker) findViewById(endtime);
		
		int startHour = start.getCurrentHour();
		int startMinute = start.getCurrentMinute();
		
		int endHour = end.getCurrentHour();
		int endMinute = end.getCurrentMinute();
		
		if (startHour > endHour)	{
			this.parameters = "Start Time must come before End Time";
			return false;
		}
		if (startHour == endHour && startMinute > endMinute)	{
			this.parameters = "Start Time must come before End Time";
			return false;
		}
		this.parameters += Utility.getString("StartTime",timeToStr(startHour,startMinute));
		this.parameters += Utility.getString("EndTime",timeToStr(endHour,endMinute));
		this.parameters += Utility.getString("DisplayStartTime",timeToStr(startHour,startMinute));
		this.parameters += Utility.getString("DisplayEndTime",timeToStr(endHour,endMinute));
		return true;
	}
	/**
	 * Changes an hour (0-23) and minute to a string in the format hour(1-12):minute meridian
	 * @param startHour
	 * @param startMinute
	 * @return String in above format
	 */
	private String timeToStr(int startHour, int startMinute) {
		String meridian = " AM";
		int finalHour = ((startHour % 12)+1);
		if (startHour >= 11 && startHour != 23) meridian = " PM";
		return finalHour + ":" + startMinute + meridian;
	}

	/**
	 * Checks if a spinner has a selected item and generates a get string from that item
	 * 
	 * @param field
	 * @return
	 */
	private boolean parseAndValidateSpinner(int field) {
		Spinner spin = (Spinner) findViewById(field);
		if (spin.getSelectedItem() == null)	{
			this.parameters = spin.getTag().toString() + " must have an item selected";
			return false;
		}
		this.parameters += Utility.getString(spin.getTag().toString(),spin.getSelectedItem().toString());
		return true;
	}
	/**
	 * Checks that a field is not blank and generates a get string from it
	 * 
	 * @param field
	 * @return
	 */
	private boolean parseAndValidateText(int field) {
		String value = ((TextView)findViewById(field)).getText().toString();
		if (value.equals(""))	{
				this.parameters = findViewById(field).getTag() + " cannot be blank";
				return false;
		}
		else	{
			this.parameters += Utility.getString(findViewById(field).getTag().toString(), value);
			return true;
		}
	}

}
