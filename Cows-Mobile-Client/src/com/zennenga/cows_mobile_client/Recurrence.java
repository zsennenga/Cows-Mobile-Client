package com.zennenga.cows_mobile_client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.zennenga.cows_fields.DateField;
import com.zennenga.utility.Utility;
import com.zennenga.utility.Validator;

public class Recurrence extends Activity {

	private HashMap<String, String> fields;
	private Validator fieldValidator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recurrence);
		setupUI(findViewById(R.id.lel));
		fields = new HashMap<String, String>();
		Utility.updateContext(this);
		Utility.isRecurrenceNow = true;
		
		fieldValidator = Validator.getInstance();

		updateFields();

		// Text Fields

		TextView e = (TextView) findViewById(R.id.frequency);

		e.setText(fields.get("RecurrenceFrequency"));

		e.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				fieldValidator.setField("RecurrenceFrequency",
						s.toString());
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

		// Spinners

		Spinner s = (Spinner) findViewById(R.id.dom);

		s.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				if (arg2 == 0) {
					fieldValidator.setField(
							"RecurrenceIsDayOfMonth", "true");
				} else
					fieldValidator.setField(
							"RecurrenceIsDayOfMonth", "false");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		s.setVisibility(View.GONE);

		populateSpinner(R.id.type, R.array.recurrenceType);
		s = (Spinner) findViewById(R.id.type);

		s.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String selected = (String) arg0.getSelectedItem();

				if (selected.equals("Month")) {
					String options[] = new String[2];
					SimpleDateFormat format1 = new SimpleDateFormat(
							"MM/dd/yyyy");
					Date dt1;
					try {
						dt1 = format1.parse(fieldValidator
								.getField("RecurrenceStartDate").getRawData());
					} catch (ParseException e) {
						Log.e("DateError", "Parse error");
						return;
					}
					DateFormat format2 = new SimpleDateFormat("EEEE");
					DateFormat format3 = new SimpleDateFormat("F");
					DateFormat format4 = new SimpleDateFormat("d");
					String finalDay = format2.format(dt1);
					int xth = Integer.parseInt(format3.format(dt1));
					int dom = Integer.parseInt(format4.format(dt1));

					options[0] = Utility.ordinal(dom) + " day";
					options[1] = Utility.ordinal(xth) + " " + finalDay;

					Spinner spinner = (Spinner) findViewById(R.id.dom);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							arg0.getContext(),
							android.R.layout.simple_spinner_dropdown_item,
							options);
					spinner.setAdapter(adapter);
					spinner.setVisibility(View.VISIBLE);
					findViewById(R.id.days).setVisibility(View.GONE);
					fieldValidator.setField("RecurrenceType", "M");

					int index = 0;

					if (!fields.get("RecurrenceIsDayOfMonth").equals("true")) {
						index = 1;
					}
					spinner.setSelection(index);

				} else if (selected.equals("Day")) {
					fieldValidator.setField("RecurrenceType", "D");
					findViewById(R.id.days).setVisibility(View.GONE);
					findViewById(R.id.dom).setVisibility(View.GONE);
				} else if (selected.equals("Week")) {
					findViewById(R.id.days).setVisibility(View.VISIBLE);
					findViewById(R.id.dom).setVisibility(View.GONE);
					fieldValidator.setField("RecurrenceType", "W");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				findViewById(R.id.days).setVisibility(View.GONE);
				findViewById(R.id.dom).setVisibility(View.GONE);
			}

		});

		int index = 0;

		if (fields.get("RecurrenceType").equals("W"))
			index = 1;
		else if (fields.get("RecurrenceType").equals("M")) {
			Spinner x = (Spinner) findViewById(R.id.dom);
			x.setVisibility(View.VISIBLE);
			index = 2;
		}
		s.setSelection(index);

		// Multi Spinners

		final MultiSelectSpinner m = (MultiSelectSpinner) findViewById(R.id.days);

		m.setItems(getResources().getStringArray(R.array.daysOfWeek));

		setupMultiSpinner();

		if (index != 1)
			m.setVisibility(View.GONE);

		// Date Pickers

		DatePicker d = (DatePicker) findViewById(R.id.startDate);
		d.setMinDate(System.currentTimeMillis() - 1000);
		String dateVal[] = fieldValidator
				.getField("RecurrenceStartDate").getRawData().split("/");
		d.init(Integer.parseInt(dateVal[2]), Integer.parseInt(dateVal[0]) - 1,
				Integer.parseInt(dateVal[1]), new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						String date = (monthOfYear + 1) + "/" + dayOfMonth
								+ "/" + year;
						try {
							fieldValidator.setField(
									"RecurrenceStartDate", date);
							((DateField) fieldValidator
									.getField("RecurrenceEndDate"))
									.setComparator(date);
							date = fieldValidator.getField(
									"RecurrenceEndDate").getRawData();
							fieldValidator.setField(
									"RecurrenceEndDate", date);
						} catch (IllegalArgumentException e) {
							Utility.showMessage(e.getMessage());
						}
					}

				});

		d = (DatePicker) findViewById(R.id.endDate);
		dateVal = fieldValidator.getField("RecurrenceEndDate")
				.getRawData().split("/");
		d.init(Integer.parseInt(dateVal[2]), Integer.parseInt(dateVal[0]) - 1,
				Integer.parseInt(dateVal[1]), new OnDateChangedListener() {

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						String date = (monthOfYear + 1) + "/" + dayOfMonth
								+ "/" + year;
						try {
							fieldValidator.setField(
									"RecurrenceEndDate", date);
						} catch (IllegalArgumentException e) {
							Utility.showMessage(e.getMessage());
						}
					}

				});
		d.setMinDate(System.currentTimeMillis() + 24 * 60 * 60 * 1000 - 1000);
	}

	private void setupMultiSpinner() {
		MultiSelectSpinner m = (MultiSelectSpinner) findViewById(R.id.days);

		List<Integer> indices = new ArrayList<Integer>();
		if (fields.get("RecurrenceMonday").equals("true"))
			indices.add(0);
		if (fields.get("RecurrenceTuesday").equals("true"))
			indices.add(1);
		if (fields.get("RecurrenceWednesday").equals("true"))
			indices.add(2);
		if (fields.get("RecurrenceThursday").equals("true"))
			indices.add(3);
		if (fields.get("RecurrenceFriday").equals("true"))
			indices.add(4);
		int index[] = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++) {
			index[i] = indices.get(i);
		}

		m.setSelection(index);
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

	private void updateFields() {
		for (String field : Utility.recurrenceFields) {
			String s = fieldValidator.getField(field).getRawData();
			fields.put(field, s);
		}
	}

	public void submitHandler(View v) {
		fieldValidator.setField("IsRepeating", "true");
		Utility.isRecurrenceNow = false;
		finish();
	}

	public void clearHandler(View v) {
		for (int i = 0; i < Utility.recurrenceFields.length; i++) {
			if (Utility.recurrenceFields.equals("RecurrenceStartDate")) {
				Calendar c = Calendar.getInstance();
				String date = (c.get(Calendar.MONTH) + 1) + "/"
						+ c.get(Calendar.DAY_OF_MONTH) + "/"
						+ c.get(Calendar.YEAR);
				fieldValidator.setField("RecurrenceStartDate",
						date);
			}

			else if (Utility.recurrenceFields.equals("RecurrenceEndDate")) {
				Calendar c = Calendar.getInstance();
				String date = (c.get(Calendar.MONTH) + 1) + "/"
						+ (1 + c.get(Calendar.DAY_OF_MONTH)) + "/"
						+ c.get(Calendar.YEAR);
				fieldValidator.setField("RecurrenceEndDate", date);
			} else
				fieldValidator.setField(
						Utility.recurrenceFields[i],
						Utility.recurrenceValues[i]);
		}
		Utility.isRecurrenceNow = false;
		finish();
	}

	public void backHandler(View v) {
		for (String field : Utility.recurrenceFields) {
			fieldValidator.setField(field, fields.get(field));
		}
		Utility.isRecurrenceNow = false;
		finish();
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
}
