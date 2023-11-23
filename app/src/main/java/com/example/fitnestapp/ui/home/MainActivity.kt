package com.example.fitnestapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnestapp.R
import com.example.fitnestapp.databinding.ActivityMainBinding
import com.example.fitnestapp.ui.createset.CreateSetActivity
import com.example.fitnestapp.ui.workoutlist.WorkoutListActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    private lateinit var recycler : RecyclerView
    lateinit var discoverLayout : ConstraintLayout
    lateinit var bannerLayout : ConstraintLayout
    lateinit var createSetLayout : ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        discoverLayout = binding.layoutDiscover
        bannerLayout = binding.layoutBanner
        createSetLayout = binding.layoutCreateSet

        discoverLayout.setOnClickListener{
            val intent = Intent(this, WorkoutListActivity::class.java)
            startActivity(intent)
        }
        bannerLayout.setOnClickListener{
            val intent = Intent(this, WorkoutListActivity::class.java)
            startActivity(intent)
        }
        createSetLayout.setOnClickListener{
            val intent = Intent(this, CreateSetActivity::class.java)
            startActivity(intent)
        }
    }


}