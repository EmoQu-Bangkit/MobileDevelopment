package com.capstone.emoqu.ui.auth.pref

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel (private val authentication: AuthPreferences) : ViewModel() {
    fun getSession(): LiveData<String> = authentication.getSession().asLiveData()

    fun clearSession() {
        viewModelScope.launch {
            authentication.clearSession()
        }
    }
}