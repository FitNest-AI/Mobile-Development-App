package com.example.fitnestapp.ui.createaccount

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fitnestapp.R
import com.example.fitnestapp.databinding.ActivityCreateAccountBinding
import com.example.fitnestapp.factory.UserModelFactory
import com.example.fitnestapp.ui.login.LoginActivity

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding

    private val viewModel by viewModels<CreateAccViewModel> {
        UserModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreate.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()
            viewModel.register(email, password, confirmPassword)
            observeSignup()
        }

    }


    private fun observeSignup() {
        viewModel.registerStatus.observe(this) { isSuccess ->
            if (isSuccess) {
                binding.warningText.text = getString(R.string.success_register)
                binding.warningText.setTextColor(ContextCompat.getColor(this, R.color.greeen))
            } else {
                Toast.makeText(this, "Register failed.", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                binding.warningText.text = errorMessage
            }
        }
    }
}