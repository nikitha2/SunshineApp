package com.nikitha.android.sunshineapp.database;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public abstract  class TaskDao {

    @Query("SELECT * FROM task ORDER BY date_txt")
    public abstract LiveData<List<TaskEntry>> loadAllTasks();

    @Query("SELECT * FROM task WHERE id=:id")
    public abstract LiveData<TaskEntry> loadTaskById(int id);

    @Query("SELECT * FROM task WHERE Date_txt=:date")
    public abstract LiveData<List<TaskEntry>> loadTaskByDate(String date);

    @Insert
    public abstract void insertTask(TaskEntry taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public abstract void updateTask(TaskEntry taskEntry);

    @Delete
    public abstract void deleteTask(TaskEntry taskEntry);

    // Since you can not have non-abstract methods in interfaces, you need to use an abstract class instead of interface for DAO.
    // else public interface  class TaskDao
    @Transaction
    public void updateData(List<TaskEntry> tasks) {
        deleteAllEntries();
        insertAll(tasks);
      //  loadAllTasks();
    }

    @Insert
    public abstract void insertAll(List<TaskEntry> tasks);

    @Query("DELETE FROM task")
    public abstract void  deleteAllEntries();

}
