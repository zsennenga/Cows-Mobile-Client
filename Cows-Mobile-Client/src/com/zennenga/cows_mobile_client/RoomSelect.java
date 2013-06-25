package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

public class RoomSelect extends Activity {
	
	private Spinner buildingSelectSpinner;
	private Spinner roomSelectSpinner;
	private DatePicker datePicker;
	public int day, year, month;
	BuildingSelectOnItemSelectedListener buildingSelectListener;
	RoomSelectOnItemSelectedListener roomSelectListener;
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_select);

		addListenerOnNextButton();
		addListenerOnBackButton();
		addListenerOnRoomSelectSpinner();
		addListenerOnBuildingSelectSpinner();
		getDateFromDatePicker();
	}
	
	public void getDateFromDatePicker() {
		datePicker = (DatePicker) findViewById(R.id.eventDatePicker);
		day = datePicker.getDayOfMonth();
		month = datePicker.getMonth();
		year = datePicker.getYear();
	}
	
	public void addListenerOnNextButton() {
		final Button nextButton = (Button) findViewById(R.id.nextButtonToScheduledEvents);
		nextButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	Intent i = new Intent(v.getContext(), RoomEventView.class);
	        	i.putExtra("day", day);
	        	i.putExtra("month", month);
	        	i.putExtra("year", year);
	        	i.putExtra("building", buildingSelectListener.getBuildingSelected());
	        	i.putExtra("room", roomSelectListener.getRoomSelected());
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
		buildingSelectListener = new BuildingSelectOnItemSelectedListener();
		buildingSelectListener.setRoomSelectListener(roomSelectListener);
		buildingSelectListener.setRoomSpinner(roomSelectSpinner);
		buildingSelectListener.setRoomContext(this);
		
		buildingSelectSpinner = (Spinner) findViewById(R.id.buildingSelectSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		R.array.buildingOptions, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buildingSelectSpinner.setAdapter(adapter);
		buildingSelectSpinner.setOnItemSelectedListener(buildingSelectListener);
    }
	
	public void addListenerOnRoomSelectSpinner() {
		//Sets a listener on room selct spinner, but buildingSelectListener also has control
		roomSelectListener = new RoomSelectOnItemSelectedListener();
		roomSelectSpinner = (Spinner) findViewById(R.id.roomSelectSpinner);
		//The adapter would be here but it gets set in building based on
		//what builing was selected
		roomSelectSpinner.setOnItemSelectedListener(roomSelectListener);
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.room_select, menu);
		return true;
	}

}
