package com.zennenga.cows_mobile_client;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.webkit.CookieManager;

public class Utility {
	public static boolean deauth(String ticket)	{
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet("https://cas.ucdavis.edu/cas/logout");
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setCookie("cas.ucdavis.edu","CASTGC="+ticket);
		try {
			httpclient.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
