/*
 * Crystal McDonald
 * MDF3
 * 1310
 * Week 3
 * 
 * Whipple
 */
package com.cm.whipple;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cm.whipple.DataFile;
import com.cm.whipple.R;
import com.cm.whipple.Service;
import com.cm.whipple.WebFile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity<TideActivity> extends Activity {//implements ActionBar.OnNavigationListener {
	//Create my custom API URL
			//pulling city tide prediction from the wunderground api
			//string reference URL
			static final String baseURL = "http://api.wunderground.com/api/3e64fa36c4f09bdd/tide/q/WA/";
			
			//text view will change for tide text
			 TextView tvCity,tvPrediction, tvWater;
			 TextView calendar, tidepre, waveheight, tidesite;
			 EditText etCity;
			 Context _context;
			//JSONObject results, type, tide, tideInfo;
			 HashMap<String, String> _history;
			 
			 //checks network connection
			 Boolean _connected = false;//want to assume not connected
			 private Button b, bLow, bPrediction5;  //global button
			 
			 
			 /** Called when the activity is first created. */
		        @SuppressLint("HandlerLeak")
				@Override
		   public void onCreate (final Bundle savedInstanceState) {
		           super.onCreate(savedInstanceState);
		           _context = this;
		           _history = getHistory();
		           Log.i("HISTORY READ",_history.toString());

		      		//setting up views
		           setContentView(R.layout.activity_main);
		           b = (Button)findViewById(R.id.bPrediction);
		           bLow = (Button)findViewById(R.id.bPredictionLow);
		           bPrediction5 = (Button)findViewById(R.id.bPrediction);
		           
		           etCity = (EditText)findViewById(R.id.etCity);
		           
		           tvCity = (TextView)findViewById(R.id.tvCity);
		           tvPrediction = (TextView)findViewById(R.id.tvPrediction);
		           tvWater = (TextView)findViewById(R.id.tvWater);
		           tidesite = (TextView)findViewById(R.id.tidesite);//location
		           calendar = (TextView)findViewById(R.id.calendar);//date
		           tidepre = (TextView)findViewById(R.id.tidepre);//high or low tide
		           waveheight = (TextView)findViewById(R.id.waveheight);//swell height
		           
		           //set a button for onclicklistener
		           b.setOnClickListener(new OnClickListener() {
		       		
		        	   //gets text entered in edit text and appends to textviews along with data pulled from json
	                @SuppressLint("HandlerLeak")
					@Override
	                public void onClick(View v) {
	                       
	                    // getting data and appending it to a string
	                    String c = etCity.getText().toString();
	                    String cal = calendar.getText().toString();
	                    String ts = tidesite.getText().toString();
	                    String tp = tidepre.getText().toString();
	                    String wh = waveheight.getText().toString();
	                    StringBuilder URL = new StringBuilder(baseURL);
	                       
	                    // this hides the keyboard after user selects the predict button
	                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		        		   imm.hideSoftInputFromWindow(b.getWindowToken(), 0);
		        		   
		        		   //Detects the network connection
		        	  		_connected = WebFile.getConnectionStatus(_context);
		        	  		if(_connected){
		        	  			Log.i("NETWORK CONNECTION ", WebFile.getConnnectionType(_context));
		        	  		}else{
		        	  			//notified if user isnt connected to the internet
		        	  			Context context = getApplicationContext();
		        	  			CharSequence text = "No Network Detected";
		        	  			int duration = Toast.LENGTH_SHORT;

		        	  			Toast toast = Toast.makeText(context, text, duration);
		        	  			toast.show();
		        	  		}
		        		
		        		   //Callback Method
		        		   Handler myHandler = new Handler(){
		        			   
		        			   public void handleMessage(Message msg){
		        				   super.handleMessage(msg);        				   
		        				   updateUI();
		        			   }
		        		   };
		        		   
		        		   //builds the url needed to pull data	
		        		   String tempUrl = "";
		        		   //adds base url + city entered by user +.json to complete correct url
		        		   tempUrl = new String(baseURL + c + ".json");
		        		   if (baseURL == null);//changed from tempURL
		        		   Context context = getApplicationContext();
	    	  				CharSequence notify = "Enter City";
	    	  				int duration = Toast.LENGTH_SHORT;

	    	  				Toast toast = Toast.makeText(context, notify, duration);
	    	  				toast.show();
	    	  				URL finalURL;                       
	                        try{
	                     	   //final url is displayed in logcat to show the right information is being pulled
	                     	   finalURL = new URL(tempUrl);
	                     	   Log.i("FINAL URL", finalURL.toString());
	                     	   
	                     	   /*
	                     	    * Explicit Intent: to Services/Singleton
	                     	    */
	                     	   Messenger myMessenger = new Messenger(myHandler);
	                     	   Intent myIntent = new Intent(_context, Service.class);
	    				        
	                     	   myIntent.putExtra("messenger", myMessenger);
	                     	   myIntent.putExtra("tidal_city", c);
	                     	   myIntent.putExtra("final_URL", finalURL.toString());
	                     	   Log.i("TIDE ACTIVITY", "Starting Service");

	                     	   //start the service the handleMessage method wont be called yet
	                     	   startService(myIntent);
	                     	   
	                            tvPrediction.setText("The best time to go clam digging is when there is a low tide. ");
	                             
	                      } catch (MalformedURLException e){
	                     
	                              Log.e("BAD URL", "MALFORMED URL");
	                              tvCity.setText("Can not provide information at this time");
	                              tvPrediction.setText( tp + " Tide Prediction: UNKNOWN");
	                              tvWater.setText(ts + ": Location: UNKOWN");
	                              etCity.setText(URL);
	                      } finally {
	                              // This is done even if try block fails
	                                  Log.i("LOG", "I have hit the finally statement");
	                      }
	                }
	             });  
	    	           
	    	        //set a button for getting coordinates
	    	           bLow.setOnClickListener(new OnClickListener() {
	    	       		
	    	        	   //gets text entered in edit text and appends to textviews along with data pulled from json
	                 @SuppressLint("HandlerLeak")
	    				@Override
	                 public void onClick(View v3) {
	                        
	                 	// getting data and appending it to a string
	                     String c = etCity.getText().toString();
	                     String cal = calendar.getText().toString();
	                     String ts = tidesite.getText().toString();
	                     String tp = tidepre.getText().toString();
	                     String wh = waveheight.getText().toString();
	                     StringBuilder URL = new StringBuilder(baseURL);
	                        
	                     // this hides the keyboard after user selects the predict button
	                     InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    	        		   imm.hideSoftInputFromWindow(b.getWindowToken(), 0);
	    	        		   
	    	        		 //Detects the network connection
	    	        	  		_connected = WebFile.getConnectionStatus(_context);
	    	        	  		if(_connected){
	    	        	  			Log.i("NETWORK CONNECTION ", WebFile.getConnnectionType(_context));
	    	        	  		}else{
	    	        	  			//notified if user isnt connected to the internet
	    	        	  			Context context = getApplicationContext();
	    	        	  			CharSequence text = "No Network Detected";
	    	        	  			int duration = Toast.LENGTH_SHORT;

	    	        	  			Toast toast = Toast.makeText(context, text, duration);
	    	        	  			toast.show();
	    	        	  		}
	    	        		
	    	        		   
	    	        		   //Callback Method
	    	        		   Handler myHandler = new Handler(){
	    	        			   
	    	        			   public void handleMessage(Message msg){
	    	        				   super.handleMessage(msg);
	    	        				   
	    	        				   updateUILatLon();
	    	        				   //updateUI_CP();
	    	        			   }
	    	        		   };
	    	        		   
	    	        		   //builds the url needed to pull data	
	    	        		   String tempUrl = "";
	    	        		   //adds base url + city entered by user +.json to complete correct url
	    	        		   tempUrl = new String(baseURL + c + ".json");
	    	        		   if (tempUrl == null);
	    	        		   Context context = getApplicationContext();
	        	  				CharSequence notify = "Please Enter City";
	        	  				int duration = Toast.LENGTH_SHORT;

	        	  				Toast toast = Toast.makeText(context, notify, duration);
	        	  				toast.show();
	    	        		    

	    	        		 //saves instance
	    	        		   if (savedInstanceState !=null){
	    	        			   Log.d("Tide Activity", "Saved Instance");
	    	        			   
	    	        			   savedInstanceState.putString(tempUrl, c);
	    	        			   savedInstanceState.putString("tidesite", ts);
	    	        			   savedInstanceState.putString("calendar", cal);
	    	        			   savedInstanceState.putString("tidepre", tp);
	    	        			   savedInstanceState.putString("waveheight", wh);
	    	        			   onSaveInstanceState(savedInstanceState);
	    	        				   
	    	        				   
	    	        			   }
	    	        		   
	    	           
	    	        		   
	                     URL finalURL;                       
	                     try{
	                  	   //final url is displayed in logcat to show the right information is being pulled
	                  	   finalURL = new URL(tempUrl);
	                  	   Log.i("FINAL URL", finalURL.toString());
	                  	   
	                  	   /*
	                 	    * Explicit Intent: to Services/Singleton
	                 	    */
	                  	   Messenger myMessenger = new Messenger(myHandler);
	                  	   Intent myIntent = new Intent(_context, Service.class);
	                  	   myIntent.putExtra("messenger", myMessenger);
	                  	   myIntent.putExtra("tidal_city", c);
	                  	   myIntent.putExtra("final_URL", finalURL.toString());
	                  	   Log.i("TIDE ACTIVITY", "Starting Service");
	                  	   
	                  	   //start the service the handleMessage method wont be called yet
	                  	   startService(myIntent);
	                         tvPrediction.setText("The best time to go clam digging is when there is a low tide. ");
	                          
	                   } catch (MalformedURLException e){
	                  
	                           Log.e("BAD URL", "MALFORMED URL");
	                           tvCity.setText("Can not provide information at this time");
	                           tvPrediction.setText( tp + " Tide Prediction: UNKNOWN");
	                           tvWater.setText(ts + ": Location: UNKOWN");
	                           etCity.setText(URL);
	                   } finally {
	                           // This is done even if try block fails
	                               Log.i("LOG", "I have hit the finally statement");
	                   }
	             }
	          });               
	    	           
	    	           
	    	        }//end onCreate
	                
		        public String dataToString(){
		    		return "In " + etCity + " The tide prediction: High";
		    	}//end


		    	//create method to get history from Hard drive
		     @SuppressWarnings("unchecked")
		    	private HashMap<String, String> getHistory(){
		     	Object stored = DataFile.readObjectFile(_context, "history", false);
		     	
		     	HashMap<String, String> history;
		     	if(stored == null){
		     		Log.i("HISTORY", "NO HISTORY FILE FOUND");
		     		history = new HashMap<String, String>();
		     	}else{
		     		history = (HashMap<String, String>)stored;
		     	}
		     	return history;
		     }//end Hashmap
		     
		     @SuppressWarnings("unused")
		    	private class LocRequest extends AsyncTask<URL,Void,String>{
		     	//override 2 separate functions
		     	@Override
		     	protected String doInBackground(URL...urls){
		     		String response = "";
		     		
		     		//pass an array even though it only holds one
		     		for(URL url: urls){
		     			Log.e("URL DOB", url.toString());
		     			response = WebFile.getURLSTringResponse(url);
		     		}
		     		return response;
		     	}
		     	
		     	//onPostExecute now inside the LocRequest class, it is a 
		     	// required interface class for AsyncTask
		     	@Override
		        	public void onPostExecute(String result){
		        		Log.i("JSON RESULTS", result);
		        		
		     	} 		
		     }//end locquest
		     
		     
		     //code now inside content provider...reading files and getting different fields
		     //data read from file and updated here to the UI
		     public void updateUI() {
		    		// TODO Auto-generated method stub
		    		//Read data from file and parse JSON
		    		JSONObject job = null;
		         JSONArray recordArray, locArray = null;
		         JSONObject field = null;
		         
		         //String JSONString = DataFile.readStringFile(getBaseContext(), "tideInfo.txt", false); 
		         String JSONString = DataFile.readStringFile2(_context, "tideInfo.txt"); 
		         String tideHeight = null;
		         String tideInfo = null;
		         String date = null;
		         String tideType = null; 
		         
		         //drilling down into the object TIDE to get tideInfo array which holds 
		         //the field to get tideSite information and the array tideSummary to get the object data to get
		         //to the fields that hold waveHeight and tide information
		         
		         try {          
		             job = new JSONObject(JSONString);
		             locArray = job.getJSONObject("tide").getJSONArray("tideInfo");
		             recordArray = job.getJSONObject("tide").getJSONArray("tideSummary");
		             
		             //Log.i("recordArray",recordArray.toString());
		             //finds the fields in the array for object tideSummary
		             for(int i = 0; i < recordArray.length(); i++) {
		                     //Log.i("recordArray, field",recordArray.getJSONObject(i).toString());
		                     field = recordArray.getJSONObject(i);

		                     tideHeight = field.getJSONObject("data").get("height").toString();//Swell
		                     date = field.getJSONObject("date").get("pretty").toString();//date
		                     tideType = field.getJSONObject("data").get("type").toString();//hi/lo tide
		                     
		                     //gives date, tide height and tide type in log cat for a 5-day forecast
		                     Log.i("Parsed JSON data", "On "+date+", date the tide height will be "+tideHeight
		                                     +" for a tide type of "+tideType);

		              for(int i1 = 0; i1 < locArray.length(); i1++) {
		                      field = locArray.getJSONObject(i1);

		                      tideInfo = field.get("tideSite").toString();//site location given
		                     
		                     //Displays update text here.
		                     tidesite.setText("Location:              " +tideInfo);//location
		                     calendar.setText("Date:                     "+date);//date
		                     tidepre.setText("Tide Prediction:   "+tideType);//hi/lo 
		                     waveheight.setText("Swell:                    "+tideHeight);//swell
		                   
		                    
		                     }     
		             }
		     } catch (JSONException e) {
		             Log.e("JSON EXCEPTION", e.toString());
		     }
		     }
		     public void updateUILatLon() {
		    		// TODO Auto-generated method stub
		    		//Read data from file and parse JSON
		    		JSONObject job = null;
		         JSONArray recordArray, locArray = null;
		         JSONObject field = null;
		         
		         //String JSONString = DataFile.readStringFile(getBaseContext(), "tideInfo.txt", false); 
		         String JSONString = DataFile.readStringFile2(_context, "tideInfo.txt"); 
		         String tideHeight = null;
		         String tideInfo = null;
		         String date = null;
		         String tideType = null; 
		         String lat = null;
		         String lon = null;
		         
		         //drilling down into the object TIDE to get tideInfo array which holds 
		         //the field to get tideSite information and the array tideSummary to get the object data to get
		         //to the fields that hold waveHeight and tide information
		         
		         try {          
		             job = new JSONObject(JSONString);
		             locArray = job.getJSONObject("tide").getJSONArray("tideInfo");
		             recordArray = job.getJSONObject("tide").getJSONArray("tideSummary");
		             
		             //Log.i("recordArray",recordArray.toString());
		             //finds the fields in the array for object tideSummary
		             for(int i = 0; i < recordArray.length(); i++) {
		                     //Log.i("recordArray, field",recordArray.getJSONObject(i).toString());
		                     field = recordArray.getJSONObject(i);

		                     tideHeight = field.getJSONObject("data").get("height").toString();//Swell
		                     date = field.getJSONObject("date").get("pretty").toString();//date
		                     tideType = field.getJSONObject("data").get("type").toString();//hi/lo tide
		                     
		                     //gives date, tide height and tide type in log cat for a 5-day forecast
		                     Log.i("Parsed JSON data", "On "+date+", date the tide height will be "+tideHeight
		                                     +" for a tide type of "+tideType);

		              for(int i1 = 0; i1 < locArray.length(); i1++) {
		                      field = locArray.getJSONObject(i1);

		                      tideInfo = field.get("tideSite").toString();//site location given
		                      lat = field.get("lat").toString();//latitude given
		                      lon = field.get("lon").toString();//longitude given
		                      
		                     //Displays update text here.
		                     tidesite.setText("Location:              " +tideInfo);//location
		                     calendar.setText("Latitude:                  "+lat);//latitude replaces date
		                     tidepre.setText("Longitude:                 "+lon);//longitude replaces prediction 
		                     waveheight.setText("Tide Prediction:            "+tideType);//prediction
		                     }     
		             }
		     } catch (JSONException e) {
		             Log.e("JSON EXCEPTION", e.toString());
		     }
		     }
		     
		     //onrestore
		     @Override
		     protected void onRestoreInstanceState(Bundle savedInstanceState){
		     	super.onRestoreInstanceState(savedInstanceState);
		     	if(savedInstanceState.getString(baseURL) !=null){
		     		((TextView) findViewById(R.id.etCity)).setText(savedInstanceState.getString("tempUrl"));
		     		((TextView) findViewById(R.id.tidesite)).setText(savedInstanceState.getString("tidesite"));
		     		((TextView) findViewById(R.id.calendar)).setText(savedInstanceState.getString("calendar"));
		     		((TextView) findViewById(R.id.tidepre)).setText(savedInstanceState.getString("tidepre"));
		     		((TextView) findViewById(R.id.waveheight)).setText(savedInstanceState.getString("waveheight"));
		     	}
		     	_history = getHistory();
		     }//end onRestore
		     
		     //onresume
		     @Override
		     protected void onResume(){
		     	super.onResume();
		     	_history = getHistory();
		     	
		     }//end onResume
		     

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
		case R.id.author:
			aboutAuthor();
			break;
			
			//launching intent that displays info about app
		case R.id.about:
			Intent intent = new Intent(this, AboutAppActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
			
		case R.id.tips:
			Intent tipIntent = new Intent(this, TipsActivity.class);
			tipIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(tipIntent);
			break;
		}
		return true;
	}//end onOptionsItemSelected

	
	//creating a method for actionbar item about Author in Alert Dialog 
	private void aboutAuthor() {
		// Shows alert with info about author
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
