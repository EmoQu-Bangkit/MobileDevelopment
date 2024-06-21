package com.capstone.emoqu.data.response

import com.google.gson.annotations.SerializedName

data class AddActivityResponse(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

