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
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class RoomAvailableTime extends Activity {
	
	private int day, month, year;	
	private Timer timer = new Timer();
	private static final int TIMEOUT_MILLISEC = 1000;
	private final String[] roomNums = {"1590 Tilia, Room 1142", "1605 Tilia, Room 1103", "1605 Tilia, Room 1111", "1605 Tilia, Room 1162", 
			"1715 Tilia, Room 1106", "1715 Tilia, Room 1119", "1715 Tilia, Room 1121", "1715 Tilia, Room 1123", "1715 Tilia, Room 1161", 
			"215 Sage, Room 1104", "215 Sage, Room 1113"};
	public static final String PM = "P";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_available_time);
		getData();
		addListenerOnBackButton();
		addListenerOnCreateButton();
		//addListenerOnRefreshButton();
		timer.schedule( new TimerTask() {
		    public void run() {
		       getEvents();
		    }
		 }, 0, 60*1000);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	
	public void getEvents() {
		nextAvailableRoomTime a = new nextAvailableRoomTime();
		a.execute();
	}
	
	private void getData() {
		day = getIntent().getExtras().getInt("day");
		month = getIntent().getExtras().getInt("month");
		year = getIntent().getExtras().getInt("year");		
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
				timer.cancel();
				Intent i = new Intent(v.getContext(), CasAuth.class);
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
	/*
	public void addListenerOnRefreshButton() {
		final Button refreshButton = (Button) findViewById(R.id.refreshButton);
		refreshButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getEvents();
			}
		});
	}
	*/
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
					+ ""
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
		}
		
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
			//		+ "{\"Title\":\"Test5\",\"Time\":\" 8:21 AM - 11:00 AM\",\"Location\":\"1605 Tilia, Room 1103\"},"
			//		+ "{\"Title\":\"Test5\",\"Time\":\" 11:00 AM - 1:00 PM\",\"Location\":\"1605 Tilia, Room 1103\"},"
			//		+ "{\"Title\":\"Test5\",\"Time\":\" 1:00 PM - 2:30 PM\",\"Location\":\"1605 Tilia, Room 1103\"},"
			//		+ "{\"Title\":\"Test5\",\"Time\":\" 2:30 PM - 4:00 PM\",\"Location\":\"1605 Tilia, Room 1103\"}]";
			//for testing
			
			Time currentTime = new Time();
			currentTime.setToNow();
			current24Time += (currentTime.hour*100)+currentTime.minute;	//8:47PM = 2047 and 12:30PM = 1230
			currentMinutes = toMinutes(current24Time);
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
			while (x < 30) {
				try {
					obj = json.getJSONObject(x);						
					info = info.concat(obj.getString("Time")+","+obj.getString("Location")+",");			
				} catch (JSONException e) {
					break;
				}
				x++;
			}//store jsonarray time and location into a string						
			String delims = "[,]+";
			String[] tokens = info.split(delims);//split string info into an array tokens[0] = time, token[1and2]=location, etc.
			
			if (tokens.length < 3) {
				Utility.showMessage("No Events to Show");
			} //in case: no events(weekends, past dates, etc.)
			else {
				for (x=0; x < tokens.length; x+=3) {
					int startTime = 0,
						endTime = 0;					
					String RoomNum = tokens[x+1].concat(","+tokens[x+2]);
					
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
							}
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
						tokens[x] = sb.toString(); 
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
						}//Room is available right now [for how long]
					}				
				}//End organizing events
				
				for (x = 0; x < roomNums.length; x++) {
					if (availableRooms.get(roomNums[x]) == null) {
						availableRooms.put(roomNums[x], 0);
					}
				}//put other rooms into hashmap with value 0, they are all free	
				
				rooms = 0;
				for (x = 0; x < roomNums.length; x++) {
					int timeLeftOrEndOfMeeting = availableRooms.get(roomNums[x]);
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
					roomTextView.setText(roomNums[x]);
					if (timeLeftOrEndOfMeeting < 0) {
						int minutes = (-timeLeftOrEndOfMeeting) % 100,
							hours = ((-timeLeftOrEndOfMeeting)-minutes)/100;
						if (-timeLeftOrEndOfMeeting >= 1200) {	
							if (hours > 12) hours -= 12;
							if (minutes < 10) {
								descripTextView.setText("Not available until "+hours+":0"+minutes+" PM");
							}
							else {
								descripTextView.setText("Not available until "+hours+":"+minutes+" PM");					
							}
						}
						else {
							if (minutes < 10) {
								descripTextView.setText("Not available until "+hours+":0"+minutes+" AM");
							}
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
							descripTextView.setText("Available for "+hours+" hour(s) "+minutes+" minutes");
						}
						else {
							descripTextView.setText("Rest of the day");
						}
						availTextView.setTextColor(Color.GREEN);
						availTextView.setText("Yes");
					}
					rooms++;					
				}//display events availability
			}
		}
			
	}

}
