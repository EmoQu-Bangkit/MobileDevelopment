package com.capstone.emoqu.helper

import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import com.capstone.emoqu.data.remote.Result
import com.capstone.emoqu.data.response.Activity
import com.capstone.emoqu.data.response.ListActivityResponse
import com.capstone.emoqu.ui.report.ReportViewModel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import com.capstone.emoqu.ml.ModelDayCondition
import org.tensorflow.lite.DataType
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DailyPredictionHelper(
    val context: Context,
    private val reportViewModel: ReportViewModel,
    private val predictionCallback: (String) -> Unit
) {
    // Labels for prediction results
    private val predictionLabels = listOf(
        "Active Explorer",
        "Balanced Achiever",
        "Fitness Fanatic",
        "Stressful Overload"
    )

    // Proses keseluruhan modeling
    private val activityDataObserver = Observer<Result<ListActivityResponse>> { result ->
        when (result) {
            is Result.Success -> {
                val activities = result.data.activities
                val formattedData = formatData(activities)
                val predictionResult = performInference(formattedData)
                val predictionInteger = convertResultToInt(predictionResult)
                val predictionLabel = labelPredictionResult(predictionInteger)
                predictionCallback(predictionLabel) // Trigger the callback with the prediction label
            }
            is Result.Error -> {
                Log.e(TAG, "Failed to fetch activity data: ${result.error}")
            }
            else -> {
                Log.e(TAG, "Unknown result type: $result")
            }
        }
    }

    fun startObserving() {
        reportViewModel.getActivityData().observeForever(activityDataObserver)
    }

    fun stopObserving() {
        reportViewModel.getActivityData().removeObserver(activityDataObserver)
    }

    // Preprocessing data
    fun formatData(activities: List<Activity>): List<Map<String, Any>> {
        val groupedActivities = activities.groupBy { it.timeStamp }
        val resultList = mutableListOf<Map<String, Any>>()

        for ((date, activitiesOnDate) in groupedActivities) {
            val activityDurationMap = mutableMapOf<String, Any>(
                "Time_Stamp" to date,
                "Dating" to 0,
                "Eating" to 0,
                "Entertainment" to 0,
                "Self Care" to 0,
                "Sleep" to 0,
                "Study" to 0,
                "Travelling" to 0,
                "Work" to 0,
                "Workout" to 0
            )

            for (activity in activitiesOnDate) {
                val duration = activity.duration
                when (activity.activities) {
                    "Dating" -> activityDurationMap["Dating"] = activityDurationMap["Dating"] as Int + duration
                    "Eating" -> activityDurationMap["Eating"] = activityDurationMap["Eating"] as Int + duration
                    "Entertainment" -> activityDurationMap["Entertainment"] = activityDurationMap["Entertainment"] as Int + duration
                    "Self Care" -> activityDurationMap["Self Care"] = activityDurationMap["Self Care"] as Int + duration
                    "Sleep" -> activityDurationMap["Sleep"] = activityDurationMap["Sleep"] as Int + duration
                    "Study" -> activityDurationMap["Study"] = activityDurationMap["Study"] as Int + duration
                    "Travelling" -> activityDurationMap["Travelling"] = activityDurationMap["Travelling"] as Int + duration
                    "Work" -> activityDurationMap["Work"] = activityDurationMap["Work"] as Int + duration
                    "Workout" -> activityDurationMap["Workout"] = activityDurationMap["Workout"] as Int + duration
                }
            }

            Log.d(TAG, "Activity Duration Map for date $date: $activityDurationMap")
            resultList.add(activityDurationMap)
        }

        return resultList
    }

    // Convert data for modelling
    private fun convertDataToTensor(data: List<Map<String, Any>>): ByteBuffer {
        val inputSize = data.size * 9 // 9 types of activities
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        for (entry in data) {
            byteBuffer.putFloat((entry["Dating"] as Int).toFloat())
            byteBuffer.putFloat((entry["Eating"] as Int).toFloat())
            byteBuffer.putFloat((entry["Entertainment"] as Int).toFloat())
            byteBuffer.putFloat((entry["Self Care"] as Int).toFloat())
            byteBuffer.putFloat((entry["Sleep"] as Int).toFloat())
            byteBuffer.putFloat((entry["Study"] as Int).toFloat())
            byteBuffer.putFloat((entry["Travelling"] as Int).toFloat())
            byteBuffer.putFloat((entry["Work"] as Int).toFloat())
            byteBuffer.putFloat((entry["Workout"] as Int).toFloat())
        }

        // Log the byte buffer data
        val tempByteBuffer = byteBuffer.asFloatBuffer()
        val tempArray = FloatArray(tempByteBuffer.limit())
        tempByteBuffer.get(tempArray)
        Log.d(TAG, "Byte Buffer Data: ${tempArray.joinToString(", ")}")

        return byteBuffer
    }

    // Modelling function
    fun performInference(data: List<Map<String, Any>>): FloatArray {
        val byteBuffer = convertDataToTensor(data)
        val model = ModelDayCondition.newInstance(context)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 9), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        // Releases model resources if no longer used.
        model.close()

        return outputFeature0.floatArray
    }

    // Convert float array to integer
    fun convertResultToInt(result: FloatArray): Int {
        // Assuming the result contains probabilities or scores, we take the index of the highest value
        return result.indices.maxByOrNull { result[it] } ?: -1
    }

    // Label the prediction result
    fun labelPredictionResult(prediction: Int): String {
        return if (prediction in predictionLabels.indices) {
            predictionLabels[prediction]
        } else {
            "Unknown"
        }
    }

    companion object {
        private const val TAG = "DailyPredictionHelper"
    }
}
