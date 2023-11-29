package com.example.fitnestapp.ui.createaccount

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitnestapp.databinding.ActivityCreateAccountBinding
import com.example.fitnestapp.ui.login.LoginActivity

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@CreateAccountActivity, LoginActivity::class.java))
            finish()
        }
    }
}