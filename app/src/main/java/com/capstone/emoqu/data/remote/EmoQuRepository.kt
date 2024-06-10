package com.capstone.emoqu.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.emoqu.data.response.RegisterResponse
import com.capstone.emoqu.data.retrofit.ApiService
import com.capstone.emoqu.ui.auth.pref.AuthPreferences
import com.capstone.emoqu.data.response.LoginResponse

class EmoQuRepository(
    private val apiService: ApiService,
    private val authentication: AuthPreferences
){
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
                authentication.saveSession(
                    response.idToken,
                )
                Log.d("EmoQuRepository", "saveSession called: token=${response.idToken}")
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message))
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
            authentication: AuthPreferences,
        ): EmoQuRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: EmoQuRepository(apiService, authentication)
            }.also { INSTANCE = it }
        }
    }
}