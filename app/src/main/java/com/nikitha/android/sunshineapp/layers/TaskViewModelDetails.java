package com.nikitha.android.sunshineapp.layers;

import android.content.Context;
import android.os.Bundle;

import com.nikitha.android.sunshineapp.database.TaskEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TaskViewModelDetails extends ViewModel {
    private static final String TAG = TaskViewModelDetails.class.getSimpleName();
    Bundle input=new Bundle();



    public Bundle getInput() {
        return input;
    }

    public void setInput(Bundle input) {
        this.input = input;
    }


    private  LiveData<List<TaskEntry>>  tasks;
    private final TasksRepository tasksRepository;

    public TaskViewModelDetails(Context context) {
        super();
        tasksRepository = new TasksRepository(null, context);
    }

    // COMPLETED (7) Create a getter for the task variable
    public LiveData<List<TaskEntry>> getTasks() {
        return tasks;
    }

    public LiveData<List<TaskEntry>> getTasksbyDate(String date ) {
        return tasksRepository.loadTaskByDate(date);
    }
  /*  public void updateTask(TaskEntry task) {
        tasksRepository.updateTaskById(task);
    }*/
}
