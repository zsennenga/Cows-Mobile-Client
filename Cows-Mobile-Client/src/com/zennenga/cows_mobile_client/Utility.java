package com.zennenga.cows_mobile_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class Utility {
	
	public static boolean deauth()	{
		DefaultHttpClient httpclient = new DefaultHttpClient();
		URI url = null;
		try {
			url = new URI("https://cas.ucdavis.edu/cas/logout");
		} catch (URISyntaxException e1) {
			Log.e("Deauth","URIExeception");
			e1.printStackTrace();
			return false;
		}
		HttpGet request = new HttpGet();
		request.setURI(url);
		try {
			httpclient.execute(request);
		} catch (ClientProtocolException e) {
			Log.e("Deauth", "ClientProtocolException");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Log.e("Deauth", "IOException");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static String getString(String field,String value)	{
		return "&" + field + "=" + value;
	}

	public static String httpResponseToString(HttpResponse out) {
		StringBuilder sb = new StringBuilder();
		try {
		    BufferedReader reader = new BufferedReader(new InputStreamReader(out.getEntity().getContent()), 65728);
		    String line = null;

		    while ((line = reader.readLine()) != null) {
		        sb.append(line);
		    }
		}
		catch (IOException e) { 
			e.printStackTrace(); 
		}
		catch (Exception e) { 
			e.printStackTrace(); 
		}
		
		return sb.toString();
	}
}
