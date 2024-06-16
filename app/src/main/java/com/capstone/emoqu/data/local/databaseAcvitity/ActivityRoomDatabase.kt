package com.capstone.emoqu.data.local.databaseAcvitity

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ActivityEntity::class],
    version = 1
)
abstract class ActivityRoomDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao

    companion object {
        @Volatile
        private var INSTANCE: ActivityRoomDatabase? = null
        @JvmStatic
        fun getInstance(context: Context): ActivityRoomDatabase {
            if (INSTANCE == null) {
                synchronized(ActivityRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ActivityRoomDatabase::class.java, "activities_database")
                        .build()
                }
            }
            Log.d("ActivityRoomDatabase", "Instance obtained.")
            return INSTANCE as ActivityRoomDatabase
        }
    }
}