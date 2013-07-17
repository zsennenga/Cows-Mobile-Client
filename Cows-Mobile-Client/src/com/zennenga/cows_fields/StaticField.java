package com.zennenga.cows_fields;

public class StaticField extends BaseField {
	
	public StaticField(String field, String data)	{
		this.data = data;
		this.fieldName = field;
		this.beenValidated = true;
	}
	/**
	 * Performs no validation
	 */
	@Override
	public void setData(String newData) {
		this.beenValidated = false;
		this.data = newData;
		this.beenValidated = true;
	}
}
