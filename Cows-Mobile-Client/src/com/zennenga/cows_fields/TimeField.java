package com.zennenga.cows_fields;

public class TimeField extends BaseField {
	private String comparator;
	private String baseTime;
	private boolean beenSet = false;

	public TimeField(String fieldName, String data) {
		this.fieldName = fieldName;
		this.data = parseTimeStringToString(data);
		this.baseTime = data;
	}

	/**
	 * Converts a 24 hour time string into a 12 hour one and checks against a
	 * comparator
	 */
	@Override
	public void setData(String newData) throws IllegalArgumentException {
		this.beenSet = true;
		this.beenValidated = false;
		if (comparator != null) {
			int[] newTimeData = parseTimeStringToArray(newData);
			int[] newTimeDataComparator = parseTimeStringToArray(this.comparator);
			if (newTimeData[0] >= newTimeDataComparator[0])
				throw new IllegalArgumentException(
						"End time must come after Start time");
			else if (newTimeData[0] == newTimeDataComparator[0]
					&& newTimeData[1] >= newTimeDataComparator[1])
				throw new IllegalArgumentException(
						"End time must come after Start time");
		}
		this.data = parseTimeStringToString(newData);
		this.baseTime = newData;
		this.beenValidated = true;
	}

	/**
	 * Returns the 24 hour time string
	 * 
	 * @return
	 */
	public String getTime() {
		return this.baseTime;
	}

	/**
	 * Converts 24-hour time to 12-hour time
	 * 
	 * @param data
	 * @return
	 * @throws IllegalArgumentException
	 */
	private String parseTimeStringToString(String data)
			throws IllegalArgumentException {
		int[] timeData = parseTimeStringToArray(data);
		String meridian = " AM";
		int finalHour;
		if (timeData[0] == 0 || timeData[0] == 12) finalHour = 12;
		else finalHour = ((timeData[0] % 12));
		if (timeData[0] >= 11)
			meridian = " PM";
		return finalHour + ":" + timeData[1] + meridian;
	}

	/**
	 * Converts a 24 hour time string into an array of it's components
	 * 
	 * @param data
	 * @return
	 * @throws IllegalArgumentException
	 */
	private int[] parseTimeStringToArray(String data)
			throws IllegalArgumentException {
		int[] newTimeData = new int[] { -1, -1, };
		int i = 0;
		for (String calendarItem : data.split(":")) {
			try {
				newTimeData[i++] = Integer.parseInt(calendarItem);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(
						"Time String not well-formed");
			}
		}
		if (newTimeData[0] < 0 || newTimeData[0] > 23)
			throw new IllegalArgumentException("Hour outside of Range");
		if (newTimeData[1] < 0 || newTimeData[1] > 59)
			throw new IllegalArgumentException("Minute outside of Range");
		return newTimeData;
	}

	/**
	 * Sets the comparator used by setData If this is StartTime, set the
	 * comparator to EndTime
	 * 
	 * Do not set the comparator on EndTime
	 * 
	 * @param c
	 */
	public void setComparator(String c) {
		this.comparator = c;
	}
	
	public boolean checkSet()	{
		return this.beenSet;
	}
}
