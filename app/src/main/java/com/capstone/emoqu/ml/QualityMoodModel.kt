package com.capstone.emoqu.ml

import android.content.Context
import com.capstone.emoqu.data.local.databaseAcvitity.ActivityEntity
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

class QualityMoodModel(context: Context) {
    private val interpreter: Interpreter
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    init {
        interpreter = Interpreter(loadModelFile(context))
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("Model_Quality_Condition.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun preprocessData(data: List<ActivityEntity>): List<ActivityEntity> {
        val processedData = data.map { entry ->
            entry.copy(date = dateFormatter.parse(entry.date)?.let { dateFormatter.format(it) } ?: entry.date)
        }

        val activityWeights = mapOf(
            "Sleep" to 0.2,
            "Study" to 0.1,
            "Work" to 0.35,
            "Dating" to 0.05,
            "Self Care" to 0.05,
            "Traveling" to 0.05,
            "Entertainment" to 0.1,
            "Eating" to 0.05,
            "Workout" to 0.05
        )

        val dailyQuality = processedData.groupBy { it.date }.map { (date, entries) ->
            val overallQuality = entries.map { it.quality * (activityWeights[it.activity] ?: 0.0) }.average()
            ActivityEntity(date = date, quality = overallQuality.toInt(), activity = "Overall", duration = 0)
        }

        val qualities = dailyQuality.map { it.quality }
        val q1 = qualities.percentile(25.0)
        val q3 = qualities.percentile(75.0)
        val iqr = q3 - q1
        val lowerBound = q1 - 1.5 * iqr
        val upperBound = q3 + 1.5 * iqr

        val cleanedData = dailyQuality.filter { it.quality >= lowerBound && it.quality <= upperBound }

        val classificationThresholds = mapOf(
            "Terrible" to 0.125000,
            "Bad" to 0.200000,
            "Okay" to 0.300000,
            "Good" to 0.550000,
            "Excellent" to 0.800000
        )

        val classifiedData = cleanedData.map { entry ->
            val category = classifyDay(entry.quality.toDouble(), classificationThresholds)
            entry.copy(activity = category)
        }

        return classifiedData
    }

    private fun classifyDay(qualityScore: Double, thresholds: Map<String, Double>): String {
        for ((category, threshold) in thresholds) {
            if (qualityScore < threshold) {
                return category
            }
        }
        return "Excellent"
    }

    private fun List<Int>.percentile(percentile: Double): Double {
        val sortedList = this.sorted()
        val index = (percentile / 100.0) * (sortedList.size - 1)
        return sortedList[index.toInt()].toDouble()
    }

    fun predict(inputData: FloatArray): FloatArray {
        val input = arrayOf(inputData)
        val output = Array(1) { FloatArray(1) }
        interpreter.run(input, output)
        return output[0]
    }
}
