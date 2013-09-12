package com.zennenga.cows_mobile_client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zennenga.utility.Utility;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class RoomAvailableTime extends Activity {
	
	private int day, month, year, timeChosen;	
	private String roomCode;
	private static final int TIMEOUT_MILLISEC = 1000;
	private final String[] roomNums = {"1590 Tilia, Room 1142", "1605 Tilia, Room 1103", "1605 Tilia, Room 1111", "1605 Tilia, Room 1162", 
			"1715 Tilia, Room 1106", "1715 Tilia, Room 1119", "1715 Tilia, Room 1121", "1715 Tilia, Room 1123", "1715 Tilia, Room 1161", 
			"215 Sage, Room 1104", "215 Sage, Room 1113"},
	rooms1605 = {"1605 Tilia, Room 1103", "1605 Tilia, Room 1111", "1605 Tilia, Room 1162"},
	rooms1715 = {"1715 Tilia, Room 1106", "1715 Tilia, Room 1119", "1715 Tilia, Room 1121", "1715 Tilia, Room 1123", "1715 Tilia, Room 1161"},
	rooms215 = {"215 Sage, Room 1104", "215 Sage, Room 1113"},
	rooms1590 = {"1590 Tilia, Room 1142"};
	
	public static final String PM = "P";
	
	private Handler mHandler = new Handler();
	private TimerTask timerTask = new ATimerTask();
	private Timer timer = new Timer();
	
	private class ATimerTask extends TimerTask {
	    public void run() {
	        mHandler.post(
	        new Runnable() { 
	        	public void run() { 
	        		new nextAvailableRoomTime().execute();
	                }
	        	}
	        );
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_available_time);
		getData();
		addListenerOnBackButton();
		addListenerOnCreateButton();
		timer.scheduleAtFixedRate(timerTask, 0, 1000*60);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	
	//public void getEvents() {
	//	nextAvailableRoomTime a = new nextAvailableRoomTime();
	//	a.execute();
	//}
	
	private void getData() {
		day = getIntent().getExtras().getInt("day");
		month = getIntent().getExtras().getInt("month");
		year = getIntent().getExtras().getInt("year");
		timeChosen = getIntent().getExtras().getInt("timeChosen");
		roomCode = getIntent().getExtras().getString("roomCode");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.room_available_time, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void addListenerOnCreateButton() {
		final Button backButton = (Button) findViewById(R.id.createEventButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				Intent i = new Intent(v.getContext(), CasAuth.class);
				timer.cancel();
				finish();
				startActivity(i);
			}
		});
	}
	
	public void addListenerOnBackButton() {
		final Button backButton = (Button) findViewById(R.id.BackButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				timer.cancel();
				finish();
			}
		});
	}
	
	public boolean isNumeric(String s) {
		return s.matches("\\d");
	}
	
	public int toMinutes(int timeIn24) {	
		int minutes = timeIn24 % 100;
		timeIn24 -= minutes;
		minutes += (timeIn24/100)*60;
		return minutes; 
	}
	
	public String toTwelveHourTime(int timeIn24) {
		int hour = 0, minutes = 0;
		String ampm = "AM", timeIn12;
		minutes = timeIn24%100;
		hour = (timeIn24-minutes)/100;
		if (hour >= 12) {
			if (hour == 12) {}
			else hour -= 12;
			ampm = "PM";
		}
		if (minutes < 10) timeIn12 = String.valueOf(hour)+":0"+String.valueOf(minutes)+" "+ampm ;
		else timeIn12 = String.valueOf(hour)+":"+String.valueOf(minutes)+" "+ampm; 
		return timeIn12; 
	}
	  
	private class nextAvailableRoomTime extends AsyncTask<String, String, String> {
		
		@Override
		protected String doInBackground(String... params) {
			String date = month + "/" + day + "/" + year;
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			
			HttpParams p = new BasicHttpParams();
			p.setParameter("user", "1");

			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient(p);

			String url = "http://dev.its.ucdavis.edu/scripts/CowsTabletServer.php"
					+ "?date="
					+ date
					+ "&bldgRoom="
					+ roomCode
					+ "&siteId=" + Utility.SITE_ID;
			Log.i("Ted", url);
			HttpGet httpGet = new HttpGet(url);

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = "";
			try {
				responseBody = httpclient.execute(httpGet, responseHandler);
			} catch (ClientProtocolException e) {
				Utility.showMessage("Unable to execute http request. Check that network is enabled.");
				e.printStackTrace();
			} catch (IOException e) {
				Utility.showMessage("Unable to execute http request. Check that network is enabled.");
				e.printStackTrace();
			}
			return responseBody;
		}//credits to Brandon
		
		@Override
		protected void onPostExecute(String line) {
			int 	current24Time = 0,
					currentMinutes = 0,
					startMinutes = 0,
					x = 0, 
					indexOfColon = 0,
					rooms = 0; 		
			String info = new String();
			TextView 	roomTextView = (TextView) findViewById(R.id.rowRoom1),
						availTextView = (TextView) findViewById(R.id.rowAvail1),
						descripTextView = (TextView) findViewById(R.id.rowDescrip1),
						titleTextView = (TextView) findViewById(R.id.titleOfActivity);
			
			HashMap<String, Integer> availableRooms = new HashMap<String, Integer> ();
			JSONArray json = null;
						
			//line = "[{\"Title\":\"Test\",\"Time\":\" 1:00 PM - 2:00 PM\",\"Location\":\"1605 Tilia, Room 1162\"},"
			//		+ "{\"Title\":\"Test3\",\"Time\":\" 12:00 PM - 1:07 PM\",\"Location\":\"1605 Tilia, Room 1111\"},"
			//		+ "{\"Title\":\"Test5\",\"Time\":\" 11:00 AM - 1:00 PM\",\"Location\":\"1605 Tilia, Room 1103\"},"
			//		+ "{\"Title\":\"Test5\",\"Time\":\" 1:00 PM - 2:30 PM\",\"Location\":\"1605 Tilia, Room 1103\"},"
			//		+ "{\"Title\":\"Test5\",\"Time\":\" 2:30 PM - 4:00 PM\",\"Location\":\"1605 Tilia, Room 1103\"}]";
			//for testing
			Time currentTime = new Time();
			currentTime.setToNow();
			current24Time += (currentTime.hour*100)+currentTime.minute;	//8:47PM = 2047 and 12:30PM = 1230
			currentMinutes = toMinutes(current24Time);
			
			if (timeChosen != current24Time && timeChosen != 0) {
				current24Time = timeChosen;
				currentMinutes = toMinutes(timeChosen);
			}
			//current24Time = 1320;//for testing
			//currentMinutes = 800;			
			titleTextView.setText("Availability ("+month+"-"+day+"-"+year+") "+toTwelveHourTime(current24Time));
			
			try {
				json = new JSONArray(line);
			} catch (JSONException e) {
				Utility.showMessage("Invalid response. Check that network is enabled");
				e.printStackTrace();
				return;
			}//check that line is a JSONarray
			JSONObject obj;						
			while (x < 30) {//30 is a random #, represents max number of meetings in a day, most likely not going to be over 30
				try {
					obj = json.getJSONObject(x);						
					info = info.concat(obj.getString("Time")+","+obj.getString("Location")+",");			
				} catch (JSONException e) {
					break;
				}
				x++;
			}//store jsonarray time and location into a string
			//string to hashmap, implemented this way so it will minimize future debugging
			String delims = "[,]+";
			String[] tokens = info.split(delims);//split string info into an array tokens[0] = time, token[1and2]=location, so on
			
			delims = "[_!]+";
			String[] roomsToCheck = roomCode.split(delims);
			String[] selectedRooms;
			int selectedBuilding = 0, selectedRoom = 0;
			
			if (roomsToCheck.length > 1) {
				try {
					selectedBuilding = Integer.valueOf(roomsToCheck[0]);					
				}
				catch (NumberFormatException e) {
					Utility.showMessage("Error: Building selected not identified");
					return;
				}
			}
			if (roomsToCheck.length > 2) {
				try {
					selectedRoom = Integer.valueOf(roomsToCheck[2]);
				}
				catch (NumberFormatException e) {
					Utility.showMessage("Error: Room selected not identified");
					return;
				}
			}
			
			switch (selectedBuilding) {
			case 215:
				if (selectedRoom != 0) {
					selectedRooms = new String[1];
					String s = "215 Sage, Room "+String.valueOf(selectedRoom);
					selectedRooms[0] = s;
				}
				else {
					selectedRooms = new String[rooms215.length];
					System.arraycopy(rooms215,0,selectedRooms,0,rooms215.length);
				}
				break;
			case 1590:
				if (selectedRoom != 0) {
					selectedRooms = new String[1];
					String s = "1590 Tilia, Room "+String.valueOf(selectedRoom);
					selectedRooms[0] = s;
				}
				else {
					selectedRooms = new String[rooms1590.length];
					System.arraycopy(rooms1590,0,selectedRooms,0,rooms1590.length);
				}
				break;
			case 1605:
				if (selectedRoom != 0) {
					selectedRooms = new String[1];
					String s = "1605 Tilia, Room "+String.valueOf(selectedRoom);
					selectedRooms[0] = s;
				}
				else {
					selectedRooms = new String[rooms1605.length];
					System.arraycopy(rooms1605,0,selectedRooms,0,rooms1605.length);
				}
				break;
			case 1715:
				if (selectedRoom != 0) {
					selectedRooms = new String[1];
					String s = "1715 Tilia, Room "+String.valueOf(selectedRoom);
					selectedRooms[0] = s;
				}
				else {
					selectedRooms = new String[rooms1715.length];
					System.arraycopy(rooms1715,0,selectedRooms,0,rooms1715.length);
				}
				break;
			default:
				selectedRooms = new String[roomNums.length];
				System.arraycopy(roomNums,0,selectedRooms,0,roomNums.length);
				break;
			}//determines which room(s) you want shown
			
			if (year<currentTime.year || (year==currentTime.year && month<(currentTime.month+1)) 
					|| (year==currentTime.year && month==(currentTime.month+1) && day<currentTime.monthDay)) {
				Utility.showMessage("Date picked is in the past");
			}
			else if (tokens.length > 2) {
				for (x=0; x < tokens.length; x+=3) {//increments of 3
					int startTime = 0,
						endTime = 0;					
					String RoomNum = tokens[x+1].concat(","+tokens[x+2]);//location
					
					if (availableRooms.get(RoomNum) == null) {
						availableRooms.put(RoomNum, 0);
					}//store new rooms into hashmap, all rooms start out available then set not available later
					
					while (true) {
						indexOfColon = tokens[x].indexOf(":");					
						if (isNumeric(String.valueOf(tokens[x].charAt(indexOfColon-1))) && //check if chars next to the colon are numbers
								isNumeric(String.valueOf(tokens[x].charAt(indexOfColon+1)))) {
							if (startTime == 0) {
								startTime = Integer.valueOf((tokens[x].substring(indexOfColon-2,indexOfColon)).trim());
								startTime *= 100;
								startMinutes = Integer.valueOf((tokens[x].substring(indexOfColon-2,indexOfColon)).trim())*60;
								startTime += Integer.valueOf((tokens[x].substring(indexOfColon+1,indexOfColon+3)).trim());
								startMinutes += Integer.valueOf((tokens[x].substring(indexOfColon+1,indexOfColon+3)).trim());
								if (String.valueOf(tokens[x].charAt(indexOfColon+4)).equals(PM) &&
										startTime < 1200) {						
										startTime += 1200;
										startMinutes += 720; 
								}//checking if AM or PM
							}//convert time into 24hour format and store into 2 vars for start and end of meeting
							else {
								endTime = Integer.valueOf((tokens[x].substring(indexOfColon-2,indexOfColon)).trim());
								endTime *= 100;
								endTime += Integer.valueOf((tokens[x].substring(indexOfColon+1,indexOfColon+3)).trim());
								if (String.valueOf(tokens[x].charAt(indexOfColon+4)).equals(PM) &&
										endTime < 1200) {
									endTime += 1200;	
								}
								break;
							}//convert strings into 24hour time XXXX format
						}
						StringBuilder sb = new StringBuilder(tokens[x]);
						sb.deleteCharAt(indexOfColon);
						tokens[x] = sb.toString();//delete the colon 
					}			
					if (endTime <= current24Time) {
						continue;
					}//disregard the meeting just read since it is after the current time, continue				
					else if ((startTime <= current24Time) && (current24Time < endTime)) {
						availableRooms.put(RoomNum,0-endTime);					
					}//a meeting is currently going on, put negative time to indicate room not available until that time
					else if (current24Time < startTime) {									
						if (availableRooms.get(RoomNum) > 0) {
							continue;
						}//already knows how long until next meeting, disregard
						else if (availableRooms.get(RoomNum) < 0) {
							if (-(toMinutes(availableRooms.get(RoomNum))) >= startMinutes) {
								availableRooms.put(RoomNum,0-endTime);
							}						
						}//if room in question already has a negative number, check if next meeting is back2back
						else if (availableRooms.get(RoomNum) == 0) {
							availableRooms.put(RoomNum, startMinutes-currentMinutes);
						}//Room is available right now, store [for how long]
					}				
				}//End organizing events
			}//if tokens length is < 3, then there were no scheduled events, meaning room(s) are free	
			for (x = 0; x < selectedRooms.length; x++) {
				if (availableRooms.get(selectedRooms[x]) == null) {
					availableRooms.put(selectedRooms[x], 0);
				}
			}//put other rooms into hashmap with value 0 if they didnt have events
			//stored with 0 since no events
			
			rooms = 0;
			for (x = 0; x < selectedRooms.length; x++) {
				int timeLeftOrEndOfMeeting = availableRooms.get(selectedRooms[x]);
				switch (rooms) {
				case 0:				
					roomTextView = (TextView) findViewById(R.id.rowRoom1);
					availTextView = (TextView) findViewById(R.id.rowAvail1);
					descripTextView = (TextView) findViewById(R.id.rowDescrip1);
					break;
				case 1:
					roomTextView = (TextView) findViewById(R.id.rowRoom2);
					availTextView = (TextView) findViewById(R.id.rowAvail2);
					descripTextView = (TextView) findViewById(R.id.rowDescrip2);
					break;
				case 2:
					roomTextView = (TextView) findViewById(R.id.rowRoom3);
					availTextView = (TextView) findViewById(R.id.rowAvail3);
					descripTextView = (TextView) findViewById(R.id.rowDescrip3);
					break;
				case 3:
					roomTextView = (TextView) findViewById(R.id.rowRoom4);
					availTextView = (TextView) findViewById(R.id.rowAvail4);
					descripTextView = (TextView) findViewById(R.id.rowDescrip4);
					break;
				case 4:
					roomTextView = (TextView) findViewById(R.id.rowRoom5);
					availTextView = (TextView) findViewById(R.id.rowAvail5);
					descripTextView = (TextView) findViewById(R.id.rowDescrip5);
					break;
				case 5:
					roomTextView = (TextView) findViewById(R.id.rowRoom6);
					availTextView = (TextView) findViewById(R.id.rowAvail6);
					descripTextView = (TextView) findViewById(R.id.rowDescrip6);
					break;
				case 6:
					roomTextView = (TextView) findViewById(R.id.rowRoom7);
					availTextView = (TextView) findViewById(R.id.rowAvail7);
					descripTextView = (TextView) findViewById(R.id.rowDescrip7);
					break;
				case 7:
					roomTextView = (TextView) findViewById(R.id.rowRoom8);
					availTextView = (TextView) findViewById(R.id.rowAvail8);
					descripTextView = (TextView) findViewById(R.id.rowDescrip8);
					break;
				case 8:
					roomTextView = (TextView) findViewById(R.id.rowRoom9);
					availTextView = (TextView) findViewById(R.id.rowAvail9);
					descripTextView = (TextView) findViewById(R.id.rowDescrip9);
					break;
				case 9:
					roomTextView = (TextView) findViewById(R.id.rowRoom10);
					availTextView = (TextView) findViewById(R.id.rowAvail10);
					descripTextView = (TextView) findViewById(R.id.rowDescrip10);
					break;
				case 10:
					roomTextView = (TextView) findViewById(R.id.rowRoom11);
					availTextView = (TextView) findViewById(R.id.rowAvail11);
					descripTextView = (TextView) findViewById(R.id.rowDescrip11);
					break;
				}
				roomTextView.setText(selectedRooms[x]);
				if (timeLeftOrEndOfMeeting < 0) {
					int minutes = (-timeLeftOrEndOfMeeting) % 100,
						hours = ((-timeLeftOrEndOfMeeting)-minutes)/100;
					if (-timeLeftOrEndOfMeeting >= 1200) {	
						if (hours > 12) hours -= 12;
						if (minutes < 10) {
							descripTextView.setText("Not available until "+hours+":0"+minutes+" PM");
						}//if minutes is 1 digit
						else {
							descripTextView.setText("Not available until "+hours+":"+minutes+" PM");					
						}
					}
					else {
						if (minutes < 10) {
							descripTextView.setText("Not available until "+hours+":0"+minutes+" AM");
						}//if minutes is 1 digit
						else {
							descripTextView.setText("Not available until "+hours+":"+minutes+" AM");							
						}
					}
					availTextView.setTextColor(Color.RED);
					availTextView.setText("No");	
				}
				else {						
					if (timeLeftOrEndOfMeeting > 0) {
						int minutes = timeLeftOrEndOfMeeting%60,
							hours = (timeLeftOrEndOfMeeting-minutes)/60;
						StringBuilder displayInfo = new StringBuilder("Available for ");
						if (hours > 1) displayInfo.append(hours+" hours ");
						else if (hours == 1) displayInfo.append(hours+" hour ");
						if (minutes > 1) displayInfo.append(minutes+" minutes ");
						else if (minutes == 1) displayInfo.append(minutes+" minute ");
						descripTextView.setText(displayInfo);
					}//took your suggestion Zach
					else {
						descripTextView.setText("Rest of the day");
					}
					availTextView.setTextColor(Color.GREEN);
					availTextView.setText("Yes");
				}
				rooms++;					
			}//display events availability
		//credits to Brandon for layout inspiration and code design in this part
		}	
	}
}
