package com.nikitha.android.sunshineapp.utilities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.nikitha.android.sunshineapp.sync.WorkerManagerScheduling;
import java.util.concurrent.TimeUnit;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class ReminderUtils {
    public static final String  LOG_TAG=ReminderUtils.class.getSimpleName();
    public static final String REMAINDER_TAG = "scheduleSyncServiceJob";
    private static final int REMINDER_FLEX_INTERVAL =1;
    private static final int REMINDER_REPEAT_INTERVAL= 24;


    private static Context context;
    public ReminderUtils(Context context) {
        this.context=context;
    }

    public static void scheduleSyncServiceJob(Context context, Bundle input) {
        Log.i(LOG_TAG,"------------------------ inside scheduleSyncServiceJob");
        Data myData = new Data.Builder()
                // We need to pass three integers: X, Y, and Z
                .putString("input", input.getString("url"))
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)  // WIFI
                //.setRequiresCharging(true)                      // REQUIRE CHARGING
                .build();
        WorkRequest scheduleSyncServiceJob =new PeriodicWorkRequest
                .Builder(WorkerManagerScheduling.class,REMINDER_REPEAT_INTERVAL, TimeUnit.HOURS,REMINDER_FLEX_INTERVAL, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag(REMAINDER_TAG)
                .setInputData(myData)
                .build();

        WorkManager.getInstance(context).enqueue(scheduleSyncServiceJob);

    }
}
