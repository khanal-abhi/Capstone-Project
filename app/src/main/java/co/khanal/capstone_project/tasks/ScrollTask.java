package co.khanal.capstone_project.tasks;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;

import co.khanal.capstone_project.Prompter;

/**
 * Created by abhi on 3/22/16.
 */
public class ScrollTask extends AsyncTask<Pair<View, Integer>, Pair<View, Integer>, View> {

    boolean keepScrolling;

    public ScrollTask(){
        this.keepScrolling = true;
    }

    @Override
    protected View doInBackground(Pair<View, Integer>... params) {
        View view = params[0].first;
        int scrollRate = params[0].second;
        while(keepScrolling){
            try{
                Thread.sleep(50);
            } catch (InterruptedException e){
                Log.e(getClass().getSimpleName(), e.getMessage());
            }
            publishProgress(Pair.create(view, scrollRate));
        }
        try{
            Thread.sleep(10000);
        } catch (InterruptedException e){
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
        return view;
    }

    @Override
    protected void onProgressUpdate(Pair<View, Integer>... values) {
        View view = values[0].first;
        int scrollRate = values[0].second;
        if(view.canScrollVertically(scrollRate)){
            view.scrollBy(0, scrollRate);
        } else {
            keepScrolling = false;
        }
    }

    @Override
    protected void onPostExecute(View view) {
        ((Prompter)view.getTag()).returnFromFullScreen();
        Snackbar.make(
                view.getRootView(),
                "Script has ended",
                Snackbar.LENGTH_SHORT
        ).show();
    }
}
