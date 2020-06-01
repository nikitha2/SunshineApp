package com.nikitha.android.sunshineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nikitha.android.sunshineapp.utilities.NetworkUtils;
import com.nikitha.android.sunshineapp.utilities.SunshineLoader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<ListItems>> , AdaptorRecyclerView.ListItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    NetworkUtils networkUtils;
    private static final String TAG = MainActivity.class.getSimpleName();
    boolean units;
    String location;String unitsValue;
    Bundle input=new Bundle();
    URL REQUEST_URL;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManagerRecyclerView;
    AdaptorRecyclerView adaptorRecyclerView;
    //ArrayAdapterSunshine adapter;
    Toast mtoast = null;
    ArrayList<ListItems> dataInfo=new ArrayList<ListItems>();
    static boolean preferencesChanged=false;
    TextView emptyTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        recyclerView=(RecyclerView) findViewById(R.id.tv_weather_data);
        networkUtils=new NetworkUtils();
        try {
            loadDataWithLoader();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //adapter=new ArrayAdapterSunshine(this,new ArrayList<ListItems>());
        adaptorRecyclerView=new AdaptorRecyclerView(new ArrayList<ListItems>(),this);
        layoutManagerRecyclerView=new LinearLayoutManager(this);
        recyclerView.setAdapter(adaptorRecyclerView);
        recyclerView.setLayoutManager(layoutManagerRecyclerView);
    }

    private void loadDataWithLoader() throws MalformedURLException {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        units = sharedPreferences.getBoolean(getString(R.string.units_key), getResources().getBoolean(R.bool.pref_show_default));
        location = sharedPreferences.getString(getString(R.string.location_key), getString(R.string.Albany));
        unitsValue=celOrFar(units);
        REQUEST_URL=networkUtils.buildUrl(location,unitsValue);
        input.putString("url", REQUEST_URL.toString());
        Log.i("initLoader started","TEST:initLoader started");
        Toast.makeText(this,"Loading data for "+location,Toast.LENGTH_SHORT).show();
        LoaderManager.getInstance(this).initLoader(1, input, this).forceLoad();
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
                    loadDataWithLoader();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            case R.id.activity_settings:   Intent intent = new Intent(this, SettingsActivity.class);
                                           startActivity(intent);
        }
        return true;
    }


    @NonNull
    @Override
    public Loader<ArrayList<ListItems>> onCreateLoader(int id, @Nullable Bundle args) {
        return new SunshineLoader(this, REQUEST_URL.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<ListItems>> loader, ArrayList<ListItems> data) {
        Toast.makeText(this,"Data loaded/refreshed",Toast.LENGTH_SHORT).show();
        emptyTextView=(TextView) findViewById(R.id.emptyView);
        if(data!=null && data.size()>0){
            //adapter.clear();
            dataInfo=data;
            adaptorRecyclerView.setData(data);
            recyclerView.setAdapter(adaptorRecyclerView);
            emptyTextView.setVisibility(View.INVISIBLE);
        }
        else{
            emptyTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
            if(networkInfo==null){
                emptyTextView.setText(getResources().getString(R.string.noDatabczNoInternet));
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<ListItems>> loader) {
        adaptorRecyclerView.setData(new ArrayList<ListItems>());
        recyclerView.setAdapter(adaptorRecyclerView);
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
        intent.putExtra("ARRAYLIST",dataInfo.get(position));
        startActivity(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this) .unregisterOnSharedPreferenceChangeListener(this);
    }
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
                loadDataWithLoader();
                preferencesChanged=false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }

    public String celOrFar(boolean units){
        String unitsValue1;
        if(units){
            unitsValue1=getString(R.string.metric);
        }
        else{
            unitsValue1=getString(R.string.Imperial);
        }
        return unitsValue1;
    }
}
