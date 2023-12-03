package com.example.fitnestapp.data.local

data class UserModel(
    val token: String,
    val email: String,
    val password: String,
    val username: String,
    val isLogin: Boolean
)