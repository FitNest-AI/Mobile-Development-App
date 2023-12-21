package com.example.fitnestapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Workout(
    val title: String,
    val time: String,
//    val desc: String,
//    val day: String,
//    val rest: Int,
//    val moveset: ArrayList<String>,
//    val photo: Int,
) : Parcelable
