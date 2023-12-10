package com.example.fitnestapp.data.repo

import com.example.fitnestapp.data.remote.retrofit.ApiService

class WorkoutRepo(private val apiService: ApiService) {



    companion object {
        @Volatile
        private var instance: WorkoutRepo? = null
        fun getInstance(apiService: ApiService): WorkoutRepo =
            instance ?: synchronized(this) {
                instance ?: WorkoutRepo(apiService)
            }.also { instance = it }
    }
}