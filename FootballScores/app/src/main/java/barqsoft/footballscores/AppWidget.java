package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import barqsoft.footballscores.service.WidgetService;
import barqsoft.footballscores.service.myFetchService;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {
    public static String EXTRA_WORD = "barqsoft.footballscores.android.appwidget.WORD";
    public static String UPDATE_LIST = "UPDATE_LIST";
    public static String LOG_TAG = "AppWidget";

    @Override
    public void onUpdate(Context ctxt, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Log.d(LOG_TAG, "onUpdate");

        Intent service_start = new Intent(ctxt, myFetchService.class);
        ctxt.startService(service_start);

        for (int i=0; i<appWidgetIds.length; i++)
        {
            Log.d(LOG_TAG, "onUpdate.." + i);
            Intent svcIntent=new Intent(ctxt, WidgetService.class);

            RemoteViews widget=new RemoteViews(ctxt.getPackageName(), R.layout.app_widget);

//            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
//            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            widget.setRemoteAdapter(appWidgetIds[i], R.id.widget_scores_list, svcIntent);

//            Intent clickIntent = new Intent(ctxt, AppWidget.class);
//            clickIntent.setAction(UPDATE_LIST);
//            PendingIntent pendingIntentRefresh = PendingIntent.getBroadcast(ctxt,0, clickIntent, 0);
//            widget.setOnClickPendingIntent(R.id.widget_scores_list, pendingIntentRefresh);

            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
        }

        super.onUpdate(ctxt, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "...onUpdate");
    }

//    @Override
//    public void onReceive(Context context, Intent intent)
//    {
//        super.onReceive(context, intent);
//
//        Log.d(LOG_TAG, "onReceive");
//
//        if(intent.getAction().equalsIgnoreCase(UPDATE_LIST)){
//            updateWidget(context);
//        }
//    }
//
//    private void updateWidget(Context context)
//    {
//        Log.d(LOG_TAG, "updateWidget");
//
//        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
//        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, AppWidget.class));
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_scores_list);
//    }
}
