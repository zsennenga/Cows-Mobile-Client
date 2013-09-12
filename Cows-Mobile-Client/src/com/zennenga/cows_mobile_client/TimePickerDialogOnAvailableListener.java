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
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerDialogOnAvailableListener extends DialogFragment
		implements TimePickerDialog.OnTimeSetListener {
	Handler h;
	
	public TimePickerDialogOnAvailableListener(Handler hFromActivity) {
		h = hFromActivity;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
		tpd.setTitle("Set time");
		return tpd;
	}
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Bundle data = new Bundle();
		Message msg = new Message();
		
		int timeChosenIn24 = (hourOfDay * 100) + minute;
		data.putString("timeChosen", String.valueOf(timeChosenIn24));
		msg.setData(data);
		h.sendMessage(msg);
	}
}


