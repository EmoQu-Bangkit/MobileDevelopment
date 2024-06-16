package com.capstone.emoqu.data.local.databaseAcvitity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ActivityDao {

    @Query("SELECT * FROM activities_table WHERE synced = 0")
    fun getUnsyncedActivities(): List<ActivityEntity>

    @Query("UPDATE activities_table SET synced = 1 WHERE id = :id")
    fun markAsSynced(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(activity: ActivityEntity)

    @Query("SELECT * FROM activities_table")
    fun getAllActivity(): LiveData<List<ActivityEntity>>
}
