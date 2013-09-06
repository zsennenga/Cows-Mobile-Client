package com.zennenga.cows_fields;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class BaseField {
	protected String fieldName;
	protected String data = "";
	protected Boolean beenValidated = false;
	protected Boolean optional = false;

	/**
	 * Validates the data in a method relevant to the field and sets the data
	 * attribute to the string newData
	 * 
	 * @param Value
	 *            to set
	 */
	public abstract void setData(String newData);

	/**
	 * Converts a field name and a value to a GET parameter
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getData() throws UnsupportedEncodingException {
		return "&" + this.fieldName + "=" + URLEncoder.encode(data, "UTF-8");
	}

	/**
	 * Makes sure that the field has been validated if it isn't optional
	 * 
	 * @return If the field has been validated
	 */
	public Boolean checkValidation() {
		return this.beenValidated || this.optional;
	}

	/**
	 * 
	 */
	public String getFieldName() {
		return this.fieldName;
	}

	public String getRawData() {
		return this.data;
	}
	public void setOptional(boolean b)	{
		this.optional = b;
	}
}
