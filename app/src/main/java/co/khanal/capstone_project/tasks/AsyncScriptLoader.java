package co.khanal.capstone_project.tasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import co.khanal.capstone_project.ScriptsProvider;
import co.khanal.capstone_project.utililty.Script;

/**
 * Created by abhi on 3/22/16.
 */
public class AsyncScriptLoader extends AsyncTaskLoader<List<Script>> {

    private List<Script> scripts;

    public AsyncScriptLoader(Context context) {
        super(context);
    }

    @Override
    public List<Script> loadInBackground() {
        Cursor cursor = getContext().getContentResolver().query(
                ScriptsProvider.SCRIPT_PROVIDER_URI,
                null,
                null,
                null,
                null
        );
        if(cursor == null)
            return null;
        if(cursor .moveToFirst()){
            List<Script> scripts = new ArrayList<>();
            do{
                scripts.add(new Script(
                        cursor.getLong(ScriptsProvider.ID_INDEX),
                        cursor.getString(ScriptsProvider.FILENAME_INDEX),
                        cursor.getString(ScriptsProvider.CONTENT_INDEX)
                ));
            } while (cursor.moveToNext());

            return scripts;
        }
        return null;
    }

    @Override
    public void deliverResult(List<Script> data) {

        if(isReset()){
            data = null;
            return;
        }

        if(isStarted()){
            scripts = data;
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if(scripts != null){
            deliverResult(scripts);
        }

        if(takeContentChanged() || scripts == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if(scripts != null){
            scripts = null;
        }
    }

    @Override
    public void onCanceled(List<Script> data) {
        super.onCanceled(data);
    }
}

