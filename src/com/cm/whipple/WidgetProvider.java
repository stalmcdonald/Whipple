package com.cm.whipple;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
	public static final String TIP = "Current tide";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager myWidgetManager,
			int[] widgID) {
		super.onUpdate(context, myWidgetManager, widgID);
		
	    Log.i(TIP, "onUpdate");
		
		final int appWidgIds = widgID.length;
		
		
		   
		    //setting pending intent
		    Intent intent = new Intent(context, WhippleWidget.class);
		    intent.setAction(WhippleWidget.RANDOMTIPS);
		    //identifies widget
		    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgID);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//launch flag in the intent 
		    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		 // Make the pending intent unique...
		    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
		  //creating remote view
		    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		    views.setOnClickPendingIntent(R.id.randomClamTips, pendingIntent);
		    
		    // Tell the AppWidgetManager to perform an update on the current App Widget
		    myWidgetManager.updateAppWidget(widgID, views);
		
	}
}