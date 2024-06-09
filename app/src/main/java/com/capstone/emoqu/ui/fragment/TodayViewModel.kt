package com.capstone.emoqu.ui.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.emoqu.data.remote.EmoQuRepository
import com.capstone.emoqu.ui.auth.pref.AuthPreferences

class TodayViewModel (
    private val emoQuRepository: EmoQuRepository,
    private val authentication: AuthPreferences
) : ViewModel() {
    fun getSession(): LiveData<String> = authentication.getSession().asLiveData()
}