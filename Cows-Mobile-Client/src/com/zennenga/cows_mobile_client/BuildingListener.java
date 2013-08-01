package com.zennenga.cows_mobile_client;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.zennenga.utility.Validator;

public class BuildingListener implements OnItemSelectedListener {

	Validator v;

	BuildingListener(Validator getValidator) {
		this.v = getValidator;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String building = parent.getSelectedItem().toString();
		v.setField("BuildingAndRoom", building + ":");
		if (building.contains("215")) {
			populateSpinner(R.id.roomSelectSpinner2, R.array.roomOptions215,
					(View) parent.getParent());
		} else if (building.contains("1590")) {
			populateSpinner(R.id.roomSelectSpinner2, R.array.roomOptions1590,
					(View) parent.getParent());
		} else if (building.contains("1605")) {
			populateSpinner(R.id.roomSelectSpinner2, R.array.roomOptions1605,
					(View) parent.getParent());
		} else if (building.contains("1715")) {
			populateSpinner(R.id.roomSelectSpinner2, R.array.roomOptions1715,
					(View) parent.getParent());
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		return;
	}

	private void populateSpinner(int fieldID, int arrayID, View v) {
		Spinner spinner = (Spinner) v.findViewById(fieldID);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				v.getContext(), arrayID, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

}
