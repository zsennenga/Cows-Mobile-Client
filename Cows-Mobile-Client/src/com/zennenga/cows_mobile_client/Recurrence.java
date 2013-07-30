package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zennenga.utility.Utility;

public class Recurrence extends Activity {
	
	boolean error;
	HashMap<String, String> fields;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recurrence);
		this.fields = new HashMap<String,String>();
		updateFields();
		
		//Text Fields
		
		EditText e = (EditText) findViewById(R.id.frequency);
		
		e.setText(fields.get("RecurrenceFrequency"));
		
		//Spinners
		
		Spinner s = (Spinner) findViewById(R.id.dom);
		int index = 0;
		
		if (!fields.get("RecurrenceIsDayOfMonth").equals("true")) index = 1;
		s.setSelection(index);
		
		s.setVisibility(1);
		
		s = (Spinner) findViewById(R.id.type);
		populateSpinner(R.id.type,R.array.recurrenceType);
		
		index = 0;
		
		if (fields.get("RecurrenceType").equals("W")) index = 1;
		else if (fields.get("RecurrenceType").equals("M")) index = 2;
		s.setSelection(index);
		
		//Multi Spinners
		
		MultiSelectSpinner m = (MultiSelectSpinner) findViewById(R.id.days);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.daysOfWeek, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		m.setAdapter(adapter);
		
		setupMultiSpinner();
		
		
		//Date Pickers
		
		DatePicker d = (DatePicker) findViewById(R.id.startDate);
		d.setMinDate(System.currentTimeMillis() - 1000);
		
		d = (DatePicker) findViewById(R.id.endDate);
		d.setMinDate(System.currentTimeMillis() + 24*60*60*1000 - 1000);
	}
	
	private void setupMultiSpinner() {
		MultiSelectSpinner m = (MultiSelectSpinner) findViewById(R.id.days);
		
		List<Integer> indices = new ArrayList<Integer>();
		if (fields.get("RecurrenceMonday").equals("true")) indices.add(1);
		if (fields.get("RecurrenceTuesday").equals("true")) indices.add(2);
		if (fields.get("RecurrenceWednesday").equals("true")) indices.add(3);
		if (fields.get("RecurrenceThursday").equals("true")) indices.add(4);
		if (fields.get("RecurrenceFriday").equals("true")) indices.add(5);
		int index[] = new int[indices.size()];
		for (int i = 0; i < indices.size(); i++)	{
			index[i] = indices.get(i);
		}
		
		m.setSelection(index);
		
	}

	private void updateFields()	{
		for (String field : Utility.recurrenceFields)	{
			try {
				String s = EventCreation.getValidator.getField(field).getData();
				this.fields.put(field, s);
			} catch (UnsupportedEncodingException e) {
				finish();
			}
		}
	}
	
	public void submitHandler(View v)	{
		finish();
	}
	
	public void clearHandler(View v)	{
		for (int i = 0; i < Utility.recurrenceFields.length; i++)	{
			EventCreation.getValidator.setField(Utility.recurrenceFields[i], 
					Utility.recurrenceValues[i], false);
		}
		finish();
	}
	
	public void backHandler(View v)	{
		for (String field : Utility.recurrenceFields)	{
			EventCreation.getValidator.setField(field, this.fields.get(field), false);
		}
		finish();
	}

	/**
	 * Adds all values to a spinner.
	 * @param fieldID
	 * @param arrayID
	 */
	private void populateSpinner(int fieldID, int arrayID) {
		Spinner spinner = (Spinner) findViewById(fieldID);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, arrayID, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
}
