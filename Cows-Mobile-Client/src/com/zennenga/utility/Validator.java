package com.zennenga.utility;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;

import android.util.Log;
import android.widget.Button;

import com.zennenga.cows_fields.BaseField;
import com.zennenga.cows_fields.BooleanField;
import com.zennenga.cows_fields.BuildingField;
import com.zennenga.cows_fields.DateField;
import com.zennenga.cows_fields.MultiSpinnerField;
import com.zennenga.cows_fields.SpinnerField;
import com.zennenga.cows_fields.StaticField;
import com.zennenga.cows_fields.TextField;
import com.zennenga.cows_fields.TimeField;

public class Validator {
	private HashMap<String, BaseField> fieldMap;
	private static Validator instance = null;

	public static Validator getInstance()	{
		if (instance == null)	{
			instance = new Validator();
		}
		return instance;
	}
	private Validator() {
		this.fieldMap = new HashMap<String, BaseField>();
		// Base Fields (Text)
		fieldMap.put("EventTitle", new TextField("EventTitle", "", false));
		fieldMap.put("Description", new TextField("Description", "", true));
		fieldMap.put("Notes", new TextField("Notes", "", true));
		// Base Fields (Time)
		fieldMap.put("StartTime", new TimeField("StartTime", "12:00"));
		fieldMap.put("EndTime", new TimeField("EndTime", "12:00"));
		fieldMap.put("DisplayStartTime", new TimeField("DisplayStartTime",
				"12:00"));
		fieldMap.put("DisplayEndTime", new TimeField("DisplayEndTime", "12:00"));
		// Base Fields (Date)
		Calendar c = Calendar.getInstance();
		String date = (c.get(Calendar.MONTH) + 1) + "/"
				+ c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.YEAR);
		fieldMap.put("StartDate", new DateField("StartDate", date));
		fieldMap.put("EndDate", new DateField("EndDate", date));
		// Base Fields (Single spinner)
		fieldMap.put("EventTypeName", new SpinnerField(
				Utility.EVENT_ATTRIBUTE_ARRAY));
		// Base Fields (MultiSpinner)
		fieldMap.put("Locations", new MultiSpinnerField(
				Utility.LOCATION_ATTRIBUTE_ARRAY, true));
		fieldMap.put("Categories", new MultiSpinnerField(
				Utility.CATEGORY_ATTRIBUTE_ARRAY, false));
		// Base field (BuildingAndRoom)
		fieldMap.put("BuildingAndRoom", new BuildingField("", ""));
		// Recurrences
		fieldMap.put("IsRepeating", new BooleanField("IsRepeating", "false"));
		fieldMap.put("RecurrenceAppliesTo", new StaticField(
				"RecurrenceAppliesTo", "None"));
		fieldMap.put("RecurrenceMonday", new BooleanField("RecurrenceMonday",
				"false"));
		fieldMap.put("RecurrenceTuesday", new BooleanField("RecurrenceTuesday",
				"false"));
		fieldMap.put("RecurrenceWednesday", new BooleanField(
				"RecurrenceWednesday", "false"));
		fieldMap.put("RecurrenceThursday", new BooleanField(
				"RecurrenceThursday", "false"));
		fieldMap.put("RecurrenceFriday", new BooleanField("RecurrenceFriday",
				"false"));
		fieldMap.put("RecurrenceIsDayOfMonth", new BooleanField(
				"RecurrenceIsDayOfMonth", "false"));
		fieldMap.put("RecurrenceStartDate", new DateField(
				"RecurrenceStartDate", date));
		c.setTime(new GregorianCalendar().getTime());
		c.add(Calendar.MONTH, 1);
		SimpleDateFormat format = new SimpleDateFormat("M/d/yyyy");
		date = format.format(c.getTime());
		fieldMap.put("RecurrenceEndDate", new DateField("RecurrenceEndDate",
				date));
		fieldMap.put("RecurrenceFrequency", new StaticField(
				"RecurrenceFrequency", "1"));
		fieldMap.put("RecurrenceType", new StaticField("RecurrenceType", "D"));
		// Static Fields
		fieldMap.put("ByRoom", new StaticField("ByRoom", "true"));
		fieldMap.put("IsOffRecurrence", new StaticField("IsOffRecurrence",
				"False"));
		fieldMap.put("ConflictLastModified", new StaticField(
				"ConflictLastModified", "1/1/0001 12:00:00 AM"));
		fieldMap.put("gridScrollLeft", new StaticField("gridScrollLeft", "480"));
		fieldMap.put("WasRepeating", new StaticField("WasRepeating", "False"));
		fieldMap.put("SiteId", new StaticField("SiteId", Utility.SITE_ID));
		fieldMap.put("RecurrenceSaturday", new StaticField(
				"RecurrenceSaturday", "false"));
		fieldMap.put("RecurrenceSunday", new StaticField("RecurrenceSunday",
				"false"));

	}

	/**
	 * Returns the full string of GET parameters
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 */
	public String getString() throws IllegalArgumentException {
		Collection<BaseField> values = fieldMap.values();
		String retString = "";
		for (BaseField f : values) {
			if (f.checkValidation() || Arrays.asList(Utility.recurrenceFields).contains(f.getFieldName())) {
				try {
					
					retString += f.getData();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				throw new IllegalArgumentException(f.getFieldName()
						+ " was not set");
			}
		}
		return retString;
	}

	/**
	 * Update the fieldmap with the given data and check if the submit button
	 * can be activated
	 * 
	 * @param fieldName
	 * @param data
	 * @throws IllegalArgumentException
	 */
	public boolean setField(String fieldName, String data) {
		try {
			Log.i("Field", "Setting " + fieldName + " from " + fieldMap.get(fieldName).getRawData() + " to " + data);
			fieldMap.get(fieldName).setData(data);
			Utility.clearToast();
		} catch (IllegalArgumentException e) {
			Log.i("Toast", "Got error");
			Utility.showMessage(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean checkFieldsValidation(Boolean isRecurrence)	{
		Collection<BaseField> values = fieldMap.values();
		for (BaseField f : values) {
			Boolean b = Arrays.asList(Utility.recurrenceFields).contains(f.getFieldName());
			if (!isRecurrence) b = !b;
			if (!f.checkValidation() && b) {
				Log.i("Validation","Check Validation returned false");
				return false;
			}
		}
		Log.i("Validation","Check Validation returned true");
		return true;
	}

	/**
	 * Checks if all recurrence fields have been validated
	 * 
	 * @param b
	 */
	public void enableRecurrenceButton(Button b) {

	}

	/**
	 * Getter for a specific field
	 * 
	 * @param fieldName
	 * @return
	 */
	public BaseField getField(String fieldName) {
		return fieldMap.get(fieldName);
	}
	
	/**
	 * Destroys the current Validator instance
	 */
	public void clearValidator()	{
		Validator.instance = null;
	}
}
