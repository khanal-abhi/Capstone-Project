package co.khanal.capstone_project;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by abhi on 3/22/16.
 */
public class AnalyticsApplication extends Application {
    private Tracker tracker;

    synchronized public Tracker getDefaultTracker(){
        if(tracker == null){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.global_tracker);
        }
        return tracker;
    }
}
