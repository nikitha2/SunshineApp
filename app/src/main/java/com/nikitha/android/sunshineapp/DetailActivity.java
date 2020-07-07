package com.nikitha.android.sunshineapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.app.ShareCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.nikitha.android.sunshineapp.database.TaskEntry;
import com.nikitha.android.sunshineapp.layers.TaskViewModel;
import com.nikitha.android.sunshineapp.layers.TaskViewModelDetails;
import com.nikitha.android.sunshineapp.layers.TaskViewModelDetailsFactory;
import com.nikitha.android.sunshineapp.layers.TaskViewModelFactory;
import com.nikitha.android.sunshineapp.utilities.NetworkUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    ShareActionProvider actionProvider;
    private static final Object FORECAST_SHARE_HASHTAG ="Weather For Today" ;
    Intent intent;
    TaskViewModelDetails viewModel;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManagerRecyclerView;
    AdaptorRecyclerViewDetails adaptorRecyclerView;
    List<TaskEntry> taskEntriesData;
    TaskEntry listItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        intent = getIntent();
        listItems = (TaskEntry) getIntent().getSerializableExtra("ARRAYLIST");

        recyclerView=(RecyclerView) findViewById(R.id.tv_weather_dataDetails);
        adaptorRecyclerView=new AdaptorRecyclerViewDetails(this,new ArrayList<TaskEntry>());
        layoutManagerRecyclerView=new LinearLayoutManager(this);
        recyclerView.setAdapter(adaptorRecyclerView);
        recyclerView.setLayoutManager(layoutManagerRecyclerView);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        setupViewModel(listItems);

    }

    private void setupViewModel(final TaskEntry listItems) {
        TaskViewModelDetailsFactory factory = new TaskViewModelDetailsFactory(this);
        viewModel = ViewModelProviders.of(this,factory).get(TaskViewModelDetails.class);
        viewModel.getTasksbyDate(listItems.getDate_txt()).observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(List<TaskEntry> taskEntries) {
               viewModel.getTasksbyDate(listItems.getDate_txt()).removeObserver(this);
                taskEntriesData=taskEntries;
                adaptorRecyclerView.setData(taskEntries);
            }
        });
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean units = sharedPreferences.getBoolean(getString(R.string.units_key), getResources().getBoolean(R.bool.pref_show_default));
        String celfar = celOrFar(units);
        Serializable listItems = getIntent().getSerializableExtra("ARRAYLIST");
        String mForecast = listItems.toString();

        TaskEntry task = taskEntriesData.get(0);
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(FORECAST_SHARE_HASHTAG+"\n"+"Todays Max temp: "+task.getTemp_max()+" "+celfar+"\n"+"Todays Min Temp:" +task.getTemp_min()+" "+celfar+"\n")
                .getIntent();
        return shareIntent;
    }
    public String celOrFar(boolean units){
        String unitsValue1;
        if(units){
            //F
            unitsValue1=getString(R.string.celsius);
        }
        else{
            //C
            unitsValue1=getString(R.string.fahrenheit);
        }
        return unitsValue1;
    }
}
