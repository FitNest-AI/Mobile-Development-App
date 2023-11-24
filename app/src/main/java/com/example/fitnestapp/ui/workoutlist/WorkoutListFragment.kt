package com.example.fitnestapp.ui.workoutlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnestapp.R
import com.example.fitnestapp.databinding.FragmentHomeBinding
import com.example.fitnestapp.databinding.FragmentWorkoutListBinding
import com.example.fitnestapp.model.Workout
import com.example.fitnestapp.ui.home.HomeAdapter

class WorkoutListFragment : Fragment(R.layout.fragment_workout_list) {

    private val list = ArrayList<Workout>()

    private var followBinding: FragmentWorkoutListBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentWorkoutListBinding.bind(view)
        followBinding = binding

        binding.rvWorkoutList.setHasFixedSize(true)
        binding.rvWorkoutList.layoutManager = LinearLayoutManager(context)
        val listDrakorAdapter = HomeAdapter(list)
        binding.rvWorkoutList.adapter = listDrakorAdapter

        list.addAll(getListWorkout())
    }

    private fun getListWorkout(): ArrayList<Workout> {
        val dataName = resources.getStringArray(R.array.data_set_name)
        val dataTime = resources.getStringArray(R.array.data_set_time)
        val dataImage = resources.obtainTypedArray(R.array.data_set_image)
        val listWorkout = ArrayList<Workout>()
        for (i in dataName.indices) {
            val workout = Workout(dataName[i], dataTime[i], dataImage.getResourceId(i, -1))
            listWorkout.add(workout)
        }
        return listWorkout
    }
}