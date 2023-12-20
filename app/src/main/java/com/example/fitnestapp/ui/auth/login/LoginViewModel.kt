package com.example.fitnestapp.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnestapp.data.local.UserModel
import com.example.fitnestapp.data.remote.response.ResponseLogin
import com.example.fitnestapp.data.repo.UserRepo
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val userRepo: UserRepo) : ViewModel() {

    private val _login = MutableLiveData<ResponseLogin>()
    val login: LiveData<ResponseLogin> = _login

    val loginStatus: MutableLiveData<Boolean> = MutableLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isVerify: MutableLiveData<Boolean?> = MutableLiveData()
    val isVerify: MutableLiveData<Boolean?> = _isVerify

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> = _errorMessage


    fun postLogin(email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = userRepo.login(email, password)
                _isLoading.value = false
                if (response.success == true) {
                    val token = response.data?.user?.token
//                    val isVerify = response.data?.user?.verify
                    isVerify.postValue(response.data?.user?.verify)

                    Log.d("Login", "$token")
                    saveSession(UserModel(email, token ?: "", true, isInsertProfile = false))
                    loginStatus.postValue(true)
                } else {
                    loginStatus.postValue(false)
                }
            } catch (e: HttpException) {
                _isLoading.value = false
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ResponseLogin::class.java)
                errorMessage.postValue(errorBody.message)
                Log.d("LoginError", "$errorMessage")
            } catch (e: Exception) {
                _isLoading.value = false
                errorMessage.postValue("Terjadi kesalahan saat pendaftaran")
                loginStatus.postValue(false)
            }
        }
    }

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepo.saveSession(user)
        }
    }
}