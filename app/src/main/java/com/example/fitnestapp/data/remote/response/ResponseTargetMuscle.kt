package com.example.fitnestapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseTargetMuscle(

	@field:SerializedName("data")
	val data: DataTargetMuscle,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataTargetMuscle(

	@field:SerializedName("count")
	val count: Int,

	@field:SerializedName("targetMuscle")
	val targetMuscle: List<TargetMuscleItem>
)

data class TargetMuscleItem(

	@field:SerializedName("__v")
	val v: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("_id")
	val id: String
)
