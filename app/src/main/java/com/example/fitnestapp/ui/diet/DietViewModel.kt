package com.example.fitnestapp.ui.diet

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnestapp.data.remote.response.RecommendationItem
import com.example.fitnestapp.data.remote.response.ResponseRegist
import com.example.fitnestapp.data.repo.UserRepo
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DietViewModel(private val repo: UserRepo) : ViewModel() {

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> = _errorMessage

    private val _food = MutableLiveData<List<RecommendationItem>>()
    val food: LiveData<List<RecommendationItem>> get() = _food

    fun getFood(workoutName: String, page: Int) {
        viewModelScope.launch {
            try {
                val response = repo.getFood(workoutName, page)
                if (response.isSuccessful && response.body() != null) {
                    _food.value = response.body()!!.recommendation?.filterNotNull()
                    Log.d("ResponFood", response.body()?.recommendation.toString())
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ResponseRegist::class.java)
                errorMessage.postValue(errorBody.message)
                Log.e("Insert Profile Error", "HTTP Exception: ${e.message}", e)
            } catch (e: Exception) {
                errorMessage.postValue("Terjadi kesalahan saat memasukan dataa")
                Log.e("Insert Profile Error", "General Exception: ${e.message}", e)
            }
        }
    }

}