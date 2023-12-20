package com.example.fitnestapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Diet(
    val name: String,
    val calorie: String,
    val fat: String,
    val carbohydrate: String,
    val protein: String
):Parcelable
