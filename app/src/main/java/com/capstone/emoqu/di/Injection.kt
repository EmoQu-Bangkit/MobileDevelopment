package com.capstone.emoqu.di

import android.content.Context
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityRoomDatabase
import com.capstone.emoqu.data.remote.EmoQuRepository
import com.capstone.emoqu.data.retrofit.ApiConfig
import com.capstone.emoqu.ui.auth.pref.AuthPreferences
import com.capstone.emoqu.ui.auth.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): EmoQuRepository {
        val pref = AuthPreferences.getInstance(context.dataStore)
        val idTokenFlow = pref.getSession()
        val idToken = runBlocking { idTokenFlow.first() }
        val apiService = ApiConfig.getApiService(idToken)
        val database = ActivityRoomDatabase.getInstance(context)
        return EmoQuRepository.getInstance(apiService, pref, database, context.applicationContext)
    }
}