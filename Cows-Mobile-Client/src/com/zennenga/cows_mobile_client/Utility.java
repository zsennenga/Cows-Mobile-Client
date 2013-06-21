package com.zennenga.cows_mobile_client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import android.webkit.CookieManager;

public class Utility {
	public static boolean deauth(CookieManager c, String ticket)	{
		DefaultHttpClient httpclient = new DefaultHttpClient();
		URI url = null;
		c.removeAllCookie();
		c.removeSessionCookie();
		try {
			url = new URI("https://cas.ucdavis.edu/cas/logout");
		} catch (URISyntaxException e1) {
			Log.e("Deauth","URIExeception");
			e1.printStackTrace();
			return false;
		}
		HttpGet request = new HttpGet();
		request.setURI(url);
		c.setCookie("https://cas.ucdavis.edu/cas","CASTGC="+ticket);
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
}
