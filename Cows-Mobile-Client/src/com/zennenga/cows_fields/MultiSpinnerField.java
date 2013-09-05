package com.zennenga.cows_fields;

import java.util.List;

import com.zennenga.cows_mobile_client.MultiSelectSpinner;
import com.zennenga.utility.Utility;

public class MultiSpinnerField extends BaseField {
	private MultiSelectSpinner spinner;
	private int arrayChoice;

	public MultiSpinnerField(int arrayChoice, boolean optional) {
		this.arrayChoice = arrayChoice;
		this.optional = optional;
	}

	/**
	 * Validates and sets the the field to the spinner's current value
	 * 
	 * newData is not used
	 */
	@Override
	public void setData(String newData) {
		if (this.spinner == null)
			throw new IllegalArgumentException(this.fieldName
					+ " spinner was not set");
		this.beenValidated = false;
		List<Integer> selected = this.spinner.getSelectedIndicies();
		if (selected.isEmpty() && !this.optional)
			throw new IllegalArgumentException(
					"You must select at least one option from "
							+ this.fieldName);
		for (Integer select : selected) {
			this.data += Utility.getAttr(select, this.arrayChoice,
					this.spinner.getContext())
					+ "&";
		}
		this.data = this.data.substring(0, this.data.length() - 1);
		this.beenValidated = true;
	}

	/**
	 * Adds a reference to the spinner this field refers to in order to get the
	 * selected item later
	 * 
	 * @param multiSpinner
	 */
	public void setSpinner(MultiSelectSpinner multiSpinner) {
		this.spinner = multiSpinner;
		this.fieldName = spinner.getTag().toString();
	}
}
