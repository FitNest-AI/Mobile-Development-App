package com.example.fitnestapp.ui.set

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnestapp.R
import com.example.fitnestapp.data.model.Exercise
import com.example.fitnestapp.data.model.Workout
import com.example.fitnestapp.data.remote.response.WorkoutItem
import com.example.fitnestapp.databinding.ActivitySetBinding
import com.example.fitnestapp.ui.camera.CameraActivity
import com.example.fitnestapp.ui.camera.CameraX
import com.example.fitnestapp.ui.home.HomeAdapter

class SetActivity : AppCompatActivity() {

    private val list = ArrayList<Exercise>()

    private lateinit var binding: ActivitySetBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvSet.setHasFixedSize(true)
        binding.rvSet.layoutManager = LinearLayoutManager(this)



        list.addAll(getSetList())
        val receivedListWorkout: WorkoutItem? = intent.getParcelableExtra("data")
        binding.tvName.setText(receivedListWorkout?.name)
        binding.tvDesc.setText(receivedListWorkout?.desc)
        val popularSetAdapter = SetAdapter(receivedListWorkout?.moveset!!)
        binding.rvSet.adapter = popularSetAdapter

        binding.btnPlay.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

    private fun getSetList(): ArrayList<Exercise> {
        val dataName = resources.getStringArray(R.array.data_exercise_name)
        val dataTime = resources.getStringArray(R.array.data_exercise_time)
        val dataImage = resources.obtainTypedArray(R.array.data_exercise_image)
        val dataDesc = resources.getStringArray(R.array.data_exercise_description)
        val listSet = ArrayList<Exercise>()
        for (i in dataName.indices) {
            val set = Exercise(dataName[i], dataTime[i], dataImage.getResourceId(i, -1), dataDesc[i])
            listSet.add(set)
        }
        return listSet
    }
}