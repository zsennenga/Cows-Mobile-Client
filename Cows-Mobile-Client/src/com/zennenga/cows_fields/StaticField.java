package com.zennenga.cows_fields;

public class StaticField extends BaseField {
	
	public StaticField(String field, String data)	{
		this.data = data;
		this.fieldName = field;
	}
	
	@Override
	public void setData(String newData) {
		this.data = newData;
	}
}
