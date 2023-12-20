package com.example.fitnestapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnestapp.R
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.model.Workout
import com.example.fitnestapp.databinding.FragmentHomeBinding
import com.example.fitnestapp.ui.createset.CreateSetActivity
import com.example.fitnestapp.ui.workoutlist.WorkoutListFragment


class HomeFragment : Fragment(R.layout.fragment_home) {

    private val list = ArrayList<Workout>()

    private lateinit var userPreferences: UserPreference
    private var followBinding: FragmentHomeBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        followBinding = binding

        binding.rvMain.setHasFixedSize(true)
        binding.rvMain.layoutManager = LinearLayoutManager(context)
        val popularSetAdapter = HomeAdapter(list)
        binding.rvMain.adapter = popularSetAdapter

        list.addAll(getListWorkout())

        binding.layoutDiscover.setOnClickListener {
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, WorkoutListFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.layoutCreateSet.setOnClickListener {
            startActivity(Intent(requireContext(), CreateSetActivity::class.java))
        }

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