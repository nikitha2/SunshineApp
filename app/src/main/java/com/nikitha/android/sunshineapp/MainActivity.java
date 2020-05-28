package com.nikitha.android.sunshineapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nikitha.android.sunshineapp.utilities.NetworkUtils;
import com.nikitha.android.sunshineapp.utilities.SunshineLoader;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<ListItems>> , AdaptorRecyclerView.ListItemClickListener{
    NetworkUtils networkUtils;
    Bundle input=new Bundle();
    URL REQUEST_URL;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManagerRecyclerView;
    AdaptorRecyclerView adaptorRecyclerView;
    //ArrayAdapterSunshine adapter;
    Toast mtoast = null;

    TextView emptyTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        recyclerView=(RecyclerView) findViewById(R.id.tv_weather_data);
        networkUtils=new NetworkUtils();
        try {
            REQUEST_URL=networkUtils.buildUrl("London");
            input.putString("url", REQUEST_URL.toString());
            Log.i("initLoader started","TEST:initLoader started");
            LoaderManager.getInstance(this).initLoader(1, input, this).forceLoad();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //adapter=new ArrayAdapterSunshine(this,new ArrayList<ListItems>());
        adaptorRecyclerView=new AdaptorRecyclerView(new ArrayList<ListItems>(),this);
        layoutManagerRecyclerView=new LinearLayoutManager(this);
        recyclerView.setAdapter(adaptorRecyclerView);
        recyclerView.setLayoutManager(layoutManagerRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forcast,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_refresh:LoaderManager.getInstance(this).initLoader(1, input, this).forceLoad();
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
    }
}
