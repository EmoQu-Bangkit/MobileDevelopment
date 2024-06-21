package com.capstone.emoqu.data.response

import com.google.gson.annotations.SerializedName

data class ListReportResponse(

    @field:SerializedName("reports")
    val reports: List<ReportsItem?>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class ReportsItem(

    @field:SerializedName("timeStamp")
    val timeStamp: String,

    @field:SerializedName("sleep")
    val sleep: Float,

    @field:SerializedName("study")
    val study: Float,

    @field:SerializedName("entertainment")
    val entertainment: Float,

    @field:SerializedName("workout")
    val workout: Float,

    @field:SerializedName("work")
    val work: Float,

    @field:SerializedName("selfCare")
    val selfCare: Float,

    @field:SerializedName("dating")
    val dating: Float,

    @field:SerializedName("traveling")
    val traveling: Float,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("eating")
    val eating: Float,

    @field:SerializedName("positif")
    val positif: Int,

    @field:SerializedName("negatif")
    val negatif: Int,

    @field:SerializedName("predictedDayLabel")
    val predictedDayLabel: String,

    @field:SerializedName("netral")
    val netral: Int,

    @field:SerializedName("predictedDayCondition")
    val predictedDayCondition: String? = null
)
