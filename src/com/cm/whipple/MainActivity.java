/*
 * Crystal McDonald
 * MDF3
 * 1310
 * Week 3
 * 
 * Whipple
 */
package com.cm.whipple;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity<TideActivity> extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	//action bar
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	//Boolean to find action bar items
	//Switch case
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
		//action bar button found by id
//		case R.id.about:
//			aboutApp();
//			break;
		case R.id.author:
			aboutAuthor();
			break;
		}
		return true;
	}//end onOptionsItemSelected

	//creating a method for actionbar item aboutApp 
	//Launches Intent (not finished yet)
	private void aboutApp() {
			// sends user to site that shows more information on Whipple
//			Intent intent = new Intent(MainActivity.this, AboutAppActivity.class);
//			startActivity(intent); 	
			
	}//end aboutApp
	
	//creating a method for actionbar item about Author in Alert Dialog 
	private void aboutAuthor() {
		// Shows toast with info about author
		new AlertDialog.Builder(this).setTitle("Author")
		.setMessage("Crystal McDonald")
		.setNeutralButton("OK",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).show();
	}

}//end
