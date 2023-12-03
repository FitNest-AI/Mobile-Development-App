package com.example.fitnestapp.data.repo

import com.example.fitnestapp.data.local.UserModel
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.remote.response.ResponseLogin
import com.example.fitnestapp.data.remote.response.ResponseRegist
import com.example.fitnestapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepo(private val apiService: ApiService, private val userPreference: UserPreference) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun login(email: String, password: String): ResponseLogin {
        return apiService.login(email, password)
    }

    suspend fun register(email: String, password: String, confirmPassword: String): ResponseRegist {
        return apiService.register(email, password, confirmPassword)
    }


    companion object {
        @Volatile
        private var instance: UserRepo? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference): UserRepo =
            instance ?: synchronized(this) {
                instance ?: UserRepo(apiService, userPreference)
            }.also { instance = it }
    }

}