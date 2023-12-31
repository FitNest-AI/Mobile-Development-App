package com.example.fitnestapp.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.fitnestapp.data.local.EditUserRequest
import com.example.fitnestapp.data.local.UserModel
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.remote.response.ProfileResponse
import com.example.fitnestapp.data.remote.response.ResponseDietPref
import com.example.fitnestapp.data.remote.response.ResponseGoal
import com.example.fitnestapp.data.remote.response.ResponseLevel
import com.example.fitnestapp.data.remote.response.ResponseLogin
import com.example.fitnestapp.data.remote.response.ResponseRegist
import com.example.fitnestapp.data.remote.response.ResponseTargetMuscle
import com.example.fitnestapp.data.remote.response.ResponseUser
import com.example.fitnestapp.data.remote.response.User
import com.example.fitnestapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.io.IOException
import java.util.Date

class UserRepo(private val apiService: ApiService, private val userPreference: UserPreference) {

    fun getSession(): LiveData<UserModel> {
        return userPreference.getSession().asLiveData()
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun login(email: String, password: String): ResponseLogin {
        return apiService.login(email, password)
    }

    suspend fun register(email: String, password: String, confirmPassword: String): ResponseRegist {
        return apiService.register(email, password, confirmPassword)
    }

    suspend fun insertProfile(token: String, firstname: String, lastname: String, gender:String, dateOfBirth: Date, height: Int, weight: Int, goalId: List<String>, levelId: String, targetMuscleId: List<String>, dietPrefId: String): ProfileResponse {
        return apiService.insertProfile(token, firstname,lastname, gender, dateOfBirth, height, weight, goalId, levelId, targetMuscleId, dietPrefId)
    }

    suspend fun getUserProfile() : ProfileResponse {
        return apiService.getUserProfile()
    }

    suspend fun getUser(): ResponseUser {
        return apiService.getUser().body() ?: throw IOException("Error fetching user data")
    }

    suspend fun updateUser(request: EditUserRequest): ResponseUser {
        return apiService.updateUser(request).body() ?: throw IOException("Error updating user data")
    }

    suspend fun getGoals(): Response<ResponseGoal> {
        return apiService.getGoal()
    }

    suspend fun getLevel(): Response<ResponseLevel> {
        return apiService.getLevel()
    }

    suspend fun getTargetMuscle(): Response<ResponseTargetMuscle> {
        return apiService.getTargetMuscle()
    }

    suspend fun getDietPref(): Response<ResponseDietPref> {
        return apiService.getDietPref()
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