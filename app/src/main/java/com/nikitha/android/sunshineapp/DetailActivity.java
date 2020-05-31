package com.nikitha.android.sunshineapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.app.ShareCompat;
import androidx.core.view.MenuItemCompat;
import androidx.loader.app.LoaderManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    ShareActionProvider actionProvider;
    private static final Object FORECAST_SHARE_HASHTAG ="WeatherForToday" ;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        ListItems listItems = (ListItems) getIntent().getSerializableExtra("ARRAYLIST");
        TextView text=(TextView) findViewById(R.id.text);
        text.setText(listItems.getDate() +" "+listItems.getDescription() );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent1=new Intent();
        switch(item.getItemId()){
            case R.id.share:  intent1=createShareForecastIntent();
                              intent1.setAction(Intent.ACTION_SEND);
                              startActivity(intent1);
                              return true;
            case android.R.id.home://this.finish();
                                    NavUtils.navigateUpFromSameTask(this);
                                    return true;

            case R.id.activity_settings:Intent intent = new Intent(this, SettingsActivity.class);
                                        startActivity(intent);
        }
        return true;
    }

    private Intent createShareForecastIntent() {
        ListItems listItems = (ListItems) getIntent().getSerializableExtra("ARRAYLIST");
        ListItems mForecast = listItems;

        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecast.getDate() + FORECAST_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }

}
