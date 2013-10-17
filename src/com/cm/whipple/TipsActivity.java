package com.cm.whipple;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TipsActivity extends Activity implements OnClickListener {
Button browseButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tips);
		
		browseButton = (Button) findViewById(R.id.browseButton);
		
		browseButton.setOnClickListener(new OnClickListener() {//setup onclick listener
		
		/*
		 * Implicit Intent: Launches Web Browsers to open website
		 */
			public void onClick(View webview) {
				// sends user to site that shows more information on clam digging
	Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://wdfw.wa.gov/fishing/shellfish/razorclams/howto_dig.html"));
	startActivity(viewIntent); 
};	
		});
}
//		@Override
//		public boolean onCreateOptionsMenu(Menu menu) {
//			// Inflate the menu; this adds items to the action bar if it is present.
//			getMenuInflater().inflate(R.menu.activity_main, menu);
//			return true;
//			
//		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
		
		
}//end