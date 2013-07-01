package com.zennenga.utility;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;

import com.zennenga.cows_fields.BaseField;

public class Validator {
	private HashMap<String,BaseField> fieldMap;
	
	public Validator()	{
		this.fieldMap = new HashMap<String,BaseField>();
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
