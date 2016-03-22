package co.khanal.capstone_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import co.khanal.capstone_project.adapters.ScriptsRecyclerViewAdapter;
import co.khanal.capstone_project.utililty.Script;

public class MainActivity extends AppCompatActivity {

    public static int ADD_SCRIPT_REQUEST = 0;

    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
        new LoadScripts().execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   loadAddScript();

                }
            });


    }

    public void loadAddScript(){
        Intent intent = new Intent(getApplicationContext(), AddScript.class);
        startActivityForResult(intent, ADD_SCRIPT_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_SCRIPT_REQUEST && resultCode == RESULT_OK){
            Script script = data.getParcelableExtra(Script.KEY);
            Snackbar.make(
                    coordinatorLayout,
                    getString(R.string.script_saved),
                    Snackbar.LENGTH_SHORT
            ).show();
            new LoadScripts().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

        }

        return super.onOptionsItemSelected(item);
    }

    public class LoadScripts extends AsyncTask<Void, Void, List<Script>>{

        @Override
        protected List<Script> doInBackground(Void... params) {

            List<Script> scripts = new ArrayList<>();

            Cursor cursor = getContentResolver().query(
                    ScriptsProvider.SCRIPT_PROVIDER_URI,
                    null,
                    null,
                    null,
                    null
            );

            if(cursor != null){
                if(cursor.moveToFirst()){
                    do {
                        scripts.add(new Script(
                                cursor.getLong(ScriptsProvider.ID_INDEX),
                                cursor.getString(ScriptsProvider.FILENAME_INDEX),
                                cursor.getString(ScriptsProvider.CONTENT_INDEX)
                        ));
                    } while(cursor.moveToNext());
                }
            }
            return scripts;
        }

        @Override
        protected void onPostExecute(List<Script> scripts) {
            super.onPostExecute(scripts);
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(new ScriptsRecyclerViewAdapter(
                    scripts,
                    new ScriptsRecyclerViewAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Script script) {
                            Intent intent = new Intent(getApplicationContext(), Prompter.class);
                            intent.putExtra(Script.KEY, script);
                            startActivity(intent);

                        }
                    },

                    new ScriptsRecyclerViewAdapter.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(final Script script) {

                            Snackbar.make(
                                    coordinatorLayout,
                                    getString(R.string.question_delete_script),
                                    Snackbar.LENGTH_LONG
                            ).setAction(getString(R.string.delete), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getContentResolver().delete(
                                            ScriptsProvider.SCRIPT_PROVIDER_URI,
                                            null,
                                            new String[]{
                                                    null,
                                                    script.getFileName(),
                                                    null
                                            }
                                    );
                                    Snackbar.make(
                                            coordinatorLayout,
                                            getString(R.string.script_deleted),
                                            Snackbar.LENGTH_SHORT
                                    ).show();
                                    new LoadScripts().execute();
                                }
                            }).show();
                            return true;
                        }
                    }
            ));
        }
    }
}
