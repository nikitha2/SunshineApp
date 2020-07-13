package com.nikitha.android.sunshineapp.layers;

import android.content.Context;
import android.os.Bundle;
import com.nikitha.android.sunshineapp.database.TaskEntry;
import com.nikitha.android.sunshineapp.sync.ServiceTasks;

import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TaskViewModel extends ViewModel {
    private static final String TAG = TaskViewModel.class.getSimpleName();
    Bundle input=new Bundle();

    public Bundle getInput() {
        return input;
    }

    public void setInput(Bundle input) {
        this.input = input;
    }


    private  LiveData<List<TaskEntry>>  tasks;
    private  List<TaskEntry>  tasksNew=new ArrayList<>();

    private final TasksRepository tasksRepository;

    public TaskViewModel(Bundle input, Context context) {
        tasksRepository = new TasksRepository(input,context);
       // tasksRepository.startSync(input);
        ServiceTasks.executeTask(context, ServiceTasks.WEATHER_SERVICE_ACTION, input.getString("url"));
        tasks = tasksRepository.loadAllTasks();
    }

    public LiveData<List<TaskEntry>> getTasks() {
        return tasks;
    }


    /*public List<TaskEntry> finMinMaxTemp( List<TaskEntry> tasks){
        List<TaskEntry> list=new ArrayList<>();
        list= tasks;
        String date=""; Double min=0.0; Double max=0.0;

        date= list.get(0).getDate_txt();
        min=Double.valueOf(list.get(0).getTemp_min());
        max=Double.valueOf(list.get(0).getTemp_max());
        String weatherid=list.get(0).getWeatherid(),desc=list.get(0).getDescription();
        for(int i=1;i<list.size();i++){
            String date1=list.get(i).getDate_txt();
            if(date.equalsIgnoreCase(date1)){
                if(Double.valueOf(list.get(i).getTemp_min()) < min){
                    min=Double.valueOf(list.get(i).getTemp_min());
                    weatherid=list.get(i).getWeatherid();
                    desc=list.get(i).getDescription();

                }
                if(Double.valueOf(list.get(i).getTemp_max()) > max){
                    max=Double.valueOf(list.get(i).getTemp_max());
                }
            }
            else{
                tasksNew.add(new TaskEntry(date,null,null,null,Double.toString(max),Double.toString(min),null,null,null,null,null,null,desc,weatherid));
                date=date1;
                min=Double.valueOf(list.get(i).getTemp_min());
                max=Double.valueOf(list.get(i).getTemp_max());
                weatherid=list.get(0).getWeatherid();
                desc=list.get(0).getDescription();

            }
        }
        return tasksNew;
    }*/
}
