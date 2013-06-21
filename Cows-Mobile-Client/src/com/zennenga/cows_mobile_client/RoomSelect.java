package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class RoomSelect extends Activity implements OnItemSelectedListener {
	
	public Spinner buildingSelectSpinner;
	public Spinner roomSelectSpinner;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_select);

		addListenerOnNextButton();
		addListenerOnBackButton();
		addListenerOnBuildingSelectSpinner();
		addListenerOnRoomSelectSpinner();
		
	}
	
	public void addListenerOnNextButton() {
		final Button nextButton = (Button) findViewById(R.id.nextButtonToScheduledEvents);
		nextButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent i = new Intent(v.getContext(), RoomEventView.class);
	    		startActivity(i);
	        }
	    });
	}
	
	public void addListenerOnBackButton() {
		final Button backButton = (Button) findViewById(R.id.backButtonToHomeScreen);
		backButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	finish();
	        }
	    });
	}
	
	public void addListenerOnBuildingSelectSpinner() {
		buildingSelectSpinner = (Spinner) findViewById(R.id.buildingSelectSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		R.array.buildingOptions, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buildingSelectSpinner.setAdapter(adapter);
		buildingSelectSpinner.setOnItemSelectedListener(new BuildingSelectOnItemSelectedListener());
    }
	
	public void addListenerOnRoomSelectSpinner() {
		roomSelectSpinner = (Spinner) findViewById(R.id.roomSelectSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		R.array.roomOptions, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		roomSelectSpinner.setAdapter(adapter);
		roomSelectSpinner.setOnItemSelectedListener(new RoomSelectOnItemSelectedListener());
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.room_select, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
