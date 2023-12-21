package com.example.fitnestapp.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.fitnestapp.data.local.UserModel
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.remote.response.ResponseFood
import com.example.fitnestapp.data.remote.retrofit.ApiService
import retrofit2.Response

class FoodRepo(private val apiService: ApiService, private val userPreference: UserPreference) {

    suspend fun getFood(): Response<ResponseFood> {
        return apiService.getFood()
    }

    fun getSession(): LiveData<UserModel> {
        return userPreference.getSession().asLiveData()
    }

    companion object {
        @Volatile
        private var instance: FoodRepo? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference): FoodRepo =
            instance ?: synchronized(this) {
                instance ?: FoodRepo(apiService, userPreference).apply { instance = this }
            }
    }

}