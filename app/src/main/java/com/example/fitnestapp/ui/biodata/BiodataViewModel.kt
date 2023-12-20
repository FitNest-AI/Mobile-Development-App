package com.example.fitnestapp.ui.biodata

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnestapp.data.local.UserModel
import com.example.fitnestapp.data.remote.response.DietPrefItem
import com.example.fitnestapp.data.remote.response.GoalItem
import com.example.fitnestapp.data.remote.response.LevelItem
import com.example.fitnestapp.data.remote.response.ProfileResponse
import com.example.fitnestapp.data.remote.response.ResponseRegist
import com.example.fitnestapp.data.remote.response.TargetMuscleItem
import com.example.fitnestapp.data.repo.UserRepo
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Date

class BiodataViewModel(private val repo: UserRepo) : ViewModel() {

    private val _profile = MutableLiveData<ProfileResponse>()
    val profile: LiveData<ProfileResponse> = _profile
    val errorMessage = MutableLiveData<String?>()
    val insertProfileStatus: MutableLiveData<Boolean> = MutableLiveData()

    fun insertProfile(token: String, firstname: String, lastname: String, gender:String, dateOfBirth: Date, height: Int, weight: Int, goalId: List<String>, levelId: String, targetMuscleId: List<String>,dietPrefId: String) {
        viewModelScope.launch {
            try {
                val response = repo.insertProfile(token,firstname,lastname, gender, dateOfBirth, height, weight, goalId, levelId, targetMuscleId, dietPrefId)
                _profile.postValue(response)
                saveSession(UserModel("", token, true, isInsertProfile = true))
                insertProfileStatus.postValue(true)
                Log.d("ApiResponse", "Response: $response")
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ResponseRegist::class.java)
                errorMessage.postValue(errorBody.message)
                insertProfileStatus.postValue(false)
                Log.e("Insert Profile Error", "HTTP Exception: ${e.message}", e)
            } catch (e: Exception) {
                errorMessage.postValue("Terjadi kesalahan saat memasukan dataa")
                insertProfileStatus.postValue(false)
                Log.e("Insert Profile Error", "General Exception: ${e.message}", e)
            }
        }

    }

    private val _goals = MutableLiveData<List<GoalItem>>()
    val goals: LiveData<List<GoalItem>> get() = _goals

    fun getGoals() {
        viewModelScope.launch {
            val response = repo.getGoals()
            if (response.isSuccessful) {
                _goals.value = response.body()?.data?.goal
            } else {
                // Handle error
            }
        }
    }

    private val _level = MutableLiveData<List<LevelItem>>()
    val level: LiveData<List<LevelItem>> get() = _level

    fun getLevel() {
        viewModelScope.launch {
            val response = repo.getLevel()
            if (response.isSuccessful) {
                _level.value = response.body()?.data?.level
            } else {
                // Handle error
            }
        }
    }

    private val _targetMuscle = MutableLiveData<List<TargetMuscleItem>>()
    val targetMuscle: LiveData<List<TargetMuscleItem>> get() = _targetMuscle

    fun getTargetMuscle() {
        viewModelScope.launch {
            val response = repo.getTargetMuscle()
            if (response.isSuccessful) {
                _targetMuscle.value = response.body()?.data?.targetMuscle
            } else {
                // Handle error
            }
        }
    }

    private val _dietPref = MutableLiveData<List<DietPrefItem>>()
    val dietPref: LiveData<List<DietPrefItem>> get() = _dietPref

    fun getDietPref() {
        viewModelScope.launch {
            val response = repo.getDietPref()
            if (response.isSuccessful) {
                _dietPref.value = response.body()?.data?.dietPref
            } else {
                // Handle error
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repo.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repo.getSession()
    }


}