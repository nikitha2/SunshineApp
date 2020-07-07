package com.nikitha.android.sunshineapp.sync;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nikitha.android.sunshineapp.utilities.NotificationUtils;

public class ServiceTasks {
    public final static String LOG_TAG=ServiceTasks.class.getSimpleName();
    public static final String WEATHER_SERVICE_ACTION ="weather-service" ;
    public static final String NOTIFICATION_SERVICE_ACTION ="notification-service" ;

    public static void executeTask(Context appContext, String action,String uri){
         if (WEATHER_SERVICE_ACTION.equals(action)) {
             Log.v(LOG_TAG,"------- inside executeTask: WEATHER_SERVICE_ACTION");
             loadWeatherDataFromWebService(appContext,uri);
        }
         else if(NOTIFICATION_SERVICE_ACTION.equals(action)){
             Log.v(LOG_TAG,"------- inside executeTask: NOTIFICATION_SERVICE_ACTION");
//             NotificationUtils.updateUserWithNotification(appContext);
         }
    }

    public static void loadWeatherDataFromWebService(Context appContext, String uri) {
        Log.v(LOG_TAG,"------- inside loadWeatherDataFromWebService");
        Intent intent1 = new Intent(appContext, LoadWeatherDataFromWebService.class);
        intent1.putExtra("uri",uri);
        appContext.startService(intent1);
    }
}
