package co.khanal.capstone_project.services;

import android.content.Intent;
import android.widget.RemoteViewsService;

import co.khanal.capstone_project.factories.ScriptsFactory;

/**
 * Created by abhi on 3/22/16.
 */
public class ScriptsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScriptsFactory(getApplicationContext(), intent);
    }
}
