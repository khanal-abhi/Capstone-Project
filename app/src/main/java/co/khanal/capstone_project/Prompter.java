package co.khanal.capstone_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import co.khanal.capstone_project.utililty.Script;

public class Prompter extends AppCompatActivity {

    private boolean isFullscreen;
    Script script;
    List<View> nonFullScreenViews;
//    float scrollRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompter);

        isFullscreen = false;
        nonFullScreenViews = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        loadScript(bundle);

        setUpToolbar();
        setUpFabs();

        TextView scriptContent = ((TextView) findViewById(R.id.script_content));
        if(scriptContent != null)
            scriptContent.setText(script.getContent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_prompter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
//                Intent intent = new Intent(getApplicationContext(), PrompterSettings.class);
//                startActivity(intent);
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        if(isFullscreen){
            returnFromFullScreen();
        } else {
            super.onBackPressed();
        }
    }

    public void goFullScreen(){
        for(View view : nonFullScreenViews){
            view.setVisibility(View.GONE);
        }
        isFullscreen = true;
    }

    public void returnFromFullScreen(){
        for(View view : nonFullScreenViews){
            view.setVisibility(View.VISIBLE);
        }
        isFullscreen = false;
    }

    View.OnClickListener fabShare = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, script.getFileName() + getString(R.string.endl) + script.getContent());
            intent.setType(getString(R.string.mime_plain));
            startActivity(intent);
        }
    };

    View.OnClickListener fabPlay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goFullScreen();
        }
    };

    public void setUpToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle(script.getFileName().toUpperCase());
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null)
                actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            nonFullScreenViews.add(toolbar);
        }

    }

    public void setUpFabs(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_play);
        if(fab != null)
            fab.setOnClickListener(this.fabPlay);

        FloatingActionButton fabShare = (FloatingActionButton)findViewById(R.id.fab_share);
        if(fabShare != null)
            fabShare.setOnClickListener(this.fabShare);

        nonFullScreenViews.add(fab);
        nonFullScreenViews.add(fabShare);
    }

    public void loadScript(Bundle bundle){
        if(bundle != null){
            script = bundle.getParcelable(Script.KEY);
        } else {
            script = null;
        }
    }

}
