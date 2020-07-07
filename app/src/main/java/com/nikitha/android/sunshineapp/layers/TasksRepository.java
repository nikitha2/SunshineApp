package com.nikitha.android.sunshineapp.layers;

import android.content.Context;
import android.os.Bundle;

import com.nikitha.android.sunshineapp.database.AppDatabase;
import com.nikitha.android.sunshineapp.database.TaskDao;
import com.nikitha.android.sunshineapp.database.TaskEntry;
import com.nikitha.android.sunshineapp.sync.ServiceTasks;
import com.nikitha.android.sunshineapp.utilities.ReminderUtils;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TasksRepository {
    private static final String LOG_TAG = TasksRepository.class.getSimpleName();
    private LiveData<List<TaskEntry>> tasks;
    private TaskDao taskDao;
    Context context;
    AppDatabase database;
    public TasksRepository(Bundle input, Context context) {
        this.context=context;
        database = AppDatabase.getInstance(context.getApplicationContext());
    }


    public LiveData<List<TaskEntry>> loadAllTasks() {
        tasks = database.taskDao().loadAllTasks();
        return tasks;
    }


    public LiveData<TaskEntry> loadTaskById(int taskId) {
        return database.taskDao().loadTaskById(taskId);
    }

    public LiveData<List<TaskEntry>> loadTaskByDate(String date) {
        return database.taskDao().loadTaskByDate(date);
    }

    public void deleteTasks(TaskEntry taskEntry) {
        database.taskDao().deleteTask(taskEntry);
    }

    public void updateTaskById(TaskEntry task) {
        database.taskDao().updateTask(task);
    }

    public void startSync(Bundle input) {
        ReminderUtils.scheduleSyncServiceJob(context,input);
    }
}
