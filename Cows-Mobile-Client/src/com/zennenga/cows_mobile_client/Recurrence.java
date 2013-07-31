package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.zennenga.cows_fields.DateField;
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
		
		TextView e = (TextView) findViewById(R.id.frequency);
		
		e.setText(fields.get("RecurrenceFrequency"));
		
		e.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				EventCreation.getValidator.setField("RecurrenceFrequency", s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		//Spinners
		
		Spinner s = (Spinner) findViewById(R.id.dom);
		
		s.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				if (arg2 == 0)	{
					EventCreation.getValidator.setField("RecurrenceIsDayOfMonth", "true");
				}
				else EventCreation.getValidator.setField("RecurrenceIsDayOfMonth", "false");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
			
		});
		
		s.setVisibility(4);
		
		populateSpinner(R.id.type,R.array.recurrenceType);
		s = (Spinner) findViewById(R.id.type);
		
		s.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				String selected = (String) arg0.getSelectedItem();
				
				if (selected.equals("Month"))	{
					String options[] = new String[2];
					SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
					Date dt1;
					try {
						dt1 = format1.parse(EventCreation.getValidator.getField("RecurrenceStartDate").getData());
					} catch (UnsupportedEncodingException e) {
						return;
					} catch (ParseException e) {
						Log.e("DateError", "Parse error");
						return;
					}
					DateFormat format2=new SimpleDateFormat("EEEE"); 
					DateFormat format3 = new SimpleDateFormat("F");
					DateFormat format4 = new SimpleDateFormat("d");
					String finalDay=format2.format(dt1);
					String xth = format3.format(dt1);
					int dom = Integer.parseInt(format4.format(dt1));
					
					options[0] = dom + Utility.ordinal(dom) + " day";
					options[1] = xth + Utility.ordinal(dom) + " " + finalDay;
					
					Spinner spinner = (Spinner) findViewById(R.id.type);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(arg0.getContext(), android.R.layout.simple_spinner_dropdown_item, options); 
					spinner.setAdapter(adapter);
					spinner.setVisibility(0);
					EventCreation.getValidator.setField("RecurrenceType", "M");
					
					int index = 0;
					Spinner s = (Spinner) findViewById(R.id.dom);
					
					if (!fields.get("RecurrenceIsDayOfMonth").equals("true")) {
						index = 1;
					}
					s.setSelection(index);
					
				}
				else if (selected.equals("Day"))	{
					EventCreation.getValidator.setField("RecurrenceType", "D");
				}
				else if (selected.equals("Week"))	{
					findViewById(R.id.days).setVisibility(0);
					EventCreation.getValidator.setField("RecurrenceType", "W");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				findViewById(R.id.days).setVisibility(4);
				findViewById(R.id.dom).setVisibility(4);
			}
			
			
		});
		
		int index = 0;
		
		if (fields.get("RecurrenceType").equals("Week")) index = 1;
		else if (fields.get("RecurrenceType").equals("Month")) index = 2;
		s.setSelection(index);
		
		//Multi Spinners
		
		final MultiSelectSpinner m = (MultiSelectSpinner) findViewById(R.id.days);
		
		m.setItems(getResources().getStringArray(R.array.daysOfWeek));
		
		setupMultiSpinner();
		
		m.setVisibility(4);
		
		m.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				List<Integer> indices = m.getSelectedIndicies();
				EventCreation.getValidator.setField("RecurrenceMonday", "false", true);
				EventCreation.getValidator.setField("RecurrenceTuesday", "false", true);
				EventCreation.getValidator.setField("RecurrenceWednesday", "false", true);
				EventCreation.getValidator.setField("RecurrenceThursday", "false", true);
				EventCreation.getValidator.setField("RecurrenceFriday", "false", true);
				for (int index : indices)	{
					switch(index)	{
						case 0:
							EventCreation.getValidator.setField("RecurrenceMonday", "true", true);
							break;
						case 1:
							EventCreation.getValidator.setField("RecurrenceTuesday", "true", true);
							break;
						case 2:
							EventCreation.getValidator.setField("RecurrenceWednesday", "true", true);
							break;
						case 3:
							EventCreation.getValidator.setField("RecurrenceThursday", "true", true);
							break;
						case 4:
							EventCreation.getValidator.setField("RecurrenceFriday", "true", true);
							break;
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				EventCreation.getValidator.setField("RecurrenceMonday", "false", true);
				EventCreation.getValidator.setField("RecurrenceTuesday", "false", true);
				EventCreation.getValidator.setField("RecurrenceWednesday", "false", true);
				EventCreation.getValidator.setField("RecurrenceThursday", "false", true);
				EventCreation.getValidator.setField("RecurrenceFriday", "false", true);
			}
			
		});
		
		//Date Pickers
		
		DatePicker d = (DatePicker) findViewById(R.id.startDate);
		d.setMinDate(System.currentTimeMillis() - 1000);
		d.init(Calendar.getInstance().get(Calendar.YEAR), 
				Calendar.getInstance().get(Calendar.MONTH), 
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH), 
				new OnDateChangedListener()	{

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						String date = (monthOfYear+1) + "/" + dayOfMonth + "/" + year;
						try	{
							EventCreation.getValidator.setField("RecurrenceStartDate", date);
							((DateField)EventCreation.getValidator.getField("RecurrenceEndDate")).setComparator(date);
						}
						catch (IllegalArgumentException e)	{
							Utility.showMessage(e.getMessage(), ((View) view.getParent()).getContext());
						}
					}
			
		});
		
		d = (DatePicker) findViewById(R.id.endDate);
		d.init(Calendar.getInstance().get(Calendar.YEAR), 
				Calendar.getInstance().get(Calendar.MONTH), 
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+1, 
				new OnDateChangedListener()	{

					@Override
					public void onDateChanged(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						String date = (monthOfYear+1) + "/" + dayOfMonth + "/" + year;
						try	{
							EventCreation.getValidator.setField("RecurrenceEndDate", date);
						}
						catch (IllegalArgumentException e)	{
							Utility.showMessage(e.getMessage(), ((View) view.getParent()).getContext());
						}
					}
			
		});
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
		EventCreation.getValidator.setField("IsRepeating", "true");
		finish();
	}
	
	public void clearHandler(View v)	{
		for (int i = 0; i < Utility.recurrenceFields.length; i++)	{
			if (Utility.recurrenceFields.equals("RecurrenceStartDate")) {
				Calendar c = Calendar.getInstance();
				String date = (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR);
				EventCreation.getValidator.setField("RecurrenceStartDate", date, true);
			}
			
			else if (Utility.recurrenceFields.equals("RecurrenceEndDate"))	{
				Calendar c = Calendar.getInstance();
				String date = (c.get(Calendar.MONTH)+1) + "/" + (1+c.get(Calendar.DAY_OF_MONTH)) + "/" + c.get(Calendar.YEAR);
				EventCreation.getValidator.setField("RecurrenceEndDate", date, true);
			}
			else EventCreation.getValidator.setField(Utility.recurrenceFields[i], 
					Utility.recurrenceValues[i], true);
		}
		finish();
	}
	
	public void backHandler(View v)	{
		for (String field : Utility.recurrenceFields)	{
			EventCreation.getValidator.setField(field, this.fields.get(field), true);
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
