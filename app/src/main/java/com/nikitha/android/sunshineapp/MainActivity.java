package com.nikitha.android.sunshineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nikitha.android.sunshineapp.database.TaskEntry;
import com.nikitha.android.sunshineapp.layers.TaskViewModel;
import com.nikitha.android.sunshineapp.layers.TaskViewModelFactory;
import com.nikitha.android.sunshineapp.utilities.NetworkUtils;
import com.nikitha.android.sunshineapp.utilities.SunshineDateUtils;
import com.nikitha.android.sunshineapp.utilities.SunshineWeatherUtils;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.nikitha.android.sunshineapp.sync.ServiceTasks.loadWeatherDataFromWebService;
import static com.nikitha.android.sunshineapp.utilities.SunshineWeatherUtils.finMinMaxTemp;

public class MainActivity extends AppCompatActivity implements /*LoaderManager.LoaderCallbacks<ArrayList<ListItems>> ,*/ AdaptorRecyclerView.ListItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener,  Serializable {
    private static final String TAG = MainActivity.class.getSimpleName();

    NetworkUtils networkUtils;
    TaskViewModel viewModel;
    TextView emptyView;
    boolean units;
    String location;String unitsValue;
    Bundle input=new Bundle();
    URL REQUEST_URL;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManagerRecyclerView;
    AdaptorRecyclerView adaptorRecyclerView;
    //ArrayAdapterSunshine adapter;
    Toast mtoast = null;
    List<TaskEntry> dataInfo;
    static boolean preferencesChanged=false;
    TextView emptyTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emptyView = (TextView)findViewById(R.id.emptyView);
        setContentView(R.layout.activity_forecast);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        recyclerView=(RecyclerView) findViewById(R.id.tv_weather_data);
        networkUtils=new NetworkUtils();
        adaptorRecyclerView=new AdaptorRecyclerView(this,new ArrayList<TaskEntry>(),this);
        layoutManagerRecyclerView=new LinearLayoutManager(this);
        recyclerView.setAdapter(adaptorRecyclerView);
        recyclerView.setLayoutManager(layoutManagerRecyclerView);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        try {
            loadData();
            setupViewModel(input);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void loadData() throws MalformedURLException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        units = sharedPreferences.getBoolean(getString(R.string.units_key), getResources().getBoolean(R.bool.pref_show_default));
        location = sharedPreferences.getString(getString(R.string.location_key), getString(R.string.Albany));
        unitsValue=celOrFar(units);
        REQUEST_URL=networkUtils.buildUrl(location,unitsValue);
        input.putString("url", REQUEST_URL.toString());
    }

    private void setupViewModel(Bundle input) {
        Toast.makeText(this,"Loading data for "+location,Toast.LENGTH_SHORT).show();
        TaskViewModelFactory factory = new TaskViewModelFactory(this,input);
        viewModel = ViewModelProviders.of(this,factory).get(TaskViewModel.class);
        viewModel.getTasks().observe(this, new Observer<List<TaskEntry>>() {
            @Override
            public void onChanged(@Nullable List<TaskEntry> taskEntries) {
                Log.d(TAG, "Updating list of tasks from LiveData in ViewModel");
                dataInfo=taskEntries;
                if(dataInfo.size()!=0){
                    List<TaskEntry> newTasks = finMinMaxTemp(dataInfo);
                    adaptorRecyclerView.setData(newTasks);
                    newTasks.clear();
                   // emptyView.setVisibility(View.INVISIBLE);
                }
                else{
//                    emptyView.setVisibility(View.VISIBLE);
                }
                adaptorRecyclerView.notifyDataSetChanged();
                //optional statement. will work the same without also
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forcast,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_refresh:
                try {
                    loadData();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            case R.id.activity_settings:   Intent intent = new Intent(this, SettingsActivity.class);
                                           startActivity(intent);
        }
        return true;
    }



    @Override
    public void onListItemClick(int position) {
        if(mtoast!=null){
            mtoast.cancel();
        }
        String toastMessage = "Item #" + position + " clicked.";
        mtoast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        mtoast.show();

        Intent intent=new Intent(this,DetailActivity.class);
        Bundle args = new Bundle();
        intent.putExtra("ARRAYLIST",  dataInfo.get(position));
        startActivity(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this) .unregisterOnSharedPreferenceChangeListener(this);
    }

   /* @Override
    protected void onResume() {
        emptyView.setVisibility(View.INVISIBLE);
        super.onResume();
    }*/

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(sharedPreferences !=null){
            preferencesChanged=true;
        }
    }
    @Override
    protected void onStart() {

        super.onStart();
        if(preferencesChanged){
            Log.d(TAG, "onStart: preferences were updated");
            try {
                loadData();
                adaptorRecyclerView.notifyDataSetChanged();
                preferencesChanged=false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
}

    public String celOrFar(boolean units){
        String unitsValue1;
        if(units){
            //F
            unitsValue1=getString(R.string.metric);
        }
        else{
            //C
            unitsValue1=getString(R.string.Imperial);
        }
        return unitsValue1;
    }
}
