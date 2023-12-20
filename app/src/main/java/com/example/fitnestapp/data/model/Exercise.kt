package com.example.fitnestapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    val name: String,
    val time: String,
    val photo: Int,
    val desc: String
) : Parcelable