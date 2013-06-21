package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class RoomSelect extends Activity {
	
	Spinner spin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_select);
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	Spinner spinner = (Spinner) findViewById(R.id.spinner2);
		    	//Set values to all room values n stuff for the given building
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
		        //Clear spinner
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.room_select, menu);
		return true;
	}

}
