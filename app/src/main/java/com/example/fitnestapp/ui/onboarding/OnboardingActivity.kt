package com.example.fitnestapp.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitnestapp.R
import com.example.fitnestapp.databinding.ActivityMainBinding
import com.example.fitnestapp.databinding.ActivityOnboardingBinding
import com.example.fitnestapp.ui.MainActivity
import com.example.fitnestapp.ui.createaccount.CreateAccountActivity
import com.example.fitnestapp.ui.home.HomeFragment
import com.example.fitnestapp.ui.login.LoginActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetStarted.setOnClickListener{
            startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
            finish()
        }

        binding.tvBtnSignin.setOnClickListener{
            startActivity(Intent(this@OnboardingActivity, CreateAccountActivity::class.java))
            finish()
        }

    }
}