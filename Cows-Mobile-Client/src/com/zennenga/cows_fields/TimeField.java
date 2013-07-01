package com.zennenga.cows_fields;

public class TimeField extends BaseField {
	private String comparator;
	
	public TimeField(String fieldName, String data) {
		this.fieldName = fieldName;
		this.data = parseTimeStringToString(data);
	}

	@Override
	public void setData(String newData) throws IllegalArgumentException {
		if (comparator != null)	{
			int[] newTimeData = parseTimeStringToArray(newData);
			int[] newTimeDataComparator = parseTimeStringToArray(this.comparator);
			if (newTimeData[0] > newTimeDataComparator[0])
				throw new IllegalArgumentException("End time must come after Start time");
			else if (newTimeData[0] == newTimeDataComparator[0] && newTimeDataComparator[1] <= newTimeData[1])
				throw new IllegalArgumentException("End time must come after Start time");
		}
		this.data = parseTimeStringToString(newData);
		this.beenValidated = true;
	}
	
	private String parseTimeStringToString(String data)  throws IllegalArgumentException {
		int [] timeData = parseTimeStringToArray(data);
		String meridian = " AM";
		int finalHour = ((timeData[0] % 12)+1);
		if (timeData[0] >= 11 && timeData[0] != 23) meridian = " PM";
		return finalHour + ":" + timeData[1] + meridian;
	}
	
	private int[] parseTimeStringToArray(String data) throws IllegalArgumentException{
		int[] newTimeData = new int[] {
				-1,
				-1,
		};
		int i = 0;
		for(String calendarItem : data.split(":"))	{
			try	{
				newTimeData[i++] = Integer.parseInt(calendarItem);
			}
			catch (NumberFormatException e)	{
				throw new IllegalArgumentException("Date String not well-formed");
			}
		}
		if (newTimeData[0] <= 0 || newTimeData[0] > 23) 
			throw new IllegalArgumentException("Hour outside of Range");
		if (newTimeData[1] <= 0 || newTimeData[1] > 59) 
			throw new IllegalArgumentException("Minute outside of Range");
		return newTimeData;
	}
	
	public void setComparator(String c)	{
		this.comparator = c;
	}
}
