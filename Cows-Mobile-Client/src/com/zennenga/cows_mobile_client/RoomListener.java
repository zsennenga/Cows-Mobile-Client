package com.zennenga.cows_mobile_client;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemSelectedListener;

import com.zennenga.utility.Validator;

public class RoomListener implements OnItemSelectedListener {

	Validator v;
	public RoomListener() {
		this.v = Validator.getInstance();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int arg2,
			long arg3) {
		if (!parent.getSelectedItem().toString().contains("All "))
			this.v.setField("BuildingAndRoom", ":"
					+ parent.getSelectedItem().toString());
		updateButton((View) parent.getParent().getParent());
	}

	private void updateButton(View p) {
		Button b = (Button) p.findViewById(R.id.button1);
		b.setEnabled(false);
		if (v.checkFieldsValidation(false))
			b.setEnabled(true);
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		return;
	}

}
