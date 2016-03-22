package co.khanal.capstone_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

import co.khanal.capstone_project.utililty.Script;

public class Prompter extends AppCompatActivity {

    private boolean isFullscreen;
    Script script;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompter);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            script = bundle.getParcelable(Script.KEY);
        } else {
            script = null;
        }

        SharedPreferences preferences = getSharedPreferences(Script.KEY, MODE_PRIVATE);

        final TypedArray colors = getResources().obtainTypedArray(R.array.selection_colors);
        final TypedArray sizes = getResources().obtainTypedArray(R.array.sizes);
        final TypedArray rates = getResources().obtainTypedArray(R.array.rate);

        int textColorInt = preferences.getInt(getString(R.string.text_color), 5);
        int colorInt = preferences.getInt(getString(R.string.color), 4);
        int fontSizeInt = preferences.getInt(getString(R.string.font_size), 0);
        int scrollRateInt = preferences.getInt(getString(R.string.scroll_rate), 0);

        final TextView scriptContent = (TextView)findViewById(R.id.script);

        isFullscreen = false;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(script.getFileName().toUpperCase());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        scriptContent.setText(script.getContent());
        scriptContent.setTextSize(sizes.getDimensionPixelSize(fontSizeInt, 0));
        scriptContent.setBackgroundColor(colors.getColor(colorInt, 4));
        scriptContent.setTextColor(colors.getColor(textColorInt, 5));

        preferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                scriptContent.setTextColor(colors.getColor(sharedPreferences.getInt(getString(R.string.text_color), 5), 5));
                scriptContent.setBackgroundColor(colors.getColor(sharedPreferences.getInt(getString(R.string.color), 4), 4));
                scriptContent.setTextSize(sizes.getDimensionPixelSize(sharedPreferences.getInt(getString(R.string.font_size), 0), 0));
                // TODO: add the scroll implementation\
            }
        });

        colors.recycle();
        sizes.recycle();
        rates.recycle();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_play);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFullscreen = true;
                findViewById(R.id.fab_play).setVisibility(View.GONE);
                findViewById(R.id.fab_share).setVisibility(View.GONE);
                findViewById(R.id.toolbar).setVisibility(View.GONE);
            }
        });

        FloatingActionButton fabShare = (FloatingActionButton)findViewById(R.id.fab_share);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, script.getFileName() + getString(R.string.endl) + script.getContent());
                intent.setType(getString(R.string.mime_plain));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), PrompterSettings.class);
                startActivity(intent);
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        if(isFullscreen){
            isFullscreen = false;
            findViewById(R.id.fab_play).setVisibility(View.VISIBLE);
            findViewById(R.id.fab_share).setVisibility(View.VISIBLE);
            findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

}
