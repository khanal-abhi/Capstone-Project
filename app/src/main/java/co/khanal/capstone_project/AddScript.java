package co.khanal.capstone_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import co.khanal.capstone_project.utililty.Script;

public class AddScript extends AppCompatActivity {

    Script script;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            script = new Script(
                    bundle.getString(Script.FILENAME_KEY),
                    bundle.getString(Script.CONTENT_KEY)
            );
            Toast.makeText(getApplicationContext(), script.toString(), Toast.LENGTH_SHORT).show();
        }

        setContentView(R.layout.activity_add_script);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
