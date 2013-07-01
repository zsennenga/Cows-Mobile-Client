package com.zennenga.cows_fields;

import com.zennenga.utility.Utility;

import android.widget.Spinner;

public class SpinnerField extends BaseField {
	private int arrayChoice;
	private Spinner spinner;

	public SpinnerField(Spinner s, int arrayChoice)	{
		this.fieldName = s.getTag().toString();
		this.arrayChoice = arrayChoice;
		this.spinner = s;
	}
	
	public void setSpinner(Spinner s)	{
		this.spinner = s;
	}
	
	@Override
	public void setData(String newData)  throws IllegalArgumentException {
		if (this.spinner.getSelectedItem() == null) throw new IllegalArgumentException(this.fieldName + " is not optional");
		this.data = Utility.getAttr(this.spinner.getSelectedItemPosition(), this.arrayChoice, this.spinner.getContext());
		this.beenValidated = true;
	}
}
