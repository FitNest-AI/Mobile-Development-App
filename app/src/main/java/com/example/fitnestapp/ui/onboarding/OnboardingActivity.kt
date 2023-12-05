package com.example.fitnestapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.local.dataStore
import com.example.fitnestapp.databinding.ActivityOnboardingBinding
import com.example.fitnestapp.ui.MainActivity
import com.example.fitnestapp.ui.createaccount.CreateAccountActivity
import com.example.fitnestapp.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var userPreference: UserPreference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(this.dataStore)
        CoroutineScope(Dispatchers.IO).launch {
//            val user = userPreference.getSession().first()
//            if (user.isLogin ){
//                startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
//                finish()
//            }
            auth = Firebase.auth
            val currentUser = auth.currentUser
            updateUi(currentUser)
        }

        Onclick()
    }

    private suspend fun updateUi(currentUser: FirebaseUser?) {
        val user = userPreference.getSession().first()
        if (currentUser != null && user.isLogin) {
            startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun Onclick() {
        binding.btnGetStarted.setOnClickListener {
            startActivity(Intent(this@OnboardingActivity, CreateAccountActivity::class.java))
            finish()
        }

        binding.tvBtnSignin.setOnClickListener {
            startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
            finish()
        }
    }
}