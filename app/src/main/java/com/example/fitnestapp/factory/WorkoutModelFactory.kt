package com.example.fitnestapp.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fitnestapp.data.repo.FoodRepo
import com.example.fitnestapp.data.repo.WorkoutRepo
import com.example.fitnestapp.di.Injection
import com.example.fitnestapp.ui.diet.DietViewModel
import com.example.fitnestapp.ui.workoutlist.WorkoutViewModel

class WorkoutModelFactory(private val repo: WorkoutRepo) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(WorkoutViewModel::class.java) -> {
                WorkoutViewModel(repo) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WorkoutModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): WorkoutModelFactory {
            return INSTANCE ?: synchronized(WorkoutModelFactory::class.java) {
                INSTANCE ?: WorkoutModelFactory(
                    Injection.provideWorkoutRepository(context)
                ).also { INSTANCE = it }
            }
        }
    }
}