package com.example.fitnestapp.ui.workoutlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnestapp.data.remote.response.ResponseRegist
import com.example.fitnestapp.data.remote.response.ResponseWorkout
import com.example.fitnestapp.data.remote.response.WorkoutItem
import com.example.fitnestapp.data.repo.WorkoutRepo
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WorkoutViewModel(private val repo: WorkoutRepo) : ViewModel() {

    private val _workout = MutableLiveData<List<WorkoutItem>>()
    val workout: LiveData<List<WorkoutItem>> get() = _workout

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> = _errorMessage
    fun getWorkout() {

        try {
            viewModelScope.launch {
                val response = repo.getWorkout()
                if (response.isSuccessful) {
                    val workoutListNullable: List<WorkoutItem?>? = response.body()?.data?.workout
                    val workoutList: List<WorkoutItem> =
                        workoutListNullable?.filterNotNull() ?: emptyList()
                    _workout.value = workoutList
                } else {
                    // Handle error
                }
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ResponseWorkout::class.java)
            errorMessage.postValue(errorBody.message)
            Log.e("Insert Profile Error", "HTTP Exception: ${e.message}", e)
        } catch (e: Exception) {
            errorMessage.postValue("Terjadi kesalahan saat memasukan dataa")
            Log.e("Insert Profile Error", "General Exception: ${e.message}", e)
        }

    }
}