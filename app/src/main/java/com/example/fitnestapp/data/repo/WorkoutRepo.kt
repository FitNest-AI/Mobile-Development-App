package com.example.fitnestapp.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.fitnestapp.data.local.UserModel
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.remote.response.ResponseWorkout
import com.example.fitnestapp.data.remote.retrofit.ApiService
import retrofit2.Response

class WorkoutRepo(private val apiService: ApiService, private val userPreference: UserPreference) {

    suspend fun getWorkout(token: String): Response<ResponseWorkout> {
        return apiService.getWorkout(token)
    }

    fun getSession(): LiveData<UserModel> {
        return userPreference.getSession().asLiveData()
    }


    companion object {
        @Volatile
        private var instance: WorkoutRepo? = null
        fun getInstance(apiService: ApiService, userPreference: UserPreference): WorkoutRepo =
            instance ?: synchronized(this) {
                instance ?: WorkoutRepo(apiService, userPreference)
            }.also { instance = it }
    }
}