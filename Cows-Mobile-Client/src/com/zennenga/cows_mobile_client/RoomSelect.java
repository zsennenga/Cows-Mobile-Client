package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class RoomSelect extends Activity implements OnItemSelectedListener {
	
	public Spinner buildingSelectSpinner;
	public Spinner roomSelectSpinner;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_select);

		addListenerOnBuildingSelectSpinner();
		
	}
	
	public void addListenerOnBuildingSelectSpinner() {
		  buildingSelectSpinner = (Spinner) findViewById(R.id.buildingSelectSpinner);
		  buildingSelectSpinner.setOnItemSelectedListener(this);
    }
	
	public void addListenerOnRoomSelectSpinner() {
		  roomSelectSpinner = (Spinner) findViewById(R.id.roomSelectSpinner);
		  roomSelectSpinner.setOnItemSelectedListener(this);
  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.room_select, menu);
		return true;
	}

}
