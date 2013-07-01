package com.zennenga.cows_mobile_client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
	    try {
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
	        
	        String url = "http://dev.its.ucdavis.edu/v1/TabletDisplay/ajaxEvents.php"
	        			 + "?date=" + date + "&bldgRoom=" + roomCode;
	        HttpPost httppost = new HttpPost(url);
	        

	        // Instantiate a GET HTTP method
	        try {
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
	                    2);
	            nameValuePairs.add(new BasicNameValuePair("user", "1"));
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            String responseBody = httpclient.execute(httppost, responseHandler);
	            // Parse
	            JSONArray json = new JSONArray(responseBody);
	            JSONObject obj;
	            String title, time;
	            int eventsDisplayed = 0;
	            TextView timeText, titleText;
	            
	            while(eventsDisplayed < MAX_EVENT_DISPLAY) {
	            
		            try {         
		            	obj = json.getJSONObject(eventsDisplayed);
		            } 
		            catch (JSONException e) {
		            	break;    
		            }
		            switch(eventsDisplayed) {
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
		            title = obj.getString("Title"); 
		            time = obj.getString("Time"); 
		            timeText.setText(time);
		            titleText.setText(title);
		            eventsDisplayed++;
	            }
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	    } catch (Throwable t) {
	        Toast.makeText(this, "Request failed: " + t.toString(),
	                Toast.LENGTH_LONG).show();
	    }
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
	        public void onClick(View v) {
	        	Intent i = new Intent(v.getContext(), CasAuth.class);
	        	startActivity(i);
	        }
	    });
	}
	
	public void addListenerOnBackButton() {
		final Button backButton = (Button) findViewById(R.id.backButtonToRoomSelect);
		backButton.setOnClickListener(new View.OnClickListener() {
	        public void onClick(View v) {
	        	finish();
	        }
	    });
	}

}
