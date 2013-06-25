package com.zennenga.cows_mobile_client;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class BuildingSelectOnItemSelectedListener implements OnItemSelectedListener {
	RoomSelectOnItemSelectedListener roomSelectListener;
	Context roomContext;
	String buildingSelectedString;
	Spinner roomSelectSpinner;
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
		ArrayAdapter<CharSequence> adapter;
		buildingSelectedString = parent.getItemAtPosition(pos).toString();
		
		//Change the room spinner based on building selected
		if (buildingSelectedString.equals("1590 Tilia")) 
			adapter = ArrayAdapter.createFromResource(roomContext, 
					R.array.roomOptions1590, android.R.layout.simple_spinner_item);
		else if (buildingSelectedString.equals("1605 Tilia")) 
			adapter = ArrayAdapter.createFromResource(roomContext, 
					R.array.roomOptions1605, android.R.layout.simple_spinner_item);
		else if (buildingSelectedString.equals("1715 Tilia")) 
			adapter = ArrayAdapter.createFromResource(roomContext, 
					R.array.roomOptions1715, android.R.layout.simple_spinner_item);
		else if (buildingSelectedString.equals("215 Sage")) 
			adapter = ArrayAdapter.createFromResource(roomContext, 
					R.array.roomOptions215, android.R.layout.simple_spinner_item);
		else 
			adapter = ArrayAdapter.createFromResource(roomContext, 
					R.array.roomOptions, android.R.layout.simple_spinner_item);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		roomSelectSpinner.setAdapter(adapter);
	}
	 
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		buildingSelectedString = "None";
	}

	public void setRoomSelectListener(RoomSelectOnItemSelectedListener roomSelectListenerSet) {
		roomSelectListener = roomSelectListenerSet;
	}
	
	public void setRoomContext(Context c) {
		roomContext = c;
	}
	
	public void setRoomSpinner(Spinner roomSpinner) {
		roomSelectSpinner = roomSpinner;
	}
	
	public String getBuildingSelected() {
		return buildingSelectedString;
	}
}