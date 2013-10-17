package com.cm.whipple;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class WhippleWidget extends Service {
	public static final String RANDOMTIPS = "updateTip";
	public static final String CURRENTTIP = "MyTip";
	
	private String mainClamtips;
	private LinkedList<String> clamtips;
	
	public WhippleWidget(){
		this.clamtips = new LinkedList<String>();
		randomTipList();
	}

	//creating Random tips to display in widget
    private void randomTipList() {
    	this.clamtips.add("Dig clams at low-tide for best results");
    	this.clamtips.add("Clamming Season Begins October.");
    	this.clamtips.add("Don't forget to purchase a liscence!");
    	this.clamtips.add("Digging clams is very messy work.");
    	this.clamtips.add("Small clams are great for chowders");
    	this.clamtips.add("Cockies are used in chowders");
    	this.mainClamtips = "Dig clams at Low Tide";
    }


    //when starting widget im calling methods to display first tip
	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int beginHere) {
		
		//super.onStart(intent, beginHere);
		
        Log.i(WidgetProvider.TIP, "onStartCommand");

        getTips(intent);
			
		stopSelf(beginHere);
		
		return START_STICKY;
	}

	//creating a method that handles random tips by timer
	private String displayTipsAtRandom() {
		Random random = new Random(Calendar.getInstance().getTimeInMillis());//changes based on time set up in xml (5000)
		int randomlyPlaced = random.nextInt(clamtips.size());
		return clamtips.get(randomlyPlaced);
	}

	//getting the tips to be displayed randomly
	private void getTips(Intent intent) {
        if (intent != null){
    		String requestedAction = intent.getAction();
            Log.i(WidgetProvider.TIP, "ACTION " + requestedAction);
    		if (requestedAction != null && requestedAction.equals(RANDOMTIPS)){
	            this.mainClamtips = displayTipsAtRandom();
	            	            	 
	            int tipWidg = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);

	            Log.i(WidgetProvider.TIP, "Tidal Prediction: " + mainClamtips + " to widget " + tipWidg);

	            AppWidgetManager myWidgMan = AppWidgetManager.getInstance(this);
	            RemoteViews rv = new RemoteViews(this.getPackageName(),R.layout.widget);
	            rv.setTextViewText(R.id.randomClamTips, mainClamtips);
	            myWidgMan.updateAppWidget(tipWidg, rv);
	            
	            Log.i(WidgetProvider.TIP, "Clamming tips updated.");
    		}
        }
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}

