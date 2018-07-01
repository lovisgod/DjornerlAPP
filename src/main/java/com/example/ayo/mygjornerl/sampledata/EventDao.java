package com.example.ayo.mygjornerl.sampledata;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * FROM task ORDER BY id ")
    LiveData<List<EventEntry>> loadAllTasks();

    @Insert
    void insertTask(EventEntry eventEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(EventEntry eventEntry);

    @Delete
    void deleteTask(EventEntry eventEntry);

    @Query("SELECT * FROM task WHERE id= :id")
    LiveData<EventEntry> loadTaskById(int id);
}
