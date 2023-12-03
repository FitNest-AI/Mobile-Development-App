package com.example.fitnestapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseLogin(

	@field:SerializedName("data")
	val data: DataLogin? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataLogin(

	@field:SerializedName("user")
	val user: UserLogin? = null
)

data class UserLogin(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("roleId")
	val roleId: String? = null,

	@field:SerializedName("verify")
	val verify: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
