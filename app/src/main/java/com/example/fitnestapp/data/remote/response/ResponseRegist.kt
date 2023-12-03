package com.example.fitnestapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseRegist(

	@field:SerializedName("data")
	val data: DataRegist? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataRegist(

	@field:SerializedName("user")
	val user: UserRegist? = null
)

data class UserRegist(

	@field:SerializedName("roleId")
	val roleId: String? = null,

	@field:SerializedName("verify")
	val verify: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
