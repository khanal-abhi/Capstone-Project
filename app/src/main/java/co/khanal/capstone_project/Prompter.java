package co.khanal.capstone_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import co.khanal.capstone_project.utililty.Script;

public class Prompter extends AppCompatActivity {

    private boolean isFullscreen;
    Script script;
    List<View> nonFullScreenViews;
    float scrollRate;
    float fontSize;
    int textColor;
    int color;

    TextView scriptContent;

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

        scriptContent = ((TextView) findViewById(R.id.script_content));
        if(scriptContent != null)
            scriptContent.setText(script.getContent());

        loadDefaults();

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
            case R.id.toggle_colors:
                toggleColors();
                break;
            case R.id.change_font_size:
                changeFontSize();
                break;
            case R.id.increase_scroll_rate:
                increaseScrollRate();
                break;
            case R.id.decrease_scroll_rate:
                decreaseScrollRate();
                break;
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
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("");
            }
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

    public void toggleColors(){
        if(textColor == Color.BLACK){
            textColor = Color.WHITE;
            color = Color.BLACK;
        } else {
            textColor = Color.BLACK;
            color = Color.WHITE;
        }
        savePreferences();
        applyPreferences();
    }

    public void changeFontSize(){
        fontSize += 10f;
        if(fontSize > 120f)
            fontSize = 40f;
        savePreferences();
        applyPreferences();
    }

    public void increaseScrollRate(){
        scrollRate += .2f;
        if(scrollRate > 4f)
            scrollRate = 4f;
        savePreferences();
        applyPreferences();
    }

    public void decreaseScrollRate(){
        scrollRate -= .1f;
        if(scrollRate < .33f)
            scrollRate = .33f;
        savePreferences();
        applyPreferences();
    }

    public void savePreferences(){
        SharedPreferences.Editor editor = getSharedPreferences(Script.KEY, MODE_PRIVATE).edit();
        editor.putInt(getString(R.string.text_color), textColor);
        editor.putInt(getString(R.string.color), color);
        editor.putFloat(getString(R.string.font_size), fontSize);
        editor.putFloat(getString(R.string.scroll_rate), scrollRate);
        editor.apply();
    }



    public void loadDefaults(){
        SharedPreferences preferences = getSharedPreferences(Script.KEY, MODE_PRIVATE);
        textColor = preferences.getInt(getString(R.string.text_color), Color.BLACK);
        color = preferences.getInt(getString(R.string.color), Color.WHITE);
        fontSize = preferences.getFloat(getString(R.string.font_size), 26f);
        scrollRate = preferences.getFloat(getString(R.string.scroll_rate), 1f);

        applyPreferences();
    }

    public void applyPreferences(){
        if(scriptContent != null){
            scriptContent.setTextColor(textColor);
            scriptContent.setBackgroundColor(color);
            scriptContent.setTextSize(fontSize);
            // TODO: Apply scroll anim speed here
        }
    }

}
