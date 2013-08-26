//Taken from https://github.com/wongk/MultiSelectSpinner
/*
 * Copyright (C) 2012 Kris Wong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zennenga.cows_mobile_client;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.zennenga.utility.Utility;
import com.zennenga.utility.Validator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * A Spinner view that does not dismiss the dialog displayed when the control is
 * "dropped down" and the user presses it. This allows for the selection of more
 * than one option.
 */
public class MultiSelectSpinner extends Spinner implements
		OnMultiChoiceClickListener {
	String[] _items = null;
	boolean[] _selection = null;

	ArrayAdapter<String> _proxyAdapter;
	private String field;
	private EventCreation e;

	/**
	 * Constructor for use when instantiating directly.
	 * 
	 * @param context
	 */
	public MultiSelectSpinner(Context context) {
		super(context);

		_proxyAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item);
		super.setAdapter(_proxyAdapter);
	}

	/**
	 * Constructor used by the layout inflater.
	 * 
	 * @param context
	 * @param attrs
	 */
	public MultiSelectSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);

		_proxyAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item);
		super.setAdapter(_proxyAdapter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		
		if (_selection != null && which < _selection.length) {
			_selection[which] = isChecked;
			
			_proxyAdapter.clear();
			_proxyAdapter.add(buildSelectedItemString());
			setSelection(0);

			String s = String.valueOf(isChecked);
			if (Utility.isRecurrenceNow)	{
				switch (which) {
				case 0:
					Validator.getInstance()
							.setField("RecurrenceMonday", s);
					break;
				case 1:
					Validator.getInstance().setField("RecurrenceTuesday", s);
					break;
				case 2:
					Validator.getInstance().setField("RecurrenceWednesday", s);
					break;
				case 3:
					Validator.getInstance().setField("RecurrenceThursday", s);
					break;
				case 4:
					Validator.getInstance()
							.setField("RecurrenceFriday", s);
					break;
				}
			}
			else	{
				Validator.getInstance().setField(field, "");
				e.updateButton();
			}
		} else {
			throw new IllegalArgumentException(
					"Argument 'which' is out of bounds.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean performClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMultiChoiceItems(_items, _selection, this);
		builder.show().setCanceledOnTouchOutside(true);
		return true;
	}

	/**
	 * MultiSelectSpinner does not support setting an adapter. This will throw
	 * an exception.
	 * 
	 * @param adapter
	 */
	@Override
	public void setAdapter(SpinnerAdapter adapter) {
		throw new RuntimeException(
				"setAdapter is not supported by MultiSelectSpinner.");
	}

	/**
	 * Sets the options for this spinner.
	 * 
	 * @param items
	 */
	public void setItems(String[] items) {
		_items = items;
		_selection = new boolean[_items.length];

		Arrays.fill(_selection, false);
	}

	/**
	 * Sets the options for this spinner.
	 * 
	 * @param items
	 */
	public void setItems(List<String> items) {
		_items = items.toArray(new String[items.size()]);
		_selection = new boolean[_items.length];

		Arrays.fill(_selection, false);
	}

	/**
	 * Sets the selected options based on an array of string.
	 * 
	 * @param selection
	 */
	public void setSelection(String[] selection) {
		for (String sel : selection) {
			for (int j = 0; j < _items.length; ++j) {
				if (_items[j].equals(sel)) {
					_selection[j] = true;
				}
			}
		}
	}

	/**
	 * Sets the selected options based on a list of string.
	 * 
	 * @param selection
	 */
	public void setSelection(List<String> selection) {
		for (String sel : selection) {
			for (int j = 0; j < _items.length; ++j) {
				if (_items[j].equals(sel)) {
					_selection[j] = true;
				}
			}
		}
	}

	/**
	 * Sets the selected options based on an array of positions.
	 * 
	 * @param selectedIndicies
	 */
	public void setSelection(int[] selectedIndicies) {
		for (int index : selectedIndicies) {
			if (index >= 0 && index < _selection.length) {
				_selection[index] = true;
			} else {
				throw new IllegalArgumentException("Index " + index
						+ " is out of bounds.");
			}
		}
		_proxyAdapter.clear();
		_proxyAdapter.add(buildSelectedItemString());
		setSelection(0);

	}

	/**
	 * Returns a list of strings, one for each selected item.
	 * 
	 * @return
	 */
	public List<String> getSelectedStrings() {
		List<String> selection = new LinkedList<String>();
		for (int i = 0; i < _items.length; ++i) {
			if (_selection[i]) {
				selection.add(_items[i]);
			}
		}
		return selection;
	}

	/**
	 * Returns a list of positions, one for each selected item.
	 * 
	 * @return
	 */
	public List<Integer> getSelectedIndicies() {
		List<Integer> selection = new LinkedList<Integer>();
		for (int i = 0; i < _items.length; ++i) {
			if (_selection[i]) {
				selection.add(i);
			}
		}
		return selection;
	}

	/**
	 * Builds the string for display in the spinner.
	 * 
	 * @return comma-separated list of selected items
	 */
	private String buildSelectedItemString() {
		StringBuilder sb = new StringBuilder();
		boolean foundOne = false;

		for (int i = 0; i < _items.length; ++i) {
			if (_selection[i]) {
				if (foundOne) {
					sb.append(", ");
				}
				foundOne = true;

				sb.append(_items[i]);
			}
		}

		return sb.toString();
	}
	
	public void setField(String field, EventCreation e)	{
		this.field = field;
		this.e = e;
	}
	
}