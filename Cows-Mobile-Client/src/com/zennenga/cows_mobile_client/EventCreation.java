package com.zennenga.cows_mobile_client;

import java.io.IOException;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import com.zennenga.utility.Utility;
import com.zennenga.utility.Validator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zennenga.cows_fields.MultiSpinnerField;
import com.zennenga.cows_fields.SpinnerField;

public class EventCreation extends Activity {
	String tgc = "";
	View view = null;
	int tries = 0;
	Validator getValidator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_creation);
		tgc = getIntent().getStringExtra("TGC");
		
		this.getValidator = new Validator(this.view);
		
		//Popualte MultiSpinners
		MultiSelectSpinner multiSpinner = ((MultiSelectSpinner) findViewById(R.id.Locations));
		multiSpinner.setItems(getResources().getStringArray(R.array.Locations));
		((MultiSpinnerField) this.getValidator.getField(multiSpinner.getTag().toString())).setSpinner(multiSpinner);
		
		multiSpinner = ((MultiSelectSpinner) findViewById(R.id.Categories));
		multiSpinner.setItems(getResources().getStringArray(R.array.Categories));
		((MultiSpinnerField) this.getValidator.getField(multiSpinner.getTag().toString())).setSpinner(multiSpinner);

		//Populate Single Spinners
		populateSpinner(R.id.eventType,R.array.EventTypes);
		populateSpinner(R.id.buildingSelectSpinner2,R.array.buildingOptions);
		
		Spinner spin = (Spinner) findViewById(R.id.buildingSelectSpinner2);
		spin.setOnItemSelectedListener(new BuildingListener(this.getValidator));
		
		spin = (Spinner) findViewById(R.id.roomSelectSpinner2);
		((SpinnerField) this.getValidator.getField(spin.getTag().toString())).setSpinner(spin);
		
		//Text Setting/validation
		setTextListener(R.id.Title,"Title");
		setTextListener(R.id.Description,"Description");
		setTextListener(R.id.Phone,"ContactPhone");
		setTextListener(R.id.Notes,"Notes");
		
		//Time setting and validation
		setTimeListener(R.id.StartTime,"StartTime");
		setTimeListener(R.id.EndTime,"EndTime");
		
		//Date setting and validation
		DatePicker date = (DatePicker) findViewById(R.id.Date);
		date.init(Calendar.getInstance().get(Calendar.YEAR), 
				Calendar.getInstance().get(Calendar.MONTH), 
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 
				new OnDateChangedListener()	{

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						String date = dayOfMonth + "/" + monthOfYear + "/" + year;
						try	{
							getValidator.setField("StartDate", date);
							getValidator.setField("EndDate", date);
						}
						catch (IllegalArgumentException e)	{
							Utility.showMessage(e.getMessage(), ((View) view.getParent()).getContext());
						}
					}
			
		});
		date.setMinDate(System.currentTimeMillis() - 1000);
		
		//Multispinner setting and validation
		setMultiSpinnerListener(R.id.Categories,"Categories");
		setMultiSpinnerListener(R.id.Locations,"Locations");
		
		//Spinner setting and validation
		spin = (Spinner) findViewById(R.id.eventType);
		spin.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try {
					getValidator.setField("EventTypeName", "");
				}
				catch (IllegalArgumentException e)	{
					Utility.showMessage(e.getMessage(), getApplicationContext());
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				try {
					getValidator.setField("EventTypeName", "");
				}
				catch (IllegalArgumentException e)	{
					Utility.showMessage(e.getMessage(), getApplicationContext());
				}
			}
		});
	}
	
	private void setMultiSpinnerListener(int fieldId, final String fieldName) {
		MultiSelectSpinner ms = (MultiSelectSpinner) findViewById(R.id.Categories);
		ms.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				try {
					getValidator.setField(fieldName, "");
				}
				catch (IllegalArgumentException e)	{
					Utility.showMessage(e.getMessage(), getApplicationContext());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				try {
					getValidator.setField(fieldName, "");
				}
				catch (IllegalArgumentException e)	{
					Utility.showMessage(e.getMessage(), getApplicationContext());
				}
			}
			
		});
	}

	private void setTimeListener(int field, final String fieldIdentifier) {
		TimePicker t = ((TimePicker)findViewById(field));
		final String time = t.getCurrentHour() + ":" + t.getCurrentMinute();
		t.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)	{
					getValidator.setField(fieldIdentifier, time);
					getValidator.setField("Display" + fieldIdentifier, time);
				}
				
			}
			
		});
	}

	private void setTextListener(int field, final String fieldName) {
		final TextView textField = ((TextView) findViewById(field));
		textField.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)	{
					try {
						getValidator.setField(fieldName, textField.getText().toString());
					}
					catch (IllegalArgumentException e)	{
						textField.setError(e.getMessage());
					}
				}
			}
		});
	}

	private void populateSpinner(int fieldID, int arrayID) {
		Spinner spinner = (Spinner) findViewById(fieldID);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayID, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		((SpinnerField) this.getValidator.getField(spinner.getTag().toString())).setSpinner(spinner);
	}

	/**
	 * Handle cas reauthentication and recurrence configuration
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent d)	{
		if (requestCode == 1)	{
			this.tgc = d.getStringExtra("tgc");
			submitHandler(this.view);
		}
		else if (requestCode == 2)	{
			//TODO handle recurrence
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
		this.view = v;
		String getString = "";
		try {
			getString = this.getValidator.getString();
		}
		catch(IllegalArgumentException e)	{
			Utility.showMessage(e.getMessage(), getApplicationContext());
			return;
		}
		String url = Utility.BASE_URL + "?ticket=" + tgc + getString;
		AsyncEvent event = new AsyncEvent();
		event.execute(url);
	}

	/**
	 * Setup recurrence
	 * 
	 * @param v
	 */
	public void doRecurrence(View v)	{
		Intent i = new Intent(v.getContext(), Recurrence.class);
		//TODO Handle recurrence
		startActivityForResult(i, 2);
	}
	
	private class AsyncEvent extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute()	{
			Button b = (Button) findViewById(R.id.button1);
			b.setEnabled(false);
			b = (Button) findViewById(R.id.button2);
			b.setEnabled(false);
			Utility.showMessage("Please Wait. Submitting event to COWS...", getApplicationContext());
		}
		
		@Override
		protected String doInBackground(String... params) {
			HttpResponse out = null;
			DefaultHttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 5000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 5000);
		
			HttpGet request = new HttpGet(params[0]);
			try {
				out = client.execute(request);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return Utility.httpResponseToString(out);
		}
		
		@Override
		protected void onPostExecute(String response)	{
			
			if (response == null || response.equals(""))	{
				Utility.showMessage("Invalid response from server.", getApplicationContext());
				return;
			}
			
			String[] pieces = response.split(":", 2);
			
			if (!pieces[0].equals("0"))	{
				handleServerError(pieces[0],pieces[1]);
				Button b = (Button) findViewById(R.id.button1);
				b.setEnabled(true);
				b = (Button) findViewById(R.id.button2);
				b.setEnabled(true);
			}
			else	{
				//Generate Alert dialog to tell the user they can create another activity
				finishDialog();
			}
		}
	}
	/**
	 * Processes server errors given from CowsMobileServer
	 * 
	 * @param errorCode
	 * @param error
	 */
	public void handleServerError(String errorCode, String error) {
		switch(Integer.parseInt(errorCode))	{
		case -1:
			//Generic error
			Utility.showMessage("Error: " + error + " Please retry your submission.", getApplicationContext());
			return;
		case -2:
			//Failed Auth
			Intent i = new Intent(this.view.getContext(), CasAuth.class);
			i.putExtra("retryingAuth",true);
			i.putExtra("error", error);
			startActivityForResult(i, 1);
			return;
		case -3:
			//Event Error
			Utility.showMessage("Event error: " + error + " Please fix this error, and retry your submission.", getApplicationContext());
			return;
		case -4:
			//cURL error
			this.tries++;
			if (this.tries >= 5)	{
				Utility.showMessage("Network Error: " + error, getApplicationContext());
				return;
			}
			else submitHandler((this.view));
			return;
		default:
			//Generic Error
			Utility.showMessage("Error: " + error, getApplicationContext());
			return;
		}
	}
	
	/**
	 * Generates an Alert Dialog to ask the user if they want to create
	 * another event or finish and logout
	 */
	public void finishDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this.view.getContext());
		final Intent i = new Intent(this.view.getContext(), EventCreation.class);
		builder.setMessage("Event Creation complete! Would you like to create another?").setTitle("Success!");
		builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				int i = 0;
				while (!Utility.deauth())	{
					if (i > 10) break;
					i++;
				}
	   			   finish();
	           }
	       });
		builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
        	   	i.putExtra("TGC", tgc);	
   				startActivity(i);
   				finish();
				}   
	       });
		builder.show();
	}
}
