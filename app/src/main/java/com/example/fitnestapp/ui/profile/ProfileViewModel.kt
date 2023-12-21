package com.example.fitnestapp.ui.profile

import android.service.autofill.UserData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnestapp.data.local.EditUserRequest
import com.example.fitnestapp.data.remote.response.DataUser
import com.example.fitnestapp.data.remote.response.GoalItem
import com.example.fitnestapp.data.remote.response.ResponseUser
import com.example.fitnestapp.data.repo.UserRepo
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: UserRepo) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> get() = _username

    fun getUser() {
        viewModelScope.launch {
            try {
                val response = repo.getUser()
                _username.value = response.data.user.username
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    suspend fun updateUser(request: EditUserRequest): ResponseUser {
        return repo.updateUser(request)
    }
}