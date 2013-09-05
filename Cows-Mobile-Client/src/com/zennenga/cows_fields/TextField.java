package com.zennenga.cows_fields;

public class TextField extends BaseField {

	public TextField(String fieldName, String data, boolean optional) {
		this.fieldName = fieldName;
		this.data = data;
		this.optional = optional;
	}

	/**
	 * Checks that the string is not blank and if the field isn't optional
	 */
	@Override
	public void setData(String newData) throws IllegalArgumentException {
		this.beenValidated = false;
		if (newData.length() == 0 && !this.optional) {
			throw new IllegalArgumentException(this.fieldName
					+ " may not be blank");
		}
		this.data = newData;
		this.beenValidated = true;
	}
}
