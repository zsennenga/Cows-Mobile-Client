package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CasAuth extends Activity {

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cas_auth);
		WebView myWebView = (WebView) findViewById(R.id.webView);
		myWebView.loadUrl("http://cas.ucdavis.edu/cas/login");
		myWebView.getSettings().setJavaScriptEnabled(true);
		myWebView.setWebViewClient(new WebViewClient() {
			   public void onPageFinished(WebView view, String url) {
			        checkCookie(view);
			   }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cas_auth, menu);
		return true;
	}
	public void checkCookie(View v)	{
		CookieManager cookieManager = CookieManager.getInstance();
		final String cookie = cookieManager.getCookie("cas.ucdavis.edu");
		if (cookie.contains("CASTGC"))	{
			String[] parts = cookie.split("&");
			for (String part : parts)	{
				String[] pieces = part.split("=");
				if (pieces[0].equals("CASTGC"))	{
					//handle pieces[1]
					Intent i = new Intent(v.getContext(), MainActivity.class);
					startActivity(i);
				}
			}
		}
	}
}
