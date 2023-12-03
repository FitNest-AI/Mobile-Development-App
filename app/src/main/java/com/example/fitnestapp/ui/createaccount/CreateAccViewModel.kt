package com.example.fitnestapp.ui.createaccount

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnestapp.data.remote.response.ResponseRegist
import com.example.fitnestapp.data.repo.UserRepo
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CreateAccViewModel(private val repo: UserRepo) : ViewModel() {

    val errorMessage = MutableLiveData<String?>()
    val registerStatus: MutableLiveData<Boolean> = MutableLiveData()

    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            try {
                val response = repo.register(email, password, confirmPassword) // Misalnya ini adalah panggilan API Anda
                registerStatus.postValue(true)
                Log.d("ApiResponse", "Response: $response")
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ResponseRegist::class.java)
                errorMessage.postValue(errorBody.message)
                registerStatus.postValue(false)
                Log.e("RegisterError", "HTTP Exception: ${e.message}", e)
            } catch (e: Exception) {
                errorMessage.postValue("Terjadi kesalahan saat pendaftaran")
                registerStatus.postValue(false)
                Log.e("RegisterError", "General Exception: ${e.message}", e)
            }
        }

    }

}