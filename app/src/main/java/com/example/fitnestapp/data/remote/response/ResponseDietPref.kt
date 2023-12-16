package com.example.fitnestapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseDietPref(

	@field:SerializedName("data")
	val data: DataDietPref,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataDietPref(

	@field:SerializedName("dietPref")
	val dietPref: List<DietPrefItem>,

	@field:SerializedName("count")
	val count: Int
)

data class DietPrefItem(

	@field:SerializedName("__v")
	val v: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("_id")
	val id: String,

	@field:SerializedName("desc")
	val desc: String
)
