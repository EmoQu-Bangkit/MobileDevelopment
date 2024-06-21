package com.capstone.emoqu.utils

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun isToday(dateString: String): Boolean {
        val todayCalendar = Calendar.getInstance()
        val activityCalendar = Calendar.getInstance().apply {
            time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString) ?: Date()
        }

        return todayCalendar.get(Calendar.YEAR) == activityCalendar.get(Calendar.YEAR) &&
                todayCalendar.get(Calendar.DAY_OF_YEAR) == activityCalendar.get(Calendar.DAY_OF_YEAR)
    }
}