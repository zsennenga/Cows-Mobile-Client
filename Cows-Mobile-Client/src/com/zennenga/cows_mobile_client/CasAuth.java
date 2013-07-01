package com.zennenga.cows_mobile_client;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CasAuth extends Activity {
	CookieManager cookieManager;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

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
			       checkCookie(view);
			   }
		});
		if (getIntent().getBooleanExtra("retryingAuth", false))	{
			Utility.showMessage("CAS Error: " + getIntent().getStringExtra("error") + " Please reauthenticate.", getApplicationContext());
		}
		this.cookieManager = CookieManager.getInstance();
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
					//If we are NOT retrying auth due to an error, create an EventCreation activity and pass TGC to it
					if (!getIntent().getBooleanExtra("retryingAuth", false))	{
						Intent i = new Intent(v.getContext(), EventCreation.class);
						i.putExtra("TGC", pieces[1]);
						startActivity(i);
					}
					else	{
						Intent i = new Intent();
						i.putExtra("TGC", pieces[1]);
						this.setResult(RESULT_OK, i);
					}
					cookieManager.removeAllCookie();
					cookieManager.removeSessionCookie();
					finish();
				}
			}
		}
	}
	/**
	 * Handler for the back button.
	 * @param v
	 */
	public void backHandle(View v)	{
		cookieManager.removeAllCookie();
		cookieManager.removeSessionCookie();
		finish();
	}
}
