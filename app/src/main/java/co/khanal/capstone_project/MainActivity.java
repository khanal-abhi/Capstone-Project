package co.khanal.capstone_project;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import co.khanal.capstone_project.adapters.ScriptAdapter;
import co.khanal.capstone_project.utililty.Script;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        setSupportActionBar(toolbar);

        Script[] scripts = new Script[]{
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh "),
                new Script("hello world", "lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh, lorem ipsum, blah blah blah blah balh blah balh blah blah blah blah balh blah balh ")

        };

        ListView listView = (ListView)findViewById(R.id.listview);
        ScriptAdapter adapter = new ScriptAdapter(getApplicationContext(), R.layout.individual_script, scripts);
        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        collapsingToolbarLayout.setTitle(getString(R.string.app_name));

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
