package com.example.fitnestapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sets")
data class Set(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val setName : String ,
    val setTime : String ,
    val setImage : Int
)