package com.example.fitnestapp.data.local

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean,
    val isInsertProfile: Boolean
)