package com.example.fitnestapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.fitnestapp.data.local.UserModel
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.local.dataStore
import com.example.fitnestapp.data.remote.response.UserLogin
import com.example.fitnestapp.data.repo.UserRepo
import com.example.fitnestapp.databinding.ActivityLoginBinding
import com.example.fitnestapp.factory.UserModelFactory
import com.example.fitnestapp.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPreferences: UserPreference
    private val loginViewModel by viewModels<LoginViewModel> {
        UserModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreference.getInstance(this.dataStore)

        loginViewModel.isLoading.observe(this){
            showLoading(it)
        }

        postLogin()
        loginViewModel.login.observe(this){response ->
            val message = "Login Success"
            if (response.success == true){
                CoroutineScope(Dispatchers.Main).launch {
                    val saveToken = async(Dispatchers.IO) {
                        userPreferences.saveSession(
                            UserModel(
                                response.data?.email.toString(),
                                response.data?.username.toString(),
                                response.data?.token.toString(),
                                true
                            )
                        )
                    }
                    saveToken.await()

                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun postLogin(){
        binding.btnLogin.setOnClickListener{
            loginViewModel.postLogin(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}