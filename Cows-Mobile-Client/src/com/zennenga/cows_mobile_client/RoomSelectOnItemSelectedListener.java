package com.zennenga.cows_mobile_client;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class RoomSelectOnItemSelectedListener implements OnItemSelectedListener {
	String roomSelectedString;
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		roomSelectedString =  parent.getItemAtPosition(pos).toString();
	}
	 
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		roomSelectedString = "None";
	}
	
	public String getRoomSelected() {
		return roomSelectedString;
	}

}