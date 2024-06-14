package com.capstone.emoqu.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityEntity
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityRoomDatabase
import com.capstone.emoqu.data.response.RegisterResponse
import com.capstone.emoqu.data.retrofit.ApiService
import com.capstone.emoqu.ui.auth.pref.AuthPreferences
import com.capstone.emoqu.data.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EmoQuRepository(
    private val apiService: ApiService,
    private val authentication: AuthPreferences,
    private val database: ActivityRoomDatabase
){
    private val activityDao = database.activityDao()
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun register(firstName: String, lastName:String, email: String, password: String)
            : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(firstName, lastName, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun login(email: String, password: String)
            : LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            if (!response.error) {
                val loginResult = response.loginResult
                authentication.saveSession(
                    loginResult.token
                )
                Log.d("EmoQuRepository", "saveSession called: token=${loginResult.token}")
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun getAllActivity(): LiveData<Result<List<ActivityEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val activities = activityDao.getAllActivity().value ?: emptyList()
            emit(Result.Success(activities))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun insertActivity(activity: ActivityEntity) {
        executorService.execute {activityDao.insert(activity)}
    }

    fun delete(activity: ActivityEntity) {
        executorService.execute { activityDao.delete(activity) }
    }
    fun update(activity: ActivityEntity) {
        executorService.execute { activityDao.update(activity) }
    }


    companion object {
        @Volatile
        private var INSTANCE: EmoQuRepository? = null

        fun getInstance(
            apiService: ApiService,
            authentication: AuthPreferences,
            database: ActivityRoomDatabase
        ): EmoQuRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: EmoQuRepository(apiService, authentication, database)
            }.also { INSTANCE = it }
        }
    }
}