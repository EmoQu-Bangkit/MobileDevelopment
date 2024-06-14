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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(activity: ActivityEntity)

    @Update
    fun update(activity: ActivityEntity)

    @Delete
    fun delete(activity: ActivityEntity)

    @Query("SELECT * from ActivityEntity ORDER BY id ASC")
    fun getAllActivity(): LiveData<List<ActivityEntity>>
}