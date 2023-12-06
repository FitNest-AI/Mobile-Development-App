package com.example.fitnestapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnestapp.R
import com.example.fitnestapp.data.local.UserModel
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.local.dataStore
import com.example.fitnestapp.data.remote.response.UserLogin
import com.example.fitnestapp.databinding.FragmentHomeBinding
import com.example.fitnestapp.model.Workout


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