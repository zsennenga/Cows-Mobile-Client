package com.zennenga.cows_mobile_client;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.zennenga.utility.Validator;

public class BuildingListener implements OnItemSelectedListener {

	Validator v;

	public BuildingListener() {
		this.v = Validator.getInstance();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String building = parent.getSelectedItem().toString();
		if (!building.contains("Tap to")) v.setField("BuildingAndRoom", building + ":");
		if (building.contains("215")) {
			populateSpinner(R.array.roomOptions215Create, (View) parent.getParent());
		} else if (building.contains("1590")) {
			populateSpinner(R.array.roomOptions1590Create, (View) parent.getParent());
		} else if (building.contains("1605")) {
			populateSpinner(R.array.roomOptions1605Create, (View) parent.getParent());
		} else if (building.contains("1715")) {
			populateSpinner(R.array.roomOptions1715Create, (View) parent.getParent());
		} else if (building.contains("Tap"))	{
			Spinner spinner = (Spinner) ((View)parent.getParent()).findViewById(R.id.roomSelectSpinner2);
			spinner.setAdapter(new ArrayAdapter<String>(((View)parent.getParent()).getContext(), 0));
			v.setField("BuildingAndRoom",":");
		}
		updateButton((View) parent.getParent().getParent().getParent());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		return;
	}
	
	private void updateButton(View p) {
		Button b = (Button) p.findViewById(R.id.button1);
		b.setEnabled(false);
		if (v.checkFieldsValidation(false))
			b.setEnabled(true);
	}

	private void populateSpinner(int arrayID, View v) {
		Spinner spinner = (Spinner) v.findViewById(R.id.roomSelectSpinner2);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				v.getContext(), arrayID, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

}
