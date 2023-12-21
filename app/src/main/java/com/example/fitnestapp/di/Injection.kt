package com.example.fitnestapp.di

import android.content.Context
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.local.dataStore
import com.example.fitnestapp.data.remote.retrofit.ApiConfigBackend
import com.example.fitnestapp.data.remote.retrofit.ApiConfigFood
import com.example.fitnestapp.data.remote.retrofit.ApiConfigWorkout
import com.example.fitnestapp.data.repo.FoodRepo
import com.example.fitnestapp.data.repo.UserRepo
import com.example.fitnestapp.data.repo.WorkoutRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideUserRepository(context: Context): UserRepo {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfigBackend.getApiService(user.token)
        return UserRepo.getInstance(apiService, pref)
    }

    fun provideFoodRepository(context: Context): FoodRepo {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfigFood.getApiService(user.token)
        return FoodRepo.getInstance(apiService, pref)
    }

    fun provideWorkoutRepository(context: Context): WorkoutRepo {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfigWorkout.getApiService(user.token)
        return WorkoutRepo.getInstance(apiService, pref)
    }

}