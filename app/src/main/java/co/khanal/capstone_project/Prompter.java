package co.khanal.capstone_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import co.khanal.capstone_project.tasks.ScrollTask;
import co.khanal.capstone_project.utililty.Script;

public class Prompter extends AppCompatActivity {

    private boolean isFullscreen;
    Script script;
    List<View> nonFullScreenViews;
    float scrollRate;
    float fontSize;
    int textColor;
    int color;

    ScrollView scrollView;
    TextView scriptContent;
    ScrollTask scrollTask;

    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompter);

        tracker = ((AnalyticsApplication) getApplication()).getDefaultTracker();

        scrollView = (ScrollView)findViewById(R.id.scroll_view);
        scrollView.setTag(this);
        scriptContent = ((TextView) findViewById(R.id.script_content));

        isFullscreen = false;
        nonFullScreenViews = new ArrayList<>();
        setUpToolbar();
        setUpFabs();

        if(savedInstanceState != null){
            loadScript(savedInstanceState);
            scrollView.scrollTo(0, savedInstanceState.getInt(getString(R.string.scroll_y)));
            applyPreferences();
        } else {

            Bundle bundle = getIntent().getExtras();
            loadScript(bundle);
            loadDefaults();
        }

        if(scriptContent != null)
            scriptContent.setText(script.getContent());


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        script = savedInstanceState.getParcelable(Script.KEY);
        textColor = savedInstanceState.getInt(getString(R.string.text_color));
        color = savedInstanceState.getInt(getString(R.string.color));
        fontSize = savedInstanceState.getFloat(getString(R.string.font_size));
        scrollRate = savedInstanceState.getFloat(getString(R.string.scroll_rate));
        scrollView.scrollTo(0, savedInstanceState.getInt(getString(R.string.scroll_y)));
        applyPreferences();
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

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.action))
                .setAction(getString(R.string.play_script))
                .setValue(1)
                .build());

        for(View view : nonFullScreenViews){
            view.setVisibility(View.GONE);
        }
        isFullscreen = true;
        scrollTask = new ScrollTask();
        scrollTask.execute(Pair.create((View) scrollView, (int) (2 * scrollRate)));
    }

    public void returnFromFullScreen(){
        for(View view : nonFullScreenViews){
            view.setVisibility(View.VISIBLE);
        }
        isFullscreen = false;
        if(scrollTask != null){
            scrollTask.cancel(true);
            scrollTask = null;
        }

    }

    View.OnClickListener fabShare = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(getString(R.string.action))
                    .setAction(getString(R.string.share_script))
                    .setValue(1)
                    .build());

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

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.action))
                .setAction(getString(R.string.toggle_colors_action))
                .setValue(1)
                .build());

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

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.action))
                .setAction(getString(R.string.change_font_size_action))
                .setValue(1)
                .build());

        fontSize += 10f;
        if(fontSize > 120f)
            fontSize = 40f;
        savePreferences();
        applyPreferences();
    }

    public void increaseScrollRate(){

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.action))
                .setAction(getString(R.string.increase_scroll_rate_action))
                .setValue(1)
                .build());

        scrollRate *= 1.2f;
        if(scrollRate > 6f)
            scrollRate = 6f;
        savePreferences();
        applyPreferences();
    }

    public void decreaseScrollRate(){

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.action))
                .setAction(getString(R.string.decrease_scroll_rate_action))
                .setValue(1)
                .build());

        scrollRate /= 1.2f;
        if(scrollRate < .5f)
            scrollRate = .5f;
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
            ((ScrollView)findViewById(R.id.scroll_view)).setBackgroundColor(color);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Script.KEY, script);
        outState.putInt(getString(R.string.text_color), textColor);
        outState.putInt(getString(R.string.color), color);
        outState.putFloat(getString(R.string.font_size), fontSize);
        outState.putFloat(getString(R.string.scroll_rate), scrollRate);
        outState.putInt(getString(R.string.scroll_y), scrollView.getScrollY());
        if(scrollTask != null){
            scrollTask.cancel(true);
            scrollTask = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tracker.setScreenName(getClass().getName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
