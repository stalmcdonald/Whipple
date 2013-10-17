package com.cm.whipple;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
	public static final String TIP = "Current tide";
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
	    Log.i(TIP, "onUpdate");
		
		final int N = appWidgetIds.length;
		
		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int i=0; i<N; i++) {
		    int appWidgetId = appWidgetIds[i];
		    		    
		    Log.i(TIP, "updating widget[id] " + appWidgetId);

		    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		    
		    Intent intent = new Intent(context, WhippleWidget.class);
		    intent.setAction(WhippleWidget.RANDOMTIPS);
		    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		    PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
		
		    views.setOnClickPendingIntent(R.id.randomClamTips, pendingIntent);
		    Log.i(TIP, "pending intent set");
		    
		    // Tell the AppWidgetManager to perform an update on the current App Widget
		    appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
}