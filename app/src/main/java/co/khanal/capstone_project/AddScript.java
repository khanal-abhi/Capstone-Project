package co.khanal.capstone_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import co.khanal.capstone_project.utililty.Script;

public class AddScript extends AppCompatActivity {

    Script script;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_script);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.with(getApplicationContext())
                .load(R.drawable.add_script_backdrop)
                .placeholder(R.drawable.backdrop)
                .error(R.drawable.backdrop)
                .into((ImageView) findViewById(R.id.backdrop));

        ((FloatingActionButton)findViewById(R.id.save_script)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = ((TextView)findViewById(R.id.title)).getText().toString();
                filename = filename.trim();
                String content = ((TextView)findViewById(R.id.content)).getText().toString();
                script = new Script(
                        filename,
                        content
                );
                new SaveQuery().execute(script);
                Intent intent = new Intent();
                intent.putExtra(Script.KEY, script);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
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



}
