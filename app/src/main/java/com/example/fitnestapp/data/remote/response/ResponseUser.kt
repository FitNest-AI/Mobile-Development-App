package com.example.fitnestapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseUser(

	@field:SerializedName("data")
	val data: DataUser,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataUser(

	@field:SerializedName("user")
	val user: User
)

data class User(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("roleId")
	val roleId: String,

	@field:SerializedName("verify")
	val verify: Boolean,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)
