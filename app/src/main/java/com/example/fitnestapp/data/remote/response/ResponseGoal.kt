package com.example.fitnestapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseGoal(

	@field:SerializedName("data")
	val data: DataModel,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataModel(

	@field:SerializedName("goal")
	val goal: List<GoalItem>,

	@field:SerializedName("count")
	val count: Int
)

data class GoalItem(

	@field:SerializedName("__v")
	val v: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("_id")
	val id: String
)
