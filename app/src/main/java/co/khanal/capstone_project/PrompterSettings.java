package co.khanal.capstone_project;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import co.khanal.capstone_project.utililty.Script;

public class PrompterSettings extends AppCompatActivity {

    Spinner textColor, color, fontSize, scrollRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompter_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setSpinners();

    }

    public void setSpinners(){
        textColor = (Spinner)findViewById(R.id.text_color);
        color = (Spinner)findViewById(R.id.color);
        fontSize = (Spinner)findViewById(R.id.font_size);
        scrollRate = (Spinner)findViewById(R.id.scroll_rate);

        ArrayAdapter<CharSequence> textColorAdapter = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.selection_colors_string,
                android.R.layout.simple_spinner_item
        );
        textColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        textColor.setAdapter(textColorAdapter);


        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.selection_colors_string,
                android.R.layout.simple_spinner_item
        );
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color.setAdapter(colorAdapter);

        ArrayAdapter<CharSequence> fontSizeAdapter = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.sizes_string,
                android.R.layout.simple_spinner_item
        );
        fontSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fontSize.setAdapter(fontSizeAdapter);

        ArrayAdapter<CharSequence> scrollRateAdapter = ArrayAdapter.createFromResource(
                getApplicationContext(),
                R.array.rate_string,
                android.R.layout.simple_spinner_item
        );
        scrollRateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scrollRate.setAdapter(scrollRateAdapter);

        final TypedArray colors = getResources().obtainTypedArray(R.array.selection_colors);
        final TypedArray sizes = getResources().obtainTypedArray(R.array.sizes);
        final TypedArray rates = getResources().obtainTypedArray(R.array.rate);

        final SharedPreferences preferences = getSharedPreferences(Script.KEY, MODE_PRIVATE);
        int textColorInt = preferences.getInt(getString(R.string.text_color), 5);
        int colorInt = preferences.getInt(getString(R.string.color), 4);
        int fontSizeInt = preferences.getInt(getString(R.string.font_size), 0);
        int scrollRateInt = preferences.getInt(getString(R.string.scroll_rate), 0);

        textColor.setSelection(textColorInt);
        color.setSelection(colorInt);
        fontSize.setSelection(fontSizeInt);
        scrollRate.setSelection(scrollRateInt);


        textColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) findViewById(R.id.text_color_label)).setTextColor(colors.getColor(position, 0));
                ((TextView) findViewById(R.id.color_label)).setTextColor(colors.getColor(position, 0));

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(getString(R.string.text_color), position);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) findViewById(R.id.color_label)).setBackgroundColor(colors.getColor(position, 0));
                ((TextView) findViewById(R.id.text_color_label)).setBackgroundColor(colors.getColor(position, 0));

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(getString(R.string.color), position);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)findViewById(R.id.font_size_label)).setTextSize(sizes.getDimensionPixelSize(position, 24));

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(getString(R.string.font_size), position);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        scrollRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) findViewById(R.id.scroll_rate_label)).setText(getString(R.string.scroll_rate) + rates.getFloat(position, 1f));

                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(getString(R.string.scroll_rate), position);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}
