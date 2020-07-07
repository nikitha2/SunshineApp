package com.nikitha.android.sunshineapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity  implements SharedPreferences.OnSharedPreferenceChangeListener {
    boolean units;
    String location;
    String unitsValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setupSharedPreferences();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: onBackPressed();
                //finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupSharedPreferences() {
        // Get all of the values from shared preferences to set it up
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        units = sharedPreferences.getBoolean(getString(R.string.units_key),getResources().getBoolean(R.bool.pref_show_default));
        location = sharedPreferences.getString(getString(R.string.location_key), getString(R.string.Albany));

        if(units){
            unitsValue=getString(R.string.celsius);
        }
        else{
            unitsValue=getString(R.string.fahrenheit);
        }
        System.out.println("-----------"+unitsValue);
        System.out.println("-----------"+location);
        // Register the listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.units_key))) {
            units = sharedPreferences.getBoolean(getString(R.string.units_key),getResources().getBoolean(R.bool.pref_show_default));
        } else if (key.equals(getString(R.string.location_key))) {
            location = sharedPreferences.getString(getString(R.string.location_key), getString(R.string.Albany));
        }
        if(units){
            unitsValue=getString(R.string.celsius);
        }
        else{
            unitsValue=getString(R.string.fahrenheit);
        }
        System.out.println("-----------"+unitsValue);
        System.out.println("-----------"+location);
    }
}
