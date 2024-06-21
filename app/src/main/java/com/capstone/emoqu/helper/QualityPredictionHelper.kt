package com.capstone.emoqu.helper

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.capstone.emoqu.R
import com.capstone.emoqu.data.response.Activity
import com.capstone.emoqu.ui.report.ReportViewModel
import com.capstone.emoqu.data.remote.Result
import com.capstone.emoqu.utils.DateHelper.getCurrentDate
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

class QualityPredictionHelper(
    qualityModel: String = "Model_Quality_Condition.tflite",
    private val context: Context,
    private val reportViewModel: ReportViewModel
) {
    // Define weights for each activity (higher weight means higher significance)
    private val activityWeights: Map<String, Float> = mapOf(
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

    private var interpreter: Interpreter? = null

    init {
        try {
            val modelBuffer = loadModelFile(context.assets, qualityModel)
            interpreter = Interpreter(modelBuffer)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load TensorFlow Lite model: $qualityModel", e)
        }
    }

    private fun loadModelFile(assets: AssetManager, modelFilename: String): ByteBuffer {
        assets.openFd(modelFilename).use { fileDescriptor ->
            FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
                val fileChannel = inputStream.channel
                val startOffset = fileDescriptor.startOffset
                val declaredLength = fileDescriptor.declaredLength
                return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            }
        }
    }

    private fun calculateDailyQualityForToday(activities: List<Activity>): Float? {
        // Mendapatkan tanggal hari ini dalam format yang sesuai dengan format timestamp dari Activity
        val todayDateString = getCurrentDate()

        // Inisialisasi mutable list untuk menyimpan kualitas keseluruhan untuk hari ini
        val overallQualitiesToday = mutableListOf<Float>()

        // Iterasi setiap activity dalam daftar activities
        activities.forEach { activity ->
            // Cek apakah timestamp activity sama dengan hari ini
            if (activity.timeStamp.startsWith(todayDateString)) {
                // Mendapatkan bobot (weight) untuk jenis kegiatan dari activityWeights
                val weight = activityWeights[activity.activities] ?: 0.0f

                // Menghitung kualitas keseluruhan dari aktivitas
                val overallQuality = activity.quality * weight

                // Menambahkan nilai kualitas keseluruhan ke dalam list overallQualitiesToday
                overallQualitiesToday.add(overallQuality)
            }
        }
        // Menghitung rata-rata kualitas harian untuk hari ini
        return overallQualitiesToday.takeIf { it.isNotEmpty() }?.average()?.toFloat()
    }

    private fun predictDailyQuality(dailyQuality: Float): String {
        if (interpreter == null) {
            Log.e(TAG, context.getString(R.string.no_tflite_interpreter_loaded))
            return "Model Interpreter Not Loaded"
        }

        val inputTensor = TensorBuffer.createFixedSize(intArrayOf(1, 1), DataType.FLOAT32)
        inputTensor.loadArray(floatArrayOf(dailyQuality))

        val outputTensor = TensorBuffer.createFixedSize(intArrayOf(1, 5), DataType.FLOAT32)

        try {
            interpreter?.run(inputTensor.buffer, outputTensor.buffer)
            val outputData = outputTensor.floatArray

            // Define class labels based on your model's output categories
            val classLabels = arrayOf("Terrible", "Bad", "Okay", "Good", "Excellent")

            // Get the index of the predicted class label
            var predictedIndex = 0
            var maxValue = outputData[0]
            for (i in 1 until outputData.size) {
                if (outputData[i] > maxValue) {
                    maxValue = outputData[i]
                    predictedIndex = i
                }
            }

            // Retrieve the predicted class label
            val predictedLabel = classLabels[predictedIndex]
            Log.d(TAG, "Predicted Quality Label: $predictedLabel")

            // Set the prediction result in the ViewModel
            reportViewModel.setPredictionResult(predictedLabel)

            return predictedLabel
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            return "Prediction Error"
        }
    }

    fun processServerData() {
        // Observe data from ViewModel
        reportViewModel.getActivityData().observeForever { result ->
            when (result) {
                is Result.Success -> {
                    val activities = result.data.activities
                    // Calculate daily quality for today
                    val dailyQuality = calculateDailyQualityForToday(activities)
                    if (dailyQuality != null) {
                        Log.d(TAG, "Daily Quality for Today: $dailyQuality")
                        predictDailyQuality(dailyQuality)
                    } else {
                        Log.d(TAG, "No activities found for today.")
                    }
                }
                is Result.Error -> {
                    Log.e(TAG, "Failed to fetch activity data: ${result.error}")
                }
                else -> {
                    Log.e(TAG, "Unknown result type: $result")
                }
            }
        }
    }

    fun close() {
        interpreter?.close()
    }

    companion object {
        private const val TAG = "QualityPredictionHelper"
    }
}
