package com.example.fitnestapp.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnestapp.data.remote.response.ErrorResponse
import com.example.fitnestapp.data.remote.response.ResponseLogin
import com.example.fitnestapp.data.repo.UserRepo
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val userRepo: UserRepo): ViewModel() {

    private val _login = MutableLiveData<ResponseLogin>()
    val login: LiveData<ResponseLogin> = _login

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLogin = MutableLiveData<String?>()
    val isLogin: LiveData<String?> get() = _isLogin

    fun postLogin(email: String, password: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = userRepo.login(email,password)
                _login.postValue(response)
                _isLoading.postValue(false)
            }catch (e: HttpException) {
                _isLoading.postValue(false)
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                Log.d("Login", "$errorMessage")
            }
        }
    }
}