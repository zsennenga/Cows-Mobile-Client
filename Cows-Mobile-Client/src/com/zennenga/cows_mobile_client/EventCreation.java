package com.zennenga.cows_mobile_client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.zennenga.cows_fields.MultiSpinnerField;
import com.zennenga.cows_fields.SpinnerField;
import com.zennenga.cows_fields.TimeField;
import com.zennenga.utility.Utility;
import com.zennenga.utility.Validator;

public class EventCreation extends Activity {
	private String tgc = "";
	private View view = null;
	private int tries = 0;
	private Validator fieldValidator;
	private boolean settingTime = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_creation);
		setupUI(findViewById(R.id.s));
		tgc = getIntent().getStringExtra("TGC");
		
		if (tgc == null || tgc == "") finish();

		Utility.updateContext(this);

		fieldValidator = Validator.getInstance();
		
		updateButton();

		// Populate MultiSpinners
		MultiSelectSpinner multiSpinner = ((MultiSelectSpinner) findViewById(R.id.Locations));
		multiSpinner.setItems(getResources().getStringArray(R.array.Locations));
		((MultiSpinnerField) fieldValidator.getField(multiSpinner
				.getTag().toString())).setSpinner(multiSpinner);

		multiSpinner = ((MultiSelectSpinner) findViewById(R.id.Categories));
		multiSpinner
				.setItems(getResources().getStringArray(R.array.Categories));
		((MultiSpinnerField) fieldValidator.getField(multiSpinner
				.getTag().toString())).setSpinner(multiSpinner);

		// Populate Single Spinners
		populateSpinner(R.id.eventType, R.array.EventTypes);
		populateSpinner(R.id.buildingSelectSpinner2, R.array.buildingOptions);

		Spinner spin = (Spinner) findViewById(R.id.buildingSelectSpinner2);
		spin.setOnItemSelectedListener(new BuildingListener());

		spin = (Spinner) findViewById(R.id.roomSelectSpinner2);
		spin.setOnItemSelectedListener(new RoomListener());

		// Text Setting/validation
		setTextListener(R.id.Title, "EventTitle");
		setTextListener(R.id.Description, "Description");
		setTextListener(R.id.Notes, "Notes");

		// Time setting and validation
		setupTimeFields();

		// Date setting and validation
		DatePicker date = (DatePicker) findViewById(R.id.Date);
		date.init(Calendar.getInstance().get(Calendar.YEAR), Calendar
				.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
				new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						String date = (monthOfYear + 1) + "/" + dayOfMonth
								+ "/" + year;
						fieldValidator.setField("StartDate", date);
						fieldValidator.setField("EndDate", date);
						updateButton();
					}

				});
		date.setMinDate(System.currentTimeMillis() - 1000);

		// Multispinner setting and validation
		setMultiSpinnerListener(R.id.Categories, "Categories");
		setMultiSpinnerListener(R.id.Locations, "Locations");

		// Spinner setting and validation
		spin = (Spinner) findViewById(R.id.eventType);
		((SpinnerField) fieldValidator.getField("EventTypeName"))
				.setSpinner(spin);
		spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				fieldValidator.setField("EventTypeName", "");
				updateButton();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				fieldValidator.setField("EventTypeName", "");
				updateButton();
			}
		});
	}

	/**
	 * Sets Listeners for a MultiSelectSpinners
	 * 
	 * @param fieldId
	 * @param fieldName
	 */
	private void setMultiSpinnerListener(int fieldId, final String fieldName) {
		MultiSelectSpinner ms = (MultiSelectSpinner) findViewById(fieldId);
		((MultiSpinnerField) fieldValidator.getField(fieldName))
				.setSpinner(ms);
		ms.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				fieldValidator.setField(fieldName, "");
				updateButton();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				fieldValidator.setField(fieldName, "");
				updateButton();
			}

		});
	}
	
	public void setupUI(View view) {

	    //Set up touch listener for non-text box views to hide keyboard.
	    if(!(view instanceof EditText)) {

	        view.setOnTouchListener(new OnTouchListener() {

	            public boolean onTouch(View v, MotionEvent event) {
	               hideSoftKeyboard();
	               return false;
	            }

	        });
	    }

	    //If a layout container, iterate over children and seed recursion.
	    if (view instanceof ViewGroup) {

	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

	            View innerView = ((ViewGroup) view).getChildAt(i);

	            setupUI(innerView);
	        }
	    }
	}
	
	private void hideSoftKeyboard() {
	    InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}

	/**
	 * Sets the Listeners for StartTime and EndTime
	 */
	private void setupTimeFields() {

		TimePicker t = ((TimePicker) findViewById(R.id.EndTime));
		t.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				if (settingTime) return;
				settingTime = true;
				TimePicker t = ((TimePicker) findViewById(R.id.EndTime));
				Integer h = t.getCurrentHour();
				if (t.getCurrentMinute() % 15 != 0)
					t.setCurrentMinute(getNewMins(t.getCurrentMinute()));
				t.setCurrentHour(h);
				String time = t.getCurrentHour() + ":" + parseMinute(t.getCurrentMinute());
				boolean success = fieldValidator.setField("EndTime", time);
				if (success) fieldValidator.setField("DisplayEndTime", time);
				if (success) ((TimeField) fieldValidator.getField("StartTime")).setComparator(time);
				if (((TimeField) fieldValidator.getField("StartTime")).checkSet() && success) 
				{
					
					fieldValidator.setField("StartTime",
							((TimeField) fieldValidator.getField("StartTime"))
									.getTime());
				}
				updateButton();
				settingTime = false;
			}
			

		});

		t = ((TimePicker) findViewById(R.id.StartTime));
		t.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				if (settingTime) return;
				settingTime = true;
				TimePicker t = ((TimePicker) findViewById(R.id.StartTime));
				Integer h = t.getCurrentHour();
				if (t.getCurrentMinute() % 15 != 0)
					t.setCurrentMinute(getNewMins(t.getCurrentMinute()));
				t.setCurrentHour(h);
				String time = t.getCurrentHour() + ":" + parseMinute(t.getCurrentMinute());
				boolean success;
				success = fieldValidator.setField("StartTime", time);
				if (success) fieldValidator.setField("DisplayStartTime", time);
				updateButton();
				settingTime = false;
			}

		});

		t = ((TimePicker) findViewById(R.id.EndTime));
		t.setCurrentMinute(getNewMins(t.getCurrentMinute()));
		t.setCurrentHour((t.getCurrentHour() + 1) % 24);
		int mins = t.getCurrentMinute();
		boolean success;
		if (mins == 0) {
			success = fieldValidator.setField("EndTime", t.getCurrentHour() + ":00");
			if (success) fieldValidator.setField("DisplayEndTime", t.getCurrentHour() + ":00");
		} else {
			success = fieldValidator.setField("EndTime",
					t.getCurrentHour() + ":" + t.getCurrentMinute());
			if (success) fieldValidator.setField("DisplayEndTime", t.getCurrentHour() + ":"
					+ t.getCurrentMinute());
		}
		updateButton();

		t = ((TimePicker) findViewById(R.id.StartTime));
		t.setCurrentMinute(getNewMins(t.getCurrentMinute()));
		mins = t.getCurrentMinute();
		if (mins == 0) {
			success = fieldValidator.setField("StartTime", t.getCurrentHour() + ":00");
			if (success) fieldValidator.setField("DisplayStartTime", t.getCurrentHour()
					+ ":00");
		} else {
			success = fieldValidator.setField("StartTime",
					t.getCurrentHour() + ":" + t.getCurrentMinute());
			if (success) fieldValidator.setField("DisplayStartTime", t.getCurrentHour() + ":"
					+ t.getCurrentMinute());
		}
		updateButton();
	}
	
	private String parseMinute(Integer currentMinute) {
		if (currentMinute == 0) return "00";
		else return String.valueOf(currentMinute);
	}

	/**
	 * Converts a value 0-59 to the closest 15 minute block
	 * 
	 * @param currentMinute
	 * @return
	 */
	private int getNewMins(Integer currentMinute) {
		if (currentMinute % 15 < 7)
			return (((int) Math.floor(currentMinute / 15) + 1) * 15) % 60;
		else
			return (int) Math.floor(currentMinute / 15) * 15;
	}

	/**
	 * Stores + validates text on every focus change
	 * 
	 * @param field
	 * @param fieldName
	 */
	private void setTextListener(int field, final String fieldName) {
		final TextView textField = ((TextView) findViewById(field));
		textField.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				fieldValidator.setField(fieldName, s.toString());
				updateButton();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
	}

	/**
	 * Adds all values to a spinner.
	 * 
	 * @param fieldID
	 * @param arrayID
	 */
	private void populateSpinner(int fieldID, int arrayID) {
		Spinner spinner = (Spinner) findViewById(fieldID);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, arrayID, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	/**
	 * Handle cas reauthentication and recurrence configuration
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent d) {
		if (requestCode == 1) {
			tgc = d.getStringExtra("tgc");
			submitHandler(view);
		}
		return;
	}

	/**
	 * Handle the back button
	 * 
	 * @param v
	 */
	public void backHandler(View v) {
		Utility.deauth();
		finish();
	}

	/**
	 * Handle the submit button
	 * 
	 * @param v
	 */
	public void submitHandler(View v) {
		view = v;
		String getString = "";
		try {
			getString = fieldValidator.getString();
		} catch (IllegalArgumentException e) {
			Utility.showMessage(e.getMessage());
			return;
		}
		String url = Utility.BASE_URL;
		getString += "&tgc=" + tgc + "&siteId=" + Utility.SITE_ID;

		Log.i("Start",
				fieldValidator.getField("RecurrenceStartDate")
						.getRawData());
		Log.i("End", fieldValidator.getField("RecurrenceEndDate")
				.getRawData());

		AsyncEvent event = new AsyncEvent();
		event.execute(url, getString);
	}

	/**
	 * Setup recurrence
	 * 
	 * @param v
	 */
	public void doRecurrence(View v) {
		Intent i = new Intent(v.getContext(), Recurrence.class);
		startActivity(i);
	}

	/**
	 * AsyncTask to execute the event HTTP request
	 * 
	 * @author its-zach
	 * 
	 */
	private class AsyncEvent extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			Button b = (Button) findViewById(R.id.button1);
			b.setEnabled(false);
			b = (Button) findViewById(R.id.button2);
			b.setEnabled(false);
			b = (Button) findViewById(R.id.Recurrence);
			b.setEnabled(false);
			Utility.showMessage("Please Wait. Submitting event to COWS...");
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> hParams = new ArrayList<NameValuePair>(2);
			String paramTokens[] = params[1].split("&");

			for (String paramToken : paramTokens) {
				if (paramToken != "") {
					String[] token = paramToken.split("=");
					if (token.length == 1) {
						hParams.add(new BasicNameValuePair(token[0], ""));
					} else
						try {
							hParams.add(new BasicNameValuePair(token[0],
									URLDecoder.decode(token[1], "UTF-8")));
						} catch (UnsupportedEncodingException e) {
							return "-1:This should never happen";
						}
				}
			}

			HttpResponse out = null;
			DefaultHttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 5000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 5000);
			HttpPost request = new HttpPost(params[0]);
			try {
				request.setEntity(new UrlEncodedFormEntity(hParams));
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
		protected void onPostExecute(String response) {

			Log.i("Response", response);

			if (response == null || response.equals("")) {
				Utility.showMessage("Invalid response from server.");
				Button b = (Button) findViewById(R.id.button1);
				b.setEnabled(true);
				b = (Button) findViewById(R.id.button2);
				b.setEnabled(true);
				b = (Button) findViewById(R.id.Recurrence);
				b.setEnabled(true);
				return;
			}

			String[] pieces = response.split(":", 2);

			if (!pieces[0].equals("0") && !pieces[0].equals("-8")) {
				handleServerError(pieces[0], pieces[1]);
				Button b = (Button) findViewById(R.id.button1);
				b.setEnabled(true);
				b = (Button) findViewById(R.id.button2);
				b.setEnabled(true);
				b = (Button) findViewById(R.id.Recurrence);
				b.setEnabled(true);
			} else {
				// Generate Alert dialog to tell the user they can create
				// another activity
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
		switch (Integer.parseInt(errorCode)) {
		case -1:
			// Generic error
			Utility.showMessage("Error: " + error
					+ " Please retry your submission.");
			return;
		case -2:
			// Failed Auth
			Intent i = new Intent(view.getContext(), CasAuth.class);
			i.putExtra("retryingAuth", true);
			i.putExtra("error", error);
			startActivity(i);
			return;
		case -3:
			// Event Error
			Utility.showMessage("Event error: " + error
					+ " Please fix this error, and retry your submission.");
			return;
		case -4:
			// cURL error
			tries++;
			if (tries >= 5) {
				Utility.showMessage("Network Error: " + error);
				return;
			} else
				submitHandler((view));
			return;
		default:
			// Generic Error
			Utility.showMessage("Error: " + error);
			return;
		}
	}

	/**
	 * Generates an Alert Dialog to ask the user if they want to create another
	 * event or finish and logout
	 */
	public void finishDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				view.getContext());
		final Intent i = new Intent(view.getContext(), EventCreation.class);
		builder.setMessage(
				"Event Creation complete! Would you like to create another?")
				.setTitle("Success!");
		builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Utility.deauth();
				finish();
			}
		});
		builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Validator.getInstance().clearValidator();
				i.putExtra("TGC", tgc);
				startActivity(i);
				finish();
			}
		});
		builder.show();
	}
	
	/**
	 * Unlock the button if all necessary fields have been set and validated
	 * 
	 * @param button
	 */
	private void updateButton() {
		Button b = (Button) findViewById(R.id.button1);
		b.setEnabled(false);
		if (fieldValidator.checkFieldsValidation(false))
			b.setEnabled(true);
	}
}
