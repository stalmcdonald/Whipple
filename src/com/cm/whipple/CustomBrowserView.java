package com.cm.whipple;

import android.webkit.WebView;
import android.webkit.WebViewClient;
//extends WebViewClient so android doesn't start intent on clicking link
public class CustomBrowserView extends WebViewClient {

	//Links load in custom browser 
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {

		view.loadUrl(url);
		return true;
	}
}
