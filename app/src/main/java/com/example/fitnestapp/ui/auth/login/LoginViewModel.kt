package com.example.fitnestapp.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnestapp.data.local.UserModel
import com.example.fitnestapp.data.remote.response.ErrorResponse
import com.example.fitnestapp.data.remote.response.ResponseLogin
import com.example.fitnestapp.data.repo.UserRepo
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val userRepo: UserRepo): ViewModel() {

    private val _login = MutableLiveData<ResponseLogin>()
    val login: LiveData<ResponseLogin> = _login

    val loginStatus: MutableLiveData<Boolean> = MutableLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postLogin(email: String, password: String){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = userRepo.login(email,password)
                _isLoading.value = false
                if (response.success == true){
                    val token = response.data?.token
                    saveSession(UserModel(email, token ?: "", true))
                    loginStatus.postValue(true)
                }else{
                    loginStatus.postValue(false)
                }
            }catch (e: HttpException) {
                _isLoading.postValue(false)
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                Log.d("Login", "$errorMessage")
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepo.saveSession(user)
        }
    }
}