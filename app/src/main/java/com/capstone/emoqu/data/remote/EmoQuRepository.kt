package com.capstone.emoqu.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.emoqu.data.response.*
import com.capstone.emoqu.data.retrofit.ApiService
import com.capstone.emoqu.ui.auth.pref.AuthPreferences
import com.capstone.emoqu.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EmoQuRepository(
    private val apiService: ApiService,
    private val authentication: AuthPreferences
) {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    fun register(firstName: String, lastName: String, email: String, password: String)
            : LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(firstName, lastName, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            if (!response.error) {
                val loginResult = response.loginResult
                authentication.saveSession(
                    loginResult.token,
                    loginResult.firstName,
                    loginResult.lastName
                )
                Log.d("EmoQuRepository", "saveSession called: token=${loginResult.token}, firstName=${loginResult.firstName}, lastName=${loginResult.lastName}")
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun addActivity(mood: Int, activities: String, duration: Int, notes: String)
            : LiveData<Result<AddActivityResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = withContext(Dispatchers.IO) {
                authentication.getSession().first()
            }

            Log.d("EmoQuRepository", "Fetched token: $token")

            if (token.isNotEmpty()) {
                val apiServiceWithToken = ApiConfig.getApiService(token)
                val response = apiServiceWithToken.addactivity(mood, activities, duration, notes)
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } else {
                emit(Result.Error("Token is empty"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    fun getAllActivityCloud(): LiveData<Result<List<Activity>>> = liveData {
        emit(Result.Loading)
        try {
            val token = withContext(Dispatchers.IO) {
                authentication.getSession().first()
            }

            Log.d("EmoQuRepository", "Fetched token: $token")

            if (token.isNotEmpty()) {
                val apiServiceWithToken = ApiConfig.getApiService(token)
                val response = apiServiceWithToken.listactivity(
                    quality = 0,
                    activities = "",
                    notes = ""
                )
                if (!response.error) {
                    val activityEntities = response.activities.map { activity ->
                        Activity(
                            timeStamp = activity.timeStamp,
                            quality = activity.quality.toInt(),
                            activities = activity.activities,
                            duration = activity.duration.toInt(),
                            notes = activity.notes
                        )
                    }
                    emit(Result.Success(activityEntities))
                } else {
                    emit(Result.Error("Failed to fetch activities from cloud"))
                }
            } else {
                emit(Result.Error("Token is empty"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: EmoQuRepository? = null

        fun getInstance(
            apiService: ApiService,
            authentication: AuthPreferences
        ): EmoQuRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: EmoQuRepository(apiService, authentication)
            }.also { INSTANCE = it }
        }
    }
}
