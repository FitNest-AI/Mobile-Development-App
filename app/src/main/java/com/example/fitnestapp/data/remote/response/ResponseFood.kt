package com.example.fitnestapp.data.remote.response
import com.google.gson.annotations.SerializedName

data class ResponseFood(

    @field:SerializedName("recommendation")
    val recommendation: List<RecommendationItem?>? = null,

    @field:SerializedName("total_pages")
    val totalPages: Int? = null,

    @field:SerializedName("current_page")
    val currentPage: Int? = null
)

data class RecommendationItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("Carbs")
    val carbs: Any? = null,

    @field:SerializedName("Fat")
    val fat: Any? = null,

    @field:SerializedName("label")
    val label: String? = null,

    @field:SerializedName("type")
    val type: Int? = null,

    @field:SerializedName("Calories")
    val calories: Any? = null,

    @field:SerializedName("Protein")
    val protein: Any? = null
)