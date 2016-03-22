package co.khanal.capstone_project.factories;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import co.khanal.capstone_project.MainActivity;
import co.khanal.capstone_project.R;
import co.khanal.capstone_project.ScriptsProvider;
import co.khanal.capstone_project.utililty.Script;

/**
 * Created by abhi on 3/22/16.
 */
public class ScriptsFactory implements RemoteViewsService.RemoteViewsFactory {

    Cursor cursor;
    Context context;
    int appWidgetId;

    public ScriptsFactory(Context context, Intent intent){
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if(cursor != null){
            cursor.close();
        }

        cursor = context.getContentResolver().query(
                ScriptsProvider.SCRIPT_PROVIDER_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onDestroy() {
        if(cursor != null){
            cursor = null;
        }
    }

    @Override
    public int getCount() {
        return cursor == null ? 0: cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if(position == AdapterView.INVALID_POSITION || cursor == null || !cursor.moveToPosition(position)){
            return null;
        }

        Script script = new Script();

        if(cursor.moveToPosition(position)){
            script = Script.fromCursor(cursor);
        }

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        rv.setTextViewText(R.id.title, script.getFileName());
        String data = script.getContent();
        try {
            data = data.substring(0, 100);
            data += context.getString(R.string.ellipsis);
        } catch (StringIndexOutOfBoundsException e){
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
        rv.setTextViewText(R.id.subtitle, data);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
