package com.capstone.emoqu.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.emoqu.data.remote.EmoQuRepository
import com.capstone.emoqu.ui.auth.pref.AuthPreferences

class LoginViewModel (
    private val emoQuRepository: EmoQuRepository
) : ViewModel() {
    fun login(email: String, password: String)=emoQuRepository.login(email, password)
}