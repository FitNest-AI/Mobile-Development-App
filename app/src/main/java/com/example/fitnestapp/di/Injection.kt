package com.example.fitnestapp.di

import android.content.Context
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.local.dataStore
import com.example.fitnestapp.data.remote.retrofit.ApiConfig
import com.example.fitnestapp.data.repo.UserRepo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideUserRepository(context: Context): UserRepo {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)

        return UserRepo.getInstance(apiService, pref)
    }

}