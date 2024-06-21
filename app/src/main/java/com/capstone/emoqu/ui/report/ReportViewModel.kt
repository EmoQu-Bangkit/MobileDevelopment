package com.capstone.emoqu.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.emoqu.data.remote.EmoQuRepository
import com.capstone.emoqu.data.remote.Result
import com.capstone.emoqu.data.response.AddReportResponse
import com.capstone.emoqu.data.response.ListActivityResponse
import com.capstone.emoqu.data.response.ListReportResponse
import kotlinx.coroutines.launch

class ReportViewModel (
    private val emoQuRepository: EmoQuRepository
) : ViewModel() {

    // Function to get all activity data
    private var _activityData: LiveData<Result<ListActivityResponse>>? = null

    fun getActivityData(): LiveData<Result<ListActivityResponse>> {
        if (_activityData == null) {
            _activityData = emoQuRepository.getAllDataFromServer()
        }
        return _activityData!!
    }

    // Function to upload processed data
    private val _uploadResult = MutableLiveData<Result<AddReportResponse>>()
    val uploadResult: LiveData<Result<AddReportResponse>> get() = _uploadResult

    fun uploadReportData(
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
        predictedDayCondition: String,
        predictedDayLabel: String,
        positif: Int,
        negatif: Int,
        netral: Int,
        dateTips: String,
        eatTips: String,
        entertainmentTips: String,
        selfCareTips: String,
        sleepTips: String,
        studyTips: String,
        travelingTips: String,
        workTips: String,
        workoutTips: String
    ) {
        viewModelScope.launch {
            _uploadResult.value = Result.Loading
            val result = emoQuRepository.uploadPreprocessingData(
                timeStamp, dating, eating, entertainment, selfCare, sleep, study,
                traveling, work, workout, predictedDayCondition, predictedDayLabel,
                positif, negatif, netral, dateTips, eatTips, entertainmentTips,
                selfCareTips, sleepTips, studyTips, travelingTips, workTips, workoutTips
            )
            _uploadResult.value = result
        }
    }

    // Function to model current condition
    private val _predictionResult = MutableLiveData<String>()

    fun getPredictionResult(): LiveData<String> = _predictionResult

    fun setPredictionResult(prediction: String) {
        _predictionResult.value = prediction
    }

    // Function to get all user reports
    private var _reportData: LiveData<Result<ListReportResponse>>? = null

    fun getAllReportData(): LiveData<Result<ListReportResponse>> {
        if (_reportData == null) {
            _reportData = emoQuRepository.getAllReportData()
        }
        return _reportData!!
    }
}
