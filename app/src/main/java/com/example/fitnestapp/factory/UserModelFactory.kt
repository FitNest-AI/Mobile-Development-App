package com.example.fitnestapp.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnestapp.data.repo.UserRepo
import com.example.fitnestapp.di.Injection
import com.example.fitnestapp.ui.auth.createaccount.CreateAccViewModel
import com.example.fitnestapp.ui.auth.login.LoginViewModel
import com.example.fitnestapp.ui.biodata.BiodataViewModel
import com.example.fitnestapp.ui.diet.DietViewModel

class UserModelFactory(private val repo: UserRepo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CreateAccViewModel::class.java) -> {
                CreateAccViewModel(repo) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                return LoginViewModel(repo) as T
            }
            modelClass.isAssignableFrom(BiodataViewModel::class.java) -> {
                return BiodataViewModel(repo) as T
            }
            modelClass.isAssignableFrom(DietViewModel::class.java) -> {
                return DietViewModel(repo) as T
            }


            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): UserModelFactory {
            return INSTANCE ?: synchronized(UserModelFactory::class.java) {
                INSTANCE ?: UserModelFactory(
                    Injection.provideUserRepository(context)
                ).also { INSTANCE = it }
            }
        }
    }
}