package com.example.fitnestapp.ui.set

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitnestapp.databinding.ActivitySetBinding
import com.example.fitnestapp.ui.camera.CameraActivity
import com.example.fitnestapp.ui.camera.CameraX

class SetActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnPlay.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }
}