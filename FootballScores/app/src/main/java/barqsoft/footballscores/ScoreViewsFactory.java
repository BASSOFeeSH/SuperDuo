package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jim on 2/22/2016.
 */
public class ScoreViewsFactory implements RemoteViewsService.RemoteViewsFactory,
                                          android.content.Loader.OnLoadCompleteListener<Cursor>
{
    //private static final String[] items={"loading.", "loading..", "loading..."};

    private Context ctxt=null;
    private int appWidgetId;
    private String[] fragmentdate = new String[1];
    private CursorLoader mCursorLoader;
    private Cursor mCursor;
    private static final int WIDGET_LISTENER = 0x01;
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public static String LOG_TAG = "ScoreViewsFactory";
    //
//    public scoresAdapter mAdapter;

    public ScoreViewsFactory(Context ctxt, Intent intent)
    {
        Log.d(LOG_TAG, "Constructor");
        this.ctxt=ctxt;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        //
//    private void update_scores()
//    {
//        Intent service_start = new Intent(ctxt, myFetchService.class);
//        ctxt.startService(service_start);
//    }

//        mAdapter = new scoresAdapter(ctxt,null,0);
        setFragmentDate();

    }

    public void setFragmentDate()
    {
        Log.d(LOG_TAG, "setFragmentDate");

        int i = Integer.parseInt(ctxt.getString(R.string.widget_day_offset));
        Date fragmentdate = new Date(System.currentTimeMillis()+((i)*86400000));
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        this.fragmentdate[0] = mformat.format(fragmentdate);
    }

    @Override
    public void onCreate()
    {
        Log.d(LOG_TAG, "onCreate");
        mCursorLoader = new CursorLoader(ctxt,DatabaseContract.scores_table.buildScoreWithDate(),null,null,fragmentdate,null);
        mCursorLoader.registerListener(WIDGET_LISTENER, this);
        mCursorLoader.startLoading();
    }

    @Override
    public void onDestroy()
    {
        Log.d(LOG_TAG, "onDestroy");

        if (mCursor != null && !mCursor.isClosed())
        {
            mCursor.close();
            mCursor = null;
        }

        // Stop the cursor loader
        if (mCursorLoader != null) {
            mCursorLoader.unregisterListener(this);
            mCursorLoader.cancelLoad();
            mCursorLoader.stopLoading();
        }
    }

    @Override
    public int getCount()
    {
        Log.d(LOG_TAG, "getCount");
        if(mCursor != null)
        {
            Log.d(LOG_TAG, Integer.toString(mCursor.getCount()));
            return (mCursor.getCount());
        }
        else
        {
            Log.d(LOG_TAG, "--null--");
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        Log.d(LOG_TAG, "getViewAt " + position);
        RemoteViews row = new RemoteViews(ctxt.getPackageName(), R.layout.widget_list_item);
//        RemoteViews row = new RemoteViews(ctxt.getPackageName(), R.layout.row);

        if (mCursor == null)
        {
            Log.d(LOG_TAG, "getViewAt mCursor is null");
            return row;
        }
        if (!mCursor.moveToPosition(position)) {
            // If we ever fail to move to a position, return the "View More conversations"
            // view.
            Log.d(LOG_TAG, "getViewAt Failed to move to position in the cursor." + position);
            return row;
        }

        String home_name = mCursor.getString(COL_HOME);
        String away_name = mCursor.getString(COL_AWAY);
        String date = mCursor.getString(COL_MATCHTIME);
        String score = Utilies.getScores(mCursor.getInt(COL_HOME_GOALS), mCursor.getInt(COL_AWAY_GOALS));

//        DateFormat df = new SimpleDateFormat("HH:mm:ss");
//        Date time = new Date();
//        score = df.format(time);
//        Double match_id = mCursor.getDouble(COL_ID);
//        int home_crest = Utilies.getTeamCrestByTeamName(mCursor.getString(COL_HOME));
//        int away_crest = Utilies.getTeamCrestByTeamName(mCursor.getString(COL_AWAY));


        row.setTextViewText(R.id.home_name, home_name);
        row.setTextColor(R.id.home_name, ctxt.getResources().getColor(R.color.black));
        row.setTextViewText(R.id.away_name, away_name);
        row.setTextColor(R.id.away_name, ctxt.getResources().getColor(R.color.black));
//        row.setTextViewText(R.id.data_textview, date);
//        row.setTextColor(R.id.data_textview, ctxt.getResources().getColor(R.color.black));
        row.setTextViewText(R.id.score_textview, score);
        row.setTextColor(R.id.score_textview, ctxt.getResources().getColor(R.color.black));
//        row.setImageViewResource(R.id.home_crest, home_crest);
//        row.setImageViewResource(R.id.away_crest, away_crest);


        Log.d(LOG_TAG, home_name);Log.d(LOG_TAG, away_name);;Log.d(LOG_TAG, score);

//        row.setTextViewText(android.R.id.text1, home_name + ": " + score + " :" + away_name);

//        Intent i=new Intent();
//        Bundle extras=new Bundle();
//
//        extras.putString("SCORE", score.toString());
//        i.putExtras(extras);
//        row.setOnClickFillInIntent(android.R.id.text1, i);

        return(row);
    }

    @Override
    public RemoteViews getLoadingView()
    {
        Log.d(LOG_TAG, "getLoadingView");
        return(null);
    }

    @Override
    public int getViewTypeCount()
    {
        Log.d(LOG_TAG, "getViewTypeCount");
        return(1);
    }

    @Override
    public long getItemId(int position)
    {
        Log.d(LOG_TAG, "getItemId");
        return(position);
    }

    @Override
    public boolean hasStableIds()
    {
        Log.d(LOG_TAG, "hasStableIds");
        return(true);
    }

    @Override
    public void onDataSetChanged()
    {
        Log.d(LOG_TAG, "onDataSetChanged");

//        Log.d(LOG_TAG, "onDataSetChanged..Close cursor");
//        if (mCursor != null && !mCursor.isClosed())
//        {
//            mCursor.close();
//            mCursor = null;
//        }
//
//        Log.d(LOG_TAG, "onDataSetChanged..Close loader");
//        // Stop the cursor loader
//        if (mCursorLoader != null) {
//            mCursorLoader.unregisterListener(this);
//            mCursorLoader.cancelLoad();
//            mCursorLoader.stopLoading();
//        }
//
//        Log.d(LOG_TAG, "onDataSetChanged..Reset fragment date");
//        setFragmentDate();
//
//        Log.d(LOG_TAG, "onDataSetChanged..Recreate loader");
//        mCursorLoader = new CursorLoader(ctxt,DatabaseContract.scores_table.buildScoreWithDate(),null,null,fragmentdate,null);
//        mCursorLoader.registerListener(WIDGET_LISTENER, this);
//        mCursorLoader.startLoading();

    }

    @Override
    public void onLoadComplete(android.content.Loader<Cursor> loader, Cursor data)
    {
        // Bind data to UI, etc
        Log.d(LOG_TAG, "onLoadCompltete");

        if(data == null)
            return;
        else
            Log.d(LOG_TAG, "onLoadCompltete " + data.getCount());

        if (data.isClosed())
        {
            Log.d(LOG_TAG, "onLoadCompltete Got a closed cursor from onLoadComplete");
            return;
        }

        this.mCursor = data;
        if(!mCursor.moveToFirst())
        {
            Log.d(LOG_TAG, "..couldn't moveToFirst");

            return;
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctxt);


        int[] widgets = appWidgetManager.getAppWidgetIds(new ComponentName(ctxt, AppWidget.class));

        for(int widget : widgets)
        {
            appWidgetManager.notifyAppWidgetViewDataChanged(widget, R.id.widget_scores_list);
        }

    }
}
