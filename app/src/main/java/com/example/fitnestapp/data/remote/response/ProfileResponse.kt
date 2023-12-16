package com.example.fitnestapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Profile(

	@field:SerializedName("firstname")
	val firstname: String? = null,

	@field:SerializedName("goalId")
	val goalId: List<String?>? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("dateOfBirth")
	val dateOfBirth: String? = null,

	@field:SerializedName("targetMuscleId")
	val targetMuscleId: List<String?>? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("lastname")
	val lastname: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("conditionId")
	val conditionId: String? = null,

	@field:SerializedName("dietPrefId")
	val dietPrefId: String? = null,

	@field:SerializedName("levelId")
	val levelId: String? = null,

	@field:SerializedName("__v")
	val v: Int? = null,

	@field:SerializedName("_id")
	val id: String? = null,

	@field:SerializedName("height")
	val height: Int? = null,

	@field:SerializedName("bmi")
	val bmi: Any? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
