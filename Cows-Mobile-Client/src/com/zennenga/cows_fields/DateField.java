package com.zennenga.cows_fields;

import java.util.Calendar;

import android.util.Log;

public class DateField extends BaseField {
	private int[] comparator;
	
	public DateField(String field, String data) {
		this.fieldName = field;
		this.data = data;
		this.comparator = null;
		this.beenValidated = true;
	}
	/**
	 * Checks that a string is a valid date and it is valid relative to the comparator
	 */
	@Override
	public void setData(String newData) throws IllegalArgumentException {
		this.beenValidated = false;
		Calendar cal = Calendar.getInstance();
		int[] dateInfo = this.parseDateString(newData);
		Log.i("Date", newData);
		if (dateInfo[2] < cal.get(Calendar.YEAR)) 
			throw new IllegalArgumentException("Year must be equal to or greater than today's year");
		if (dateInfo[0] < cal.get(Calendar.MONTH)) 
			throw new IllegalArgumentException("Month must be equal to or greater than the today's month");
		if (dateInfo[1] < cal.get(Calendar.DAY_OF_MONTH) && dateInfo[0] == cal.get(Calendar.MONTH)) 
			throw new IllegalArgumentException("Day must be equal to or greater than the today's day");
		if (this.comparator != null)	{
			if (dateInfo[2] < this.comparator[2]) 
				throw new IllegalArgumentException("Year must be equal to or greater than the year of the comparator");
			if (dateInfo[0] < this.comparator[0]) 
				throw new IllegalArgumentException("Month must be equal to or greater than the month of the comparator");
			if (dateInfo[1] < this.comparator[1]) 
				throw new IllegalArgumentException("Day must be equal to or greater than the day of the comparator");
		}
		this.data = newData;
		this.beenValidated = true;
	}
	/**
	 * Changes a date in the format Day/Month/Year into an int arry
	 * 
	 * @param data
	 * @return
	 * @throws IllegalArgumentException
	 */
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
		if (newCalData[1] <= 0 || newCalData[1] > 31) 
			throw new IllegalArgumentException("Day outside of Range");
		if (newCalData[0] <= 0 || newCalData[0] > 12) 
			throw new IllegalArgumentException("Month outside of Range");
		if (newCalData[2] <= 0 || newCalData[2] > 9999) 
			throw new IllegalArgumentException("Year outside of Range");
		return newCalData;
	}
	/**
	 * Comparators are used to check if a date comes before another date.
	 * If this is a EndDate set the comparator to the StartDate
	 * 
	 * Do not set the comparator on StartDate
	 * 
	 * @param c
	 * @throws IllegalArgumentException
	 */
	public void setComparator(String c)	throws IllegalArgumentException{
		this.comparator = parseDateString(c);
	}
	public void updateAndComparator(String date) {
		this.comparator = null;
		this.setData(date);
		this.setComparator(date);
	}
}
