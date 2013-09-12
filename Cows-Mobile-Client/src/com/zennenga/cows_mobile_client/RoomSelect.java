package com.zennenga.cows_mobile_client;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

public class RoomSelect extends FragmentActivity {

	private Spinner buildingSelectSpinner;
	private Spinner roomSelectSpinner;
	private DatePicker datePicker;
	public int day, year, month;
	public String timeSelected;
	BuildingSelectOnItemSelectedListener buildingSelectListener;
	RoomSelectOnItemSelectedListener roomSelectListener;
	final static int TIME_DIALOG_ID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_select);

		addListenerOnNextButton();
		addListenerOnBackButton();
		addListenerOnAvailableRoomButton();
		addListenerOnRoomSelectSpinner();
		addListenerOnBuildingSelectSpinner();	
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
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), RoomEventView.class);
				getDateFromDatePicker();
				String roomCode = getRoomCode(roomSelectListener.getRoomSelected());
				i.putExtra("day", day);
				i.putExtra("month", month + 1); // month returned from picker
												// starts at 0
				i.putExtra("year", year);
				i.putExtra("roomCode", roomCode);
				startActivity(i);
			}
		});
	}

	public void addListenerOnBackButton() {
		final Button backButton = (Button) findViewById(R.id.backButtonToHomeScreen);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	public void addListenerOnAvailableRoomButton() {
		final Button availableButton = (Button) findViewById(R.id.nextAvailableTime);
		availableButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presentDialog();
			}
		});
	}
	
	public void presentDialog() {
		final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
		    public void onClick(DialogInterface dialog, int yesNo) {
		        switch (yesNo){
		        case DialogInterface.BUTTON_POSITIVE:
		        	dialogReceiver(1);
		            break;
		        case DialogInterface.BUTTON_NEGATIVE:
					dialogReceiver(0);
		            break;
		        }
		    }
		};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Is there a specific time you would like to check for availability?").setPositiveButton("Yes", dialogClickListener)
		    .setNegativeButton("No", dialogClickListener).show();
	}
	
	public void dialogReceiver(int yesNo) {
		Intent i = new Intent(getApplicationContext(), RoomAvailableTime.class);
		Handler h = new Handler(){
	        @Override
	        public void handleMessage(Message msg){
	            timeSelected = msg.getData().getString("timeChosen");
	            dialogReceiver(2);
	        }
	    };
		getDateFromDatePicker();
		String roomCode = getRoomCode(roomSelectListener.getRoomSelected());
		i.putExtra("day", day);
		i.putExtra("month", month + 1);
		i.putExtra("year", year);
		i.putExtra("roomCode", roomCode);
		
		switch (yesNo) {
		case 2:
			i.putExtra("timeChosen", Integer.valueOf(timeSelected));
			startActivity(i);
			break;
		case 1:
			DialogFragment timePickerDialogListener = new TimePickerDialogOnAvailableListener(h);
			timePickerDialogListener.show(getFragmentManager(), "timePicker");
			break;
		case 0:
			startActivity(i);
			break;
		}
	}
	
	public void addListenerOnBuildingSelectSpinner() {
		buildingSelectListener = new BuildingSelectOnItemSelectedListener();
		buildingSelectListener.setRoomSelectListener(roomSelectListener);
		buildingSelectListener.setRoomSpinner(roomSelectSpinner);
		buildingSelectListener.setRoomContext(this);

		buildingSelectSpinner = (Spinner) findViewById(R.id.buildingSelectSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.buildingOptions,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buildingSelectSpinner.setAdapter(adapter);
		buildingSelectSpinner.setOnItemSelectedListener(buildingSelectListener);
	}

	public void addListenerOnRoomSelectSpinner() {
		// Sets a listener on room selct spinner, but buildingSelectListener
		// also has control
		roomSelectListener = new RoomSelectOnItemSelectedListener();
		roomSelectSpinner = (Spinner) findViewById(R.id.roomSelectSpinner);
		// The adapter would be here but it gets set in building based on
		// what builing was selected
		roomSelectSpinner.setOnItemSelectedListener(roomSelectListener);
	}

	public String getRoomCode(String str) {
		if (str.contains("1142"))
			return "1590_Tilia!1142";
		else if (str.contains("1103"))
			return "1605_Tilia!1103";
		else if (str.contains("1111"))
			return "1605_Tilia!1111";
		else if (str.contains("1162"))
			return "1605_Tilia!1162";
		else if (str.contains("1106"))
			return "1715_Tilia!1106";
		else if (str.contains("1119"))
			return "1715_Tilia!1119";
		else if (str.contains("1121"))
			return "1715_Tilia!1121";
		else if (str.contains("1123"))
			return "1715_Tilia!1123";
		else if (str.contains("1161"))
			return "1715_Tilia!1161";
		else if (str.contains("1104"))
			return "215_Sage!1104";
		else if (str.contains("1113"))
			return "215_Sage!1113";
		// Codes for selecting all rooms in a building
		else if (str.contains("1605"))
			return "1605_Tilia";
		else if (str.contains("1715"))
			return "1715_Tilia";
		else if (str.contains("215"))
			return "215_Sage";
		// No Code then select all events in all buildings
		return "";
	}
}
