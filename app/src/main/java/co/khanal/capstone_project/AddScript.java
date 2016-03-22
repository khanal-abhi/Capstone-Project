package co.khanal.capstone_project;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import co.khanal.capstone_project.utililty.Script;

public class AddScript extends AppCompatActivity {

    Script script;
    CoordinatorLayout coordinatorLayout;

    AdView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_script);

        banner = (AdView)findViewById(R.id.banner_ad);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("41dabf86")
                .build();

        banner.loadAd(adRequest);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.with(getApplicationContext())
                .load(R.drawable.add_script_backdrop)
                .placeholder(R.drawable.add_script_backdrop)
                .error(R.drawable.backdrop)
                .into((ImageView) findViewById(R.id.backdrop));

        FloatingActionButton fab = ((FloatingActionButton) findViewById(R.id.save_script));

        if(fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String filename = ((TextView) findViewById(R.id.title)).getText().toString();
                    filename = filename.trim();
                    String content = ((TextView) findViewById(R.id.content)).getText().toString();
                    script = new Script(
                            filename,
                            content
                    );
                    Intent intent = new Intent();

                    if (filename.length() > 0) {
                        if (content.trim().length() > 0) {
                            new SaveQuery().execute(script);
                            intent.putExtra(Script.KEY, script);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            Snackbar.make(v.getRootView().findViewById(R.id.coordinator_layout), getString(R.string.script_save_no_content), Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(v.getRootView().findViewById(R.id.coordinator_layout), getString(R.string.script_save_no_filename), Snackbar.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            script = savedInstanceState.getParcelable(Script.KEY);
            ((TextView)findViewById(R.id.title)).setText(savedInstanceState.getString(getString(R.string.script_title_input)));
            ((TextView)findViewById(R.id.content)).setText(savedInstanceState.getString(getString(R.string.script_content_input)));
            if(savedInstanceState.getBoolean(getString(R.string.script_title_label))){
                ((TextView)findViewById(R.id.title)).requestFocus();
            }

            if(savedInstanceState.getBoolean(getString(R.string.script_content_label))){
                ((TextView)findViewById(R.id.content)).requestFocus();
            }
        }
    }

    public class SaveQuery extends AsyncTask<Script, Void, Void>{

        @Override
        protected Void doInBackground(Script... params) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ScriptsProvider.FILENAME, script.getFileName());
            contentValues.put(ScriptsProvider.CONTENT, script.getContent());
            getContentResolver().insert(
                    ScriptsProvider.SCRIPT_PROVIDER_URI,
                    contentValues
            );
            return null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String scriptTitle = ((TextView)findViewById(R.id.title)).getText().toString();
        String scriptContent = ((TextView)findViewById(R.id.content)).getText().toString();
        outState.putParcelable(Script.KEY, script);
        outState.putString(getString(R.string.script_title_input), scriptTitle);
        outState.putString(getString(R.string.script_content_input), scriptContent);
        outState.putBoolean(getString(R.string.script_title_label), ((TextView) findViewById(R.id.title)).isFocused());
        outState.putBoolean(getString(R.string.script_content_label), ((TextView)findViewById(R.id.content)).isFocused());
    }
}
