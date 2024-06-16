package com.capstone.emoqu.data.response

import com.google.gson.annotations.SerializedName

data class AddActivityResponse(

    @field:SerializedName("data")
    val data: AddActivityResult?,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String

)

data class AddActivityResult(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("mood")
    val mood: Int,

    @field:SerializedName("activities")
    val activities: String,

    @field:SerializedName("duration")
    val duration: Int,

    @field:SerializedName("notes")
    val notes: String,

    @field:SerializedName("createdAt")
    val createdAt: String
)
