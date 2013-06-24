package com.zennenga.cows_mobile_client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
public class RoomEventView extends Activity {

	private static final int TIMEOUT_MILLISEC = 10000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_event_view);
		
		
		getEvents();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.room_event_view, menu);
		return true;
	}
	
	public void getEvents() {
	    try {
	        HttpParams httpParams = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(httpParams,
	                TIMEOUT_MILLISEC);
	        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
	        //
	        HttpParams p = new BasicHttpParams();
	        p.setParameter("user", "1");

	        // Instantiate an HttpClient
	        HttpClient httpclient = new DefaultHttpClient(p);
	        
	        TextView text = (TextView) findViewById(R.id.testString);
	        text.setText("test");
	        
	        //Use 10.0.2.2:8080/ for emulator, change to actual address later
	        String url = "http://128.120.151.196/cows/COWS%20TV%20Display/ajaxEvents.php";
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
	            JSONObject json = new JSONObject(responseBody);
	            
	            text.setText("test2");
	            text.setText(json.getString("2"));


	        } catch (ClientProtocolException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	    } catch (Throwable t) {
	        Toast.makeText(this, "Request failed: " + t.toString(),
	                Toast.LENGTH_LONG).show();
	    }
	}

}
