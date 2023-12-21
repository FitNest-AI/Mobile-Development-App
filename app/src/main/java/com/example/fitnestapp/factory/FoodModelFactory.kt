package com.example.fitnestapp.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnestapp.data.repo.FoodRepo
import com.example.fitnestapp.di.Injection
import com.example.fitnestapp.ui.diet.DietViewModel

class FoodModelFactory(private val repo: FoodRepo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DietViewModel::class.java) -> {
                DietViewModel(repo) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: FoodModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): FoodModelFactory {
            return INSTANCE ?: synchronized(FoodModelFactory::class.java) {
                INSTANCE ?: FoodModelFactory(
                    Injection.provideFoodRepository(context)
                ).also { INSTANCE = it }
            }
        }
    }
}