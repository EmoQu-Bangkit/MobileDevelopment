package com.capstone.emoqu.ui.auth.pref

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class AuthPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val TOKEN_KEY = stringPreferencesKey("token")
    private val FIRST_NAME_KEY = stringPreferencesKey("first_name")
    private val LAST_NAME_KEY = stringPreferencesKey("last_name")

    fun getSession(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }.also { Log.d("Authentication", "Session loaded") }
    }

    fun getFirstName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[FIRST_NAME_KEY] ?: ""
        }
    }

    fun getLastName(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[LAST_NAME_KEY] ?: ""
        }
    }

    suspend fun saveSession(token: String, firstName: String, lastName: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
            it[FIRST_NAME_KEY] = firstName
            it[LAST_NAME_KEY] = lastName
        }
        Log.d("Authentication", "Session saved: token=$token, firstName=$firstName, lastName=$lastName")
    }

    suspend fun clearSession() {
        dataStore.edit {
            it.clear()
        }
        Log.d("Authentication", "Session cleared")
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthPreferences? = null
        fun getInstance(dataStore: DataStore<Preferences>): AuthPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
