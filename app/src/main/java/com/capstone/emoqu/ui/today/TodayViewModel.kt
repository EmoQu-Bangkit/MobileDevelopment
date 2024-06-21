package com.capstone.emoqu.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.capstone.emoqu.data.remote.EmoQuRepository
import com.capstone.emoqu.data.remote.Result
import com.capstone.emoqu.data.response.ListActivityResponse
import com.capstone.emoqu.data.response.ProfileResponse
import com.capstone.emoqu.ui.auth.pref.AuthPreferences

class TodayViewModel (
    private val emoQuRepository: EmoQuRepository,
    private val authentication: AuthPreferences
) : ViewModel() {
    fun getSession(): LiveData<String> = authentication.getSession().asLiveData()

    private var _activityData: LiveData<Result<ListActivityResponse>>? = null

    fun getActivityData(): LiveData<Result<ListActivityResponse>> {
        if (_activityData == null) {
            _activityData = emoQuRepository.getAllDataFromServer()
        }
        return _activityData!!
    }

    fun getProfileUser(): LiveData<Result<ProfileResponse>> = emoQuRepository.getProfileUser()
}