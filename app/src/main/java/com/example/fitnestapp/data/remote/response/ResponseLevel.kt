package com.example.fitnestapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseLevel(

	@field:SerializedName("data")
	val data: DataLevel,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class LevelItem(

	@field:SerializedName("__v")
	val v: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("_id")
	val id: String
)

data class DataLevel(

	@field:SerializedName("level")
	val level: List<LevelItem>,

	@field:SerializedName("count")
	val count: Int
)
