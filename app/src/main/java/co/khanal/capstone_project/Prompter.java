package co.khanal.capstone_project;

import android.content.Intent;
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
        ((TextView)findViewById(R.id.script)).setText(script.getContent());

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
                // TODO: Load settings menu
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
