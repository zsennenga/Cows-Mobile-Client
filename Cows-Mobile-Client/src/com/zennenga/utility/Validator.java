package com.zennenga.utility;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;

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
	private HashMap<String,BaseField> fieldMap;
	
	public Validator()	{
		this.fieldMap = new HashMap<String,BaseField>();
		//Base Fields (Text)
		fieldMap.put("EventTitle", new TextField("Title","",false));
		fieldMap.put("ContactPhone", new TextField("Phone","",false));
		fieldMap.put("Description", new TextField("Description","",false));
		fieldMap.put("Notes", new TextField("Notes","",false));
		//Base Fields (Time)
		fieldMap.put("StartTime", new TimeField("StartTime","12:00"));
		fieldMap.put("EndTime", new TimeField("EndTime","12:00"));
		fieldMap.put("DisplayStartTime", new TimeField("DisplayStartTime","12:00"));
		fieldMap.put("DisplayEndTime", new TimeField("DisplayEndTime","12:00"));
		//Base Fields (Date)
		fieldMap.put("StartDate", new DateField("StartDate","1/1/1"));
		fieldMap.put("EndDate", new DateField("EndDate","1/1/1"));
		//Base Fields (Single spinner)
		fieldMap.put("EventTypeName", new SpinnerField(null,Utility.EVENT_ATTRIBUTE_ARRAY));
		//Base Fields (MultiSpinner)
		fieldMap.put("Locations", new MultiSpinnerField(null,Utility.LOCATION_ATTRIBUTE_ARRAY,true));
		fieldMap.put("Categories", new MultiSpinnerField(null,Utility.CATEGORY_ATTRIBUTE_ARRAY,false));
		//Base field (BuildingAndRoom)
		fieldMap.put("BuildingAndRoom", new BuildingField("",""));
		//Recurrences
		fieldMap.put("IsRepeating",new BooleanField("IsRepeating","false"));
		fieldMap.put("RecurrenceAppliesTo", new StaticField("RecurrenceAppliesTo","None"));
		fieldMap.put("RecurrenceMonday",new BooleanField("RecurrenceMonday","false"));
		fieldMap.put("RecurrenceTuesday",new BooleanField("RecurrenceTuesday","false"));
		fieldMap.put("RecurrenceWednesday",new BooleanField("RecurrenceWednesday","false"));
		fieldMap.put("RecurrenceThursday",new BooleanField("RecurrenceThursday","false"));
		fieldMap.put("RecurrenceFriday",new BooleanField("RecurrenceFriday","false"));
		fieldMap.put("RecurrenceIsDayOfMonth",new BooleanField("RecurrenceIsDayOfMonth","false"));
		fieldMap.put("RecurrenceStartDate",new DateField("RecurrenceStartDate","7/5/2013"));
		fieldMap.put("RecurrenceEndDate",new DateField("RecurrenceEndDate","8/4/2013"));
		fieldMap.put("RecurrenceFrequency",new StaticField("RecurrenceFrequency","1"));
		fieldMap.put("RecurrenceType",new StaticField("RecurrenceType","D"));
		//Static Fields
		fieldMap.put("ByRoom",new StaticField("ByRoom","true"));
		fieldMap.put("IsOffRecurrence",new StaticField("IsOffRecurrence","False"));
		fieldMap.put("ConflictLastModified", new StaticField("ConflictLastModified","1/1/0001 12:00:00 AM"));
		fieldMap.put("gridScrollLeft", new StaticField("gridScrollLeft","480"));
		fieldMap.put("WasRepeating",new StaticField("WasRepeating","False"));
		fieldMap.put("SiteId",new StaticField("SiteId","its"));
		fieldMap.put("RecurrenceSaturday",new StaticField("RecurrenceSaturday","false"));
		fieldMap.put("RecurrenceSunday",new StaticField("RecurrenceSunday","false"));
	}
	

	public String getString() {
		Collection<BaseField> values = fieldMap.values();
		String retString = "";
		for (BaseField f : values)	{
			if (f.checkValidation())	{
				try {
					retString += f.getData();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			else	{
				throw new IllegalArgumentException(f.getFieldName() + " was not set");
			}
		}
		return retString;
	}
	
	public void setField(String fieldName, String data) throws IllegalArgumentException {
		fieldMap.get(fieldName).setData(data);
	}
}
