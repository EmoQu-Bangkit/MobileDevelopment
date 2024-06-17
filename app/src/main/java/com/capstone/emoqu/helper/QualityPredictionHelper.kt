package com.capstone.emoqu.helper

import android.content.Context
import android.util.Log
import com.capstone.emoqu.data.response.Activity
import com.capstone.emoqu.ui.report.ReportViewModel
import com.capstone.emoqu.data.remote.Result

class QualityPredictionHelper (
    private val qualityModel: String = "Model_Quality_Condition.tflite",
    val context: Context,
    private val reportViewModel: ReportViewModel
) {
    // Define weights for each activity (higher weight means higher significance)
    val activityWeights: Map<String, Float> = mapOf(
        "Sleep" to 0.2f,
        "Study" to 0.1f,
        "Work" to 0.35f,
        "Dating" to 0.05f,
        "Self Care" to 0.05f,
        "Traveling" to 0.05f,
        "Entertainment" to 0.1f,
        "Eating" to 0.05f,
        "Workout" to 0.05f
    )

    fun calculateDailyQuality(activities: List<Activity>): Map<String, Float> {
        // Inisialisasi mutable map untuk menyimpan kualitas harian
        val dailyQuality = mutableMapOf<String, MutableList<Float>>()

        // Iterasi setiap activity dalam daftar activities
        activities.forEach { activity ->
            // Mendapatkan bobot (weight) untuk jenis kegiatan dari activityWeights
            val weight = activityWeights[activity.activities] ?: 0.0f

            // Menghitung kualitas keseluruhan dari aktivitas
            val overallQuality = activity.quality * weight

            // Memasukkan nilai kualitas keseluruhan ke dalam dailyQuality map,
            // dengan key berupa timestamp dari activity
            dailyQuality.computeIfAbsent(activity.timeStamp) { mutableListOf() }.add(overallQuality)
        }
        // Menghitung rata-rata kualitas harian untuk setiap timestamp
        // dan mengembalikan hasilnya dalam bentuk Map<String, Float>
        return dailyQuality.mapValues { it.value.average().toFloat() }
    }

    fun processServerData() {
        // Observe data from ViewModel
        reportViewModel.getActivityData().observeForever { result ->
            when (result) {
                is Result.Success -> {
                    val activities = result.data.activities
                    // Calculate daily quality
                    val dailyQuality = calculateDailyQuality(activities)
                    // Process the daily quality data further as needed
                    Log.d(TAG, "Daily Quality: $dailyQuality")
                    // Example: Pass dailyQuality to another function or module
                    // processDailyQuality(dailyQuality)
                }
                is Result.Error -> {
                    Log.e(TAG, "Failed to fetch activity data: ${result.error}")
                    // Handle error scenario if needed
                }

                else -> {}
            }
        }
    }

    fun classifyDayQuality() {

    }

    companion object {
        private const val TAG = "QualityPredictionHelper"
    }
}


