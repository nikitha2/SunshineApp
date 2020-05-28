package com.nikitha.android.sunshineapp.utilities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.nikitha.android.sunshineapp.ListItems;
import com.nikitha.android.sunshineapp.MainActivity;

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
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class SunshineLoader extends AsyncTaskLoader<ArrayList<ListItems>> {
    Bundle args=new Bundle();
    String uri="";
    String dt_txt="";
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
    public SunshineLoader(@NonNull Context context, Bundle args) {
        super(context);
        this.args=args;
    }
    public SunshineLoader(MainActivity context, String toString) {
        super(context);
        uri=toString;
    }
    @Nullable
    @Override
    public ArrayList<ListItems>  loadInBackground() {
        ArrayList<ListItems> networkResponse=new ArrayList<ListItems>();
        URL url=null;

        try {
            if(uri!=null) {
                url=createURL(uri); }
            else{
                url=null; } }
        catch (MalformedURLException e) {
            e.printStackTrace(); }

        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            networkResponse = extractFeatureFromJson(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return networkResponse;
    }

    private ArrayList<ListItems> extractFeatureFromJson(String jsonResponse) throws JSONException {
       // String s= jsonResponse.substring(5,jsonResponse.length()-1);
        JSONObject baseJsonResponse = new JSONObject(jsonResponse);
        JSONArray list = baseJsonResponse.getJSONArray("list");
        //LinkedList<ArrayList<ListItems>> listAsPerDate=new LinkedList<>();
        ArrayList<ListItems> weatherAtDate=new ArrayList<ListItems>();

        int arrayListIndex=0;
        for(int i=0;i<list.length();i++){
            JSONObject ithElement = list.getJSONObject(i);
            if(ithElement.has("dt_txt")){
                 dt_txt=ithElement.getString("dt_txt");
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
            }
            weatherAtDate.add(new ListItems(dt_txt,  temp,  temp_kf,  temp_max,  temp_min,  sea_level,  pressure,  humidity,  grnd_level,  feels_like,  windSpeed,  description));
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
            urlConnection.setRequestProperty("x-rapidapi-key", "5591d0feecmsh68ff6a66c080efcp1cc3fajsn07ea13d935e3");
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
