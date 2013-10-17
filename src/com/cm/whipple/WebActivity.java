/*
 * Crystal McDonald
 * MDF 3
 * 1310
 * Web Activity
 * 
 * Set Sail Browser is a custom based Nautical Theme browser.
 */

package com.cm.whipple;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;


public class WebActivity extends Activity implements OnClickListener{
	WebView myWebView;
	EditText urlText;
	Button goBack, goForward, goButton;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//makes activity full screen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.web);
		//getting webView
		myWebView = (WebView)findViewById(R.id.clammingWebView);
		
		/*
		 * Screen Control: Optimal Usability: loads views zoomed out
		 */
		myWebView.getSettings().setUseWideViewPort(true);
		myWebView.getSettings().setLoadWithOverviewMode(true);
		
		/*
		 * Helps application perform action intended when clicking links
		 * allows links to load in my custom web browser
		 */
		myWebView.setWebViewClient(new CustomBrowserView());
		
		//default URL
		myWebView.loadUrl("http://wdfw.wa.gov/fishing/shellfish/razorclams/howto_dig.html");

	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}


}//end