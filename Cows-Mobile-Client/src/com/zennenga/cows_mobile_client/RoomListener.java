package com.zennenga.cows_mobile_client;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.zennenga.utility.Validator;

public class RoomListener implements OnItemSelectedListener {

	Validator v;

	public RoomListener(Validator v) {
		this.v = v;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View arg1, int arg2,
			long arg3) {
		if (!parent.getSelectedItem().toString().contains("All "))
			this.v.setField("BuildingAndRoom", ":"
					+ parent.getSelectedItem().toString());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		return;
	}

}
