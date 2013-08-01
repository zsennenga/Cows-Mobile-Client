package com.zennenga.cows_mobile_client;

import java.io.IOException;

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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zennenga.utility.Utility;

public class RoomEventView extends Activity {
	private int day, month, year;
	private String roomCode;
	public static final int MAX_EVENT_DISPLAY = 10;
	private static final int TIMEOUT_MILLISEC = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_event_view);

		addListenerOnBackButton();
		addListenerOnCreateButton();
		getData();
		getEvents();
	}

	public void getEvents() {
		AsyncEvent a = new AsyncEvent();
		a.execute();
	}

	private void getData() {
		day = getIntent().getExtras().getInt("day");
		month = getIntent().getExtras().getInt("month");
		year = getIntent().getExtras().getInt("year");
		roomCode = getIntent().getExtras().getString("roomCode");
	}

	public void addListenerOnCreateButton() {
		final Button backButton = (Button) findViewById(R.id.createEventFromEventViewButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), CasAuth.class);
				startActivity(i);
			}
		});
	}

	public void addListenerOnBackButton() {
		final Button backButton = (Button) findViewById(R.id.backButtonToRoomSelect);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private class AsyncEvent extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {
			String date = month + "/" + day + "/" + year;
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			//
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
			Log.i("Brandon", url);
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
		protected void onPostExecute(String response) {
			JSONArray json = null;
			try {
				json = new JSONArray(response);
			} catch (JSONException e) {
				Utility.showMessage("Invalid response. Check that network is enabled");
				e.printStackTrace();
				return;
			}
			JSONObject obj;
			String title = null, time = null;
			int eventsDisplayed = 0;
			TextView timeText, titleText;

			while (eventsDisplayed < MAX_EVENT_DISPLAY) {

				try {
					obj = json.getJSONObject(eventsDisplayed);
				} catch (JSONException e) {
					break;
				}
				switch (eventsDisplayed) {
				case 0:
					timeText = (TextView) findViewById(R.id.rowTime1);
					titleText = (TextView) findViewById(R.id.rowTitle1);
					break;
				case 1:
					timeText = (TextView) findViewById(R.id.rowTime2);
					titleText = (TextView) findViewById(R.id.rowTitle2);
					break;
				case 2:
					timeText = (TextView) findViewById(R.id.rowTime3);
					titleText = (TextView) findViewById(R.id.rowTitle3);
					break;
				case 3:
					timeText = (TextView) findViewById(R.id.rowTime4);
					titleText = (TextView) findViewById(R.id.rowTitle4);
					break;
				case 4:
					timeText = (TextView) findViewById(R.id.rowTime5);
					titleText = (TextView) findViewById(R.id.rowTitle5);
					break;
				case 5:
					timeText = (TextView) findViewById(R.id.rowTime6);
					titleText = (TextView) findViewById(R.id.rowTitle6);
					break;
				case 6:
					timeText = (TextView) findViewById(R.id.rowTime7);
					titleText = (TextView) findViewById(R.id.rowTitle7);
					break;
				case 7:
					timeText = (TextView) findViewById(R.id.rowTime8);
					titleText = (TextView) findViewById(R.id.rowTitle8);
					break;
				case 8:
					timeText = (TextView) findViewById(R.id.rowTime9);
					titleText = (TextView) findViewById(R.id.rowTitle9);
					break;
				default:
					timeText = (TextView) findViewById(R.id.rowTime10);
					titleText = (TextView) findViewById(R.id.rowTitle10);
				}
				try {
					title = obj.getString("Title");
					time = obj.getString("Time");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				timeText.setText(time);
				titleText.setText(title);
				eventsDisplayed++;
			}
		}
	}
}
