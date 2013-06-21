package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		R.array.buildingOptions, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buildingSelectSpinner.setAdapter(adapter);
		buildingSelectSpinner.setOnItemSelectedListener(this);
    }
	
	public void addListenerOnRoomSelectSpinner() {
		roomSelectSpinner = (Spinner) findViewById(R.id.roomSelectSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		R.array.roomOptions, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		roomSelectSpinner.setAdapter(adapter);
		roomSelectSpinner.setOnItemSelectedListener(this);
    }
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.room_select, menu);
		return true;
	}

}
