package com.capstone.emoqu.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("profile")
	val profile: Profile,

	@field:SerializedName("error")
	val error: Boolean
)

data class Profile(

	@field:SerializedName("firstName")
	val firstName: String,

	@field:SerializedName("lastName")
	val lastName: String,

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("about")
	val about: String,

	@field:SerializedName("email")
	val email: String
)
