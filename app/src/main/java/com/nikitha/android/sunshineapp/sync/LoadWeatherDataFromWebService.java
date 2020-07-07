package com.nikitha.android.sunshineapp.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.util.Log;

import com.nikitha.android.sunshineapp.R;
import com.nikitha.android.sunshineapp.data.SunshinePreferences;
import com.nikitha.android.sunshineapp.database.AppDatabase;
import com.nikitha.android.sunshineapp.database.TaskEntry;
import com.nikitha.android.sunshineapp.utilities.NotificationUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import androidx.annotation.Nullable;

public class LoadWeatherDataFromWebService  extends Service {
    private final String LOG_TAG=LoadWeatherDataFromWebService.class.getSimpleName();
    private Executor executor;
    AppDatabase database;
    String uri="";
    String dt_txt="";
    String time="";
    JSONObject main;
    String temp="";
    String temp_kf="";
    String temp_max="";
    String temp_min="";
    String sea_level="";
    String pressure="";
    String humidity="";
    String grnd_level="";
    String feels_like="";
    String windSpeed="";
    String description="";
    String id="";

    Context context;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "--------------------service created!");
        executor = MyApplication.getInstance().executorService;
        context=getApplicationContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "--------------------service started!");

        final String uri= intent.getStringExtra("uri");
        final ArrayList<TaskEntry>[] networkResponse = new ArrayList[]{new ArrayList<TaskEntry>()};
        final URL[] url = {null};
        database = AppDatabase.getInstance(getApplicationContext().getApplicationContext());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if(uri!=null) {
                        url[0] =createURL(uri); }
                    else{
                        url[0] =null; } }
                catch (MalformedURLException e) {
                    e.printStackTrace(); }

                String jsonResponse = "";
                try {
                    jsonResponse = makeHttpRequest(url[0]);

                    // just for testing purpose- remove later
                    if(jsonResponse.isEmpty() || jsonResponse.equalsIgnoreCase("")){
                        jsonResponse=getFakeData();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    networkResponse[0] = extractFeatureFromJson(jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                database.taskDao().updateData(networkResponse[0]);

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean shouldDisplayNotifications = sp.getBoolean(context.getString(R.string.notifications_key), true);
                Long timeSinceLastNotification = SunshinePreferences.getEllapsedTimeSinceLastNotification(context);

                boolean oneDayPassedSinceLastNotification = false;

                if (timeSinceLastNotification >= DateUtils.DAY_IN_MILLIS) {
                    oneDayPassedSinceLastNotification = true;
                }
                if (shouldDisplayNotifications && oneDayPassedSinceLastNotification) {
                    NotificationUtils.updateUserWithNotification(context);
                }

                stopSelf();
            }

            private String getFakeData() {

                String fakeJsonresponse = "";
                return fakeJsonresponse;
            }
        });

        return START_STICKY;
    }

    private ArrayList<TaskEntry> extractFeatureFromJson(String jsonResponse) throws JSONException {
        // String s= jsonResponse.substring(5,jsonResponse.length()-1);
        JSONObject baseJsonResponse = new JSONObject(jsonResponse);
        JSONArray list = baseJsonResponse.getJSONArray("list");
        //LinkedList<ArrayList<ListItems>> listAsPerDate=new LinkedList<>();
        ArrayList<TaskEntry> weatherAtDate=new ArrayList<TaskEntry>();

        int arrayListIndex=0;
        for(int i=0;i<list.length();i++){
            JSONObject ithElement = list.getJSONObject(i);
            if(ithElement.has("dt_txt")){
                dt_txt=ithElement.getString("dt_txt");
                String[] datentime=dt_txt.split("\\s");
                dt_txt=datentime[0];
                time=datentime[1];
            }
            if(ithElement.has("dt")){
                time =ithElement.getString("dt");
            }
            if(ithElement.has("main")){
                main = ithElement.getJSONObject("main");
            }
            if(main.has("temp")){
                temp=main.getString("temp");
            }
            if(main.has("temp_kf")){
                temp_kf=main.getString("temp_kf");
            }
            if(main.has("temp_max")){
                temp_max=main.getString("temp_max");
            }
            if(main.has("temp_min")){
                temp_min=main.getString("temp_min");
            }
            if(main.has("sea_level")){
                sea_level=main.getString("sea_level");
            }
            if(main.has("pressure")){
                pressure=main.getString("pressure");
            }
            if(main.has("humidity")){
                humidity=main.getString("humidity");
            }
            if(main.has("grnd_level")){
                grnd_level=main.getString("grnd_level");
            }
            if(main.has("feels_like")){
                feels_like=main.getString("feels_like");
            }
            if(ithElement.has("wind")){
                JSONObject wind = ithElement.getJSONObject("wind");
                if(wind.has("speed")){
                    windSpeed=wind.getString("speed");
                }
            }
            if(ithElement.has("weather")){
                JSONArray weather = ithElement.getJSONArray("weather");
                JSONObject zerothweather=weather.getJSONObject(0);
                if(zerothweather.has("description")){
                    description=zerothweather.getString("description");
                }
                if(zerothweather.has("description")){
                    id=zerothweather.getString("id");
                }
            }
            weatherAtDate.add(new TaskEntry(dt_txt, time,  temp,  temp_kf,  temp_max,  temp_min,  sea_level,  pressure,  humidity,  grnd_level,  feels_like,  windSpeed,  description,id));
        }
        return weatherAtDate;
    }
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("x-rapidapi-key", "a1c5976ab0msh5b5550912475b33p16709ejsn55da6b994e97");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            System.out.println("----------------------------------------------"+urlConnection.getURL());
            int responseStatus = urlConnection.getResponseCode();
            Log.v("responseStatus= ", Integer.toString(responseStatus));

            if(responseStatus==200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else{
                jsonResponse="";
            }
        } catch (IOException e) {
            throw new IOException("invalid URL? - exception is "+e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder response=new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line!=null){
                response.append(line);
                line = bufferedReader.readLine();
            }
        }
        return response.toString();
    }

    private URL createURL(String uri) throws MalformedURLException {
        return new URL(uri);
    }

}
