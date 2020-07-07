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
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    ShareActionProvider actionProvider;
    private static final Object FORECAST_SHARE_HASHTAG ="WeatherForToday" ;
    Intent intent;
    TaskViewModelDetails viewModel;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManagerRecyclerView;
    AdaptorRecyclerViewDetails adaptorRecyclerView;
    List<TaskEntry> taskEntriesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        intent = getIntent();
        final TaskEntry listItems = (TaskEntry) getIntent().getSerializableExtra("ARRAYLIST");

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
        ListItems listItems = (ListItems) getIntent().getSerializableExtra("ARRAYLIST");
        ListItems mForecast = listItems;

        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecast.getDate() + FORECAST_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }

}
