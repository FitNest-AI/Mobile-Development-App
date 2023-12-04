package com.example.fitnestapp.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitnestapp.R
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.local.dataStore
import com.example.fitnestapp.databinding.ActivityMainBinding
import com.example.fitnestapp.databinding.ActivityOnboardingBinding
import com.example.fitnestapp.ui.MainActivity
import com.example.fitnestapp.ui.createaccount.CreateAccountActivity
import com.example.fitnestapp.ui.home.HomeFragment
import com.example.fitnestapp.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var userPreference: UserPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(this.dataStore)
        CoroutineScope(Dispatchers.IO).launch {
            val user = userPreference.getSession().first()
            if (user.isLogin){
                startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
                finish()
            }
        }

        Onclick()
    }

    private fun Onclick() {
        binding.btnGetStarted.setOnClickListener{
            startActivity(Intent(this@OnboardingActivity, CreateAccountActivity::class.java))
            finish()
        }

        binding.tvBtnSignin.setOnClickListener{
            startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
            finish()
        }
    }
}