package co.khanal.capstone_project;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import co.khanal.capstone_project.adapters.ScriptsCursorAdapter;
import co.khanal.capstone_project.utililty.Script;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static int ADD_SCRIPT_REQUEST = 0;
    public static final int CURSOR_LOADER_ID = 0;

    CoordinatorLayout coordinatorLayout;
    ScriptsCursorAdapter adapter;

    Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnalyticsApplication application = (AnalyticsApplication)getApplication();
        tracker = application.getDefaultTracker();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if(fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   loadAddScript();

                }
            });

        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new ScriptsCursorAdapter(
                getApplicationContext(),
                null,
                new ScriptsCursorAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Script script) {

                        tracker.send(new HitBuilders.EventBuilder()
                                .setCategory(getString(R.string.action))
                                .setAction(getString(R.string.load_script))
                                .setValue(1)
                                .build());

                        Intent intent = new Intent(getApplicationContext(), Prompter.class);
                        intent.putExtra(Script.KEY, script);
                        startActivity(intent);
                    }
                },
                new ScriptsCursorAdapter.OnItemLongClickListener() {
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
                                tracker.send(new HitBuilders.EventBuilder()
                                        .setCategory(getString(R.string.action))
                                        .setAction(getString(R.string.delete_script))
                                        .setValue(1)
                                        .build());
                                adapter.changeCursor(updateCursor());
                            }
                        }).show();
                        return true;
                    }
                }

        );
        recyclerView.setAdapter(adapter);

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        try {
            Picasso.with(getApplicationContext())
                    .load(R.drawable.backdrop)
                    .placeholder(R.drawable.add_script_backdrop)
                    .error(R.drawable.backdrop)
                    .into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e){
            Log.e(getClass().getSimpleName(), e.getMessage());
        }

    }

    public void loadAddScript() {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.action))
                .setAction(getString(R.string.add_script))
                .setValue(1)
                .build());

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
            adapter.changeCursor(updateCursor());
        }
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case CURSOR_LOADER_ID:
                return new CursorLoader(
                        getApplicationContext(),
                        ScriptsProvider.SCRIPT_PROVIDER_URI,
                        null,
                        null,
                        null,
                        null
                );

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(adapter != null){
            adapter.changeCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(adapter != null){
            adapter.changeCursor(null);
        }
    }

    public Cursor updateCursor(){
        return getContentResolver().query(
                ScriptsProvider.SCRIPT_PROVIDER_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        tracker.setScreenName(getClass().getName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        adapter.changeCursor(updateCursor());
    }
}
