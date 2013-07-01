package com.zennenga.cows_fields;

import java.util.List;

import com.zennenga.cows_mobile_client.MultiSelectSpinner;
import com.zennenga.utility.Utility;

public class MultiSpinnerField extends BaseField {
	private boolean optional = false;
	private MultiSelectSpinner spinner;
	private int arrayChoice;
	
	public MultiSpinnerField(MultiSelectSpinner s, int arrayChoice, boolean optional)	{
		this.fieldName = spinner.getTag().toString();
		this.arrayChoice = arrayChoice;
		this.spinner = s;
		this.optional = optional;
	}
	
	@Override
	public void setData(String newData) {
		List<Integer> selected = this.spinner.getSelectedIndicies();
		if (selected.size() == 0 && !this.optional) throw new IllegalArgumentException("You must select at least one option from " + this.fieldName);
		for (Integer select : selected)	{
			this.data += Utility.getAttr(select,this.arrayChoice, this.spinner.getContext()) + "&";
		}
		this.data = this.data.substring(0,this.data.length()-1);
	}
	public void setSpinner(MultiSelectSpinner multiSpinner) {
		this.spinner = multiSpinner;
	}
}
