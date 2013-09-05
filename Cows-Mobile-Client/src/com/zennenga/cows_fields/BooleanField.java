package com.zennenga.cows_fields;

public class BooleanField extends BaseField {

	public BooleanField(String fieldName, String data) {
		this.fieldName = fieldName;
		this.data = data;
	}

	@Override
	public void setData(String newData) {
		this.beenValidated = false;
		if (!newData.equals("false") && !newData.equals("true"))
			throw new IllegalArgumentException(
					"Boolean fields must have a boolean value");
		this.data = newData;
		this.beenValidated = true;
	}
}
