package com.nikitha.android.sunshineapp.sync;

import android.content.Context;
import android.util.Log;

import com.google.common.util.concurrent.ListenableFuture;

import androidx.annotation.NonNull;
import androidx.work.WorkerParameters;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkerManagerScheduling extends Worker {
    final String log_TAG=WorkerManagerScheduling.class.getSimpleName();
    Context appContext;
    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public WorkerManagerScheduling(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        this.appContext=appContext;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.i(log_TAG,"--------------inside dowork()");
            String uri = getInputData().getString("input");
            ServiceTasks.executeTask(appContext, ServiceTasks.WEATHER_SERVICE_ACTION, uri);
           // ServiceTasks.executeTask(appContext, ServiceTasks.NOTIFICATION_SERVICE_ACTION, uri);
            return Result.success();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Result.failure();
    }

}
