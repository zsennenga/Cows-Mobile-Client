package com.zennenga.cows_fields;

import android.widget.Spinner;

import com.zennenga.utility.Utility;

public class SpinnerField extends BaseField {
	private int arrayChoice;
	private Spinner spinner;

	public SpinnerField(int arrayChoice) {
		this.arrayChoice = arrayChoice;
	}

	/**
	 * Set a reference to the spinner this field refers to. Must be executed
	 * before setData
	 * 
	 * @param s
	 */
	public void setSpinner(Spinner s) {
		this.fieldName = s.getTag().toString();
		this.spinner = s;
	}

	/**
	 * Checks that an item was selected from the spinner and sets the value if
	 * it was set.
	 */
	@Override
	public void setData(String newData) throws IllegalArgumentException {
		if (this.spinner == null)
			throw new IllegalArgumentException(this.fieldName
					+ " spinner was not set");
		this.beenValidated = false;
		if (this.spinner.getSelectedItem() == null)
			throw new IllegalArgumentException(this.fieldName
					+ " is not optional");
		this.data = Utility.getAttr(this.spinner.getSelectedItemPosition(),
				this.arrayChoice, this.spinner.getContext());
		this.beenValidated = true;
	}
}
