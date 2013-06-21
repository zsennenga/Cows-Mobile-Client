package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CasAuth extends Activity {
	CookieManager cookieManager;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//Allow network in main thread
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		//Android Stuff
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cas_auth);
		
		//Setup Webview
		WebView myWebView = (WebView) findViewById(R.id.webView);
		myWebView.loadUrl("https://cas.ucdavis.edu/cas/login");
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.getSettings().setSavePassword(false);
		myWebView.getSettings().setSaveFormData(false);
		myWebView.setWebViewClient(new WebViewClient() {
			   public void onPageFinished(WebView view, String url) {
				   //check if was 200 else reload or something
			       checkCookie(view);
			   }
		});
		this.cookieManager = CookieManager.getInstance();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cas_auth, menu);
		return true;
	}
	/**
	 * Takes a cookie and pulls out the CASTGC if it exists, then clears all cookies.
	 * 
	 * @param View used to create the next activity
	 */
	public void checkCookie(View v)	{
		final String cookie = cookieManager.getCookie("https://cas.ucdavis.edu/cas");
		if (cookie != null && cookie.contains("CASTGC"))	{
			String[] parts = cookie.split("; ");
			for (String part : parts)	{
				String[] pieces = part.split("=");
				if (pieces[0].equals("CASTGC"))	{
					Intent i = new Intent(v.getContext(), EventCreation.class);
					i.putExtra("TGC", pieces[1]);
					startActivity(i);
					cookieManager.removeAllCookie();
					cookieManager.removeSessionCookie();
					finish();
				}
			}
		}
	}
	
	public void backHandle(View v)	{
		cookieManager.removeAllCookie();
		cookieManager.removeSessionCookie();
		finish();
	}
}
