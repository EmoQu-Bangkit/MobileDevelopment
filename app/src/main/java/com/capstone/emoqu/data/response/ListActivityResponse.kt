package com.capstone.emoqu.data.response

import com.google.gson.annotations.SerializedName

data class ListActivityResponse(
    @SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @SerializedName("activities")
    val activities: List<Activity>

)

data class Activity(

    @field:SerializedName("id")
    val id: String,

    @SerializedName("time_stamp")
    val timeStamp: String,

    @SerializedName("time")
    val time: String,

    @SerializedName("quality")
    val quality: Int,

    @SerializedName("activities")
    val activities: String,

    @SerializedName("duration")
    val duration: Int,

    @SerializedName("notes")
    val notes: String
)
