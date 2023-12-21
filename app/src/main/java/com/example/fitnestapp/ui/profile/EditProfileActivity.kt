package com.example.fitnestapp.ui.profile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.fitnestapp.R
import com.example.fitnestapp.data.local.EditUserRequest
import com.example.fitnestapp.databinding.ActivityBiodataBinding
import com.example.fitnestapp.databinding.ActivityEditProfileBinding
import com.example.fitnestapp.factory.UserModelFactory
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private val viewModel by viewModels<ProfileViewModel> {
        UserModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEdit.setOnClickListener {
            val username = binding.editUsername.text.toString()


            if (username.isNotEmpty()){
                val request = EditUserRequest(username = username)
                updateUser(request)
                startActivity(Intent(this@EditProfileActivity, ProfileFragment::class.java))
                finish()
            }
        }
    }

    private fun updateUser(request: EditUserRequest) {
        lifecycleScope.launch {
            try {
                val response = viewModel.updateUser(request)
            } catch (e: Exception) {
                Log.d("UpdateUser","error")
            }
        }
    }
}