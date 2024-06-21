package com.capstone.emoqu.data.remote

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityEntity
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityRoomDatabase
import com.capstone.emoqu.data.response.AddActivityResponse
import com.capstone.emoqu.data.response.AddReportResponse
import com.capstone.emoqu.data.response.ListActivityResponse
import com.capstone.emoqu.data.response.ListReportResponse
import com.capstone.emoqu.data.response.RegisterResponse
import com.capstone.emoqu.data.retrofit.ApiService
import com.capstone.emoqu.ui.auth.pref.AuthPreferences
import com.capstone.emoqu.data.response.LoginResponse
import com.capstone.emoqu.data.response.ProfileResponse
import com.capstone.emoqu.data.retrofit.ApiConfig
import com.capstone.emoqu.utils.NetworkCheck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import retrofit2.http.Field

class EmoQuRepository(
    private val apiService: ApiService,
    private val authentication: AuthPreferences,
    private val database: ActivityRoomDatabase,
    private val applicationContext: Context
){
    private val activityDao = database.activityDao()

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

    private suspend fun uploadDataToLocal(activity: ActivityEntity): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                activityDao.insert(activity)
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error("Failed to insert activity locally: ${e.message}")
        }
    }


    private suspend fun uploadToServer(activity: ActivityEntity): Result<AddActivityResponse> {
        return try {
            val token = withContext(Dispatchers.IO) {
                authentication.getSession().first()
            }

            if (token.isEmpty()) {
                return Result.Error("Token is empty")
            }
            val response = withContext(Dispatchers.IO) {
                val apiServiceWithToken = ApiConfig.getApiService(token)
                apiServiceWithToken.addActivity(
                    activity.quality,
                    activity.activity,
                    activity.duration,
                    activity.notes ?: ""
                )
            }
            if (response.error) {
                Result.Error("Failed to upload activity: ${response.message}")
            } else {
                Result.Success(response)
            }
        } catch (e: Exception) {
            Result.Error("Exception occurred: ${e.message}")
        }
    }

    fun getAllDataFromServer(): LiveData<Result<ListActivityResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = withContext(Dispatchers.IO) {
                authentication.getSession().first()
            }
            Log.d("EmoQuRepository", "Fetched token: $token")

            if (token.isNotEmpty()) {
                val apiServiceWithToken = ApiConfig.getApiService(token)
                val response = apiServiceWithToken.listActivity()
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

    fun getProfileUser(): LiveData<Result<ProfileResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = withContext(Dispatchers.IO) {
                authentication.getSession().first()
            }
            Log.d("EmoQuRepository", "Fetched token: $token")

            if (token.isNotEmpty()) {
                val apiServiceWithToken = ApiConfig.getApiService(token)
                val response = apiServiceWithToken.getProfile()
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error("An error occurred: Unable to fetch user profile data"))
                }
            } else {
                emit(Result.Error("Token is empty"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    suspend fun uploadPreprocessingData(
        timeStamp: String,
        dating: Float,
        eating: Float,
        entertainment: Float,
        selfCare: Float,
        sleep: Float,
        study: Float,
        traveling: Float,
        work: Float,
        workout: Float,
        predictedDayCondition:String,
        predictedDayLabel:String,
        positif:Int,
        negatif:Int,
        netral:Int,
        dateTips:String,
        eatTips:String,
        entertainmentTips:String,
        selfCareTips:String,
        sleepTips:String,
        studyTips:String,
        travelingTips:String,
        workTips:String,
        workoutTips:String
    ): Result<AddReportResponse> {
        return try {
            val token = withContext(Dispatchers.IO) {
                authentication.getSession().first()
            }
            Log.d("EmoQuRepository", "Fetched token: $token")

            if (token.isNotEmpty()) {
                val response = withContext(Dispatchers.IO) {
                    val apiServiceWithToken = ApiConfig.getApiService(token)
                    apiServiceWithToken.addReport(
                        timeStamp,
                        dating,
                        eating,
                        entertainment,
                        selfCare,
                        sleep,
                        study,
                        traveling,
                        work,
                        workout,
                        predictedDayCondition,
                        predictedDayLabel,
                        positif,
                        negatif,
                        netral,
                        dateTips,
                        eatTips,
                        entertainmentTips,
                        selfCareTips,
                        sleepTips,
                        studyTips,
                        travelingTips,
                        workTips,
                        workoutTips,)
                }
                if (response.error) {
                    Result.Error("Failed to upload daily report: ${response.message}")
                } else {
                    Result.Success(response)
                }
            } else {
                Result.Error("Token is empty")
            }
        } catch (e: Exception) {
            Result.Error("Exception occurred: ${e.message}")
        }
    }

    fun getAllReportData(): LiveData<Result<ListReportResponse>> = liveData {
        emit(Result.Loading)
        try {
            val token = withContext(Dispatchers.IO) {
                authentication.getSession().first()
            }
            Log.d("EmoQuRepository", "Fetched token: $token")

            if (token.isNotEmpty()) {
                val apiServiceWithToken = ApiConfig.getApiService(token)
                val response = apiServiceWithToken.getListReport()
                if (!response.error) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error("An error occurred: Unable to fetch user report data"))
                }
            } else {
                emit(Result.Error("Token is empty"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unknown error occurred"))
        }
    }

    suspend fun saveActivity(activity: ActivityEntity): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                if (NetworkCheck.isNetworkAvailable(applicationContext)) {
                    // Jika ada koneksi internet, simpan ke server
                    val serverResult = uploadToServer(activity)
                    Log.d("EmoQuRepository", "Uploaded to server successfully")
                    if (serverResult is Result.Error) {
                        // Gagal upload ke server, simpan ke lokal
                        Log.d("EmoQuRepository", "Failed to upload to server, saving locally")
                        uploadDataToLocal(activity)
                    } else {
                        // Berhasil upload ke server, juga simpan ke lokal
                        Log.d("EmoQuRepository", "Uploaded to server, saving locally as well")
                        uploadDataToLocal(activity)
                        Result.Success(Unit)
                    }
                } else {
                    // Tidak ada koneksi internet, simpan ke lokal
                    Log.d("EmoQuRepository", "No network, saving locally")
                    uploadDataToLocal(activity)
                }
            } catch (e: Exception) {
                Log.e("EmoQuRepository", "Failed to save activity: ${e.message}")
                Result.Error("Failed to save activity: ${e.message}")
            }
        }
    }

    suspend fun syncData(): Result<Unit> {
        return try {
            if (NetworkCheck.isNetworkAvailable(applicationContext)) {
                Log.d("SyncData", "Starting synchronization process...")

                // Ambil data yang belum disinkronkan dari lokal
                val unsyncedActivities = activityDao.getUnsyncedActivities()
                for (activity in unsyncedActivities) {
                    val serverResult = uploadToServer(activity)
                    if (serverResult is Result.Success) {
                        // Jika berhasil, tandai data sebagai sudah disinkronkan
                        activity.synced = true
                        activityDao.markAsSynced(activity.id)
                    } else {
                        Log.e("SyncData", "Failed to sync activity: ${activity.id}")
                    }
                }
                Log.d("SyncData", "Synchronization process completed successfully.")
                Result.Success(Unit)
            } else {
                Result.Error("No internet connection")
            }
        } catch (e: Exception) {
            Log.e("SyncData", "Exception during sync: ${e.message}", e)
            Result.Error("Failed to sync data: ${e.message}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: EmoQuRepository? = null

        fun getInstance(
            apiService: ApiService,
            authentication: AuthPreferences,
            database: ActivityRoomDatabase,
            applicationContext: Context
        ): EmoQuRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: EmoQuRepository(apiService, authentication, database, applicationContext)
            }.also { INSTANCE = it }
        }
    }
}