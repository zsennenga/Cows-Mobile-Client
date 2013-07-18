package com.zennenga.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.zennenga.cows_mobile_client.R;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.widget.Toast;

public class Utility {
	public static final String BASE_URL = "http://dev.its.ucdavis.edu/v1/CowsMobile/CowsMobileProxy.php";
	public static final int LOCATION_ATTRIBUTE_ARRAY = 0;
	public static final int CATEGORY_ATTRIBUTE_ARRAY = 1;
	public static final int EVENT_ATTRIBUTE_ARRAY = 2;
		
	/**
	 * Deauthenticates the client from CAS
	 * 
	 * This is not performed in an async task mostly because 
	 * I want to be 100% sure this actually completes due to 
	 * security/privacy issues before the ui continues its operations
	 * 
	 * @return
	 */
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
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		cookieManager.removeSessionCookie();
		return true;
	}
	/**
	 * Converts a field and a value to a GET parameter
	 * 
	 * @param field
	 * @param value
	 * @return &field=value
	 */
	public static String getString(String field,String value)	{
		String val = "";
		try {
			val = URLEncoder.encode(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "&" + field + "=" + value;
		}
		return "&" + field + "=" + val;
	}
	/**
	 * 
	 * Converts an httpresonse to an httpentity to an inputstream to a buffered reader to a string.
	 * 
	 * @param HttpResponse
	 * @return Body of the response
	 */
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
	/**
	 * Show a toast with a message, usually an error message
	 * 
	 * @param Message
	 * @param appContext
	 */
	public static void showMessage(String error, Context appContext)	{
		Toast.makeText(appContext, error, Toast.LENGTH_LONG).show();
		return;
	}
	/**
	 * Gets the attribute of a choice from a spinner.
	 * Attributes are usually the fieldName used by cows, derived
	 * from the friendly name shown in the spinner
	 * 
	 * @param Index of the StringArray related to the attribute
	 * @param Which array to grab
	 * @param Context to getResources() from
	 * @return String version of the attribute
	 */
	public static String getAttr(int index, int arrayChoice, Context c) throws IllegalArgumentException {
		switch(arrayChoice)	{
			case 0:
				return c.getResources().getStringArray(R.array.locattr)[index];
			case 1:
				return c.getResources().getStringArray(R.array.catattr)[index];
			case 2:
				return c.getResources().getStringArray(R.array.eventattr)[index];
			default:
				throw new IllegalArgumentException("Invalid attribute array");
		}
	}
}
