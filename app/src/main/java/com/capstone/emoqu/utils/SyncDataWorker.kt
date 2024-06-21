package com.capstone.emoqu.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityRoomDatabase
import com.capstone.emoqu.data.remote.EmoQuRepository
import com.capstone.emoqu.data.retrofit.ApiConfig
import com.capstone.emoqu.ui.auth.pref.AuthPreferences
import com.capstone.emoqu.ui.auth.pref.dataStore
import kotlinx.coroutines.flow.first

class SyncDataWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("SyncDataWorker", "Work request received")

        return try {
            val authPreferences = AuthPreferences.getInstance(applicationContext.dataStore)
            val token = authPreferences.getSession().first()

            val repository = EmoQuRepository.getInstance(
                ApiConfig.getApiService(token),
                authPreferences,
                ActivityRoomDatabase.getInstance(applicationContext),
                applicationContext
            )

            val syncResult = repository.syncData()

            if (syncResult is com.capstone.emoqu.data.remote.Result.Success) {
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e("SyncDataWorker", "Error during sync: ${e.message}", e)
            Result.failure()
        }
    }
}
