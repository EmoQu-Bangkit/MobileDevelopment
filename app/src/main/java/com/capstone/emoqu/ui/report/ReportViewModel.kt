package com.capstone.emoqu.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.emoqu.data.remote.EmoQuRepository
import com.capstone.emoqu.data.remote.Result
import com.capstone.emoqu.data.response.ListActivityResponse

class ReportViewModel (
    private val emoQuRepository: EmoQuRepository
) : ViewModel() {

    private var _activityData: LiveData<Result<ListActivityResponse>>? = null

    fun getActivityData(): LiveData<Result<ListActivityResponse>> {
        if (_activityData == null) {
            _activityData = emoQuRepository.getAllDataFromServer()
        }
        return _activityData!!
    }
}