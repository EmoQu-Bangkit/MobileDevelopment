package com.capstone.emoqu.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.emoqu.data.remote.EmoQuRepository
import com.capstone.emoqu.data.remote.Result
import com.capstone.emoqu.ui.auth.pref.AuthPreferences
import com.capstone.emoqu.data.response.Activity

class TodayViewModel(
    private val emoQuRepository: EmoQuRepository,
    private val authPreferences: AuthPreferences
) : ViewModel() {

    val firstName: LiveData<String> = authPreferences.getFirstName().asLiveData()
    val lastName: LiveData<String> = authPreferences.getLastName().asLiveData()

    fun getSession(): LiveData<String> {
        return authPreferences.getSession().asLiveData()
    }

    fun getAllActivityCloud(): LiveData<Result<List<Activity>>> {
        return emoQuRepository.getAllActivityCloud()
    }
}
