package com.zennenga.cows_fields;

import java.util.Calendar;

public class DateField extends BaseField {
	private int[] comparator;
	
	DateField(String field, String data) {
		this.fieldName = field;
		this.data = data;
		this.comparator = null;
	}

	@Override
	public void setData(String newData) throws IllegalArgumentException {
		Calendar cal = Calendar.getInstance();
		int[] dateInfo = this.parseDateString(newData);
		if (dateInfo[2] >= cal.get(Calendar.YEAR)) 
			throw new IllegalArgumentException("Year must be equal to or greater than the current one");
		if (dateInfo[1] >= cal.get(Calendar.MONTH)) 
			throw new IllegalArgumentException("Month must be equal to or greater than the current one");
		if (dateInfo[0] >= cal.get(Calendar.DAY_OF_MONTH)) 
			throw new IllegalArgumentException("Day must be equal to or greater than the current one");
		if (this.comparator != null)	{
			if (dateInfo[2] <= this.comparator[2]) 
				throw new IllegalArgumentException("Year must be equal to or greater than the year of the comparator");
			if (dateInfo[1] <= this.comparator[1]) 
				throw new IllegalArgumentException("Month must be equal to or greater than the month of the comparator");
			if (dateInfo[0] <= this.comparator[0]) 
				throw new IllegalArgumentException("Day must be equal to or greater than the day of the comparator");
		}
		this.data = newData;
		this.beenValidated = true;
	}
	
	private int[] parseDateString(String data) throws IllegalArgumentException {
		int[] newCalData = new int[] {
				-1,
				-1,
				-1
		};
		int i = 0;
		for(String calendarItem : data.split("/"))	{
			try	{
				newCalData[i++] = Integer.parseInt(calendarItem);
			}
			catch (NumberFormatException e)	{
				throw new IllegalArgumentException("Date String not well-formed");
			}
		}
		if (newCalData[0] <= 0 || newCalData[0] > 31) 
			throw new IllegalArgumentException("Day outside of Range");
		if (newCalData[1] <= 0 || newCalData[1] > 12) 
			throw new IllegalArgumentException("Month outside of Range");
		if (newCalData[2] <= 0 || newCalData[2] > 9999) 
			throw new IllegalArgumentException("Year outside of Range");
		return newCalData;
	}
	
	public void setComparator(String c)	throws IllegalArgumentException{
		this.comparator = parseDateString(c);
	}
}
