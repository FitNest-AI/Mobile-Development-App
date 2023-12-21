package com.example.fitnestapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnestapp.R
import com.example.fitnestapp.data.local.UserModel
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.local.dataStore
import com.example.fitnestapp.data.remote.response.UserLogin
import com.example.fitnestapp.databinding.FragmentHomeBinding
import com.example.fitnestapp.data.model.Workout
import com.example.fitnestapp.data.remote.response.WorkoutItem
import com.example.fitnestapp.factory.WorkoutModelFactory
import com.example.fitnestapp.ui.workoutlist.WorkoutListFragment
import com.example.fitnestapp.ui.workoutlist.WorkoutViewModel


class HomeFragment : Fragment(R.layout.fragment_home) {

    private val list = ArrayList<Workout>()

    private lateinit var userPreferences: UserPreference
    private var Binding: FragmentHomeBinding? = null
    private lateinit var listWorkoutSet: HomeAdapter
    private val viewModel by viewModels<WorkoutViewModel> {
        WorkoutModelFactory.getInstance(this.requireContext())
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        Binding = binding

//        list.addAll(getListWorkout())

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        viewModel.getWorkout()

        viewModel.workout.observe(viewLifecycleOwner) { workoutItems ->
            val diets =
                workoutItems?.map {
                    convertFoodItemToDiet(it)
                }?.let { ArrayList(it) } ?: arrayListOf()

            binding.rvMain.setHasFixedSize(true)
            binding.rvMain.layoutManager = LinearLayoutManager(context)
            listWorkoutSet = HomeAdapter(diets)
            binding.rvMain.adapter = listWorkoutSet
        }

        binding.layoutDiscover.setOnClickListener{
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, WorkoutListFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
    private fun convertFoodItemToDiet(workoutItem: WorkoutItem): Workout {
        return Workout(
            title = workoutItem.name.toString(),
            time = workoutItem.time.toString(),
//            desc = workoutItem.desc.toString(),
//            day = workoutItem.day.toString(),
//            rest = workoutItem.rest.toString().toInt(),
//            moveset = workoutItem.moveset.
        )
    }

    private fun showLoading(isLoading: Boolean) {
        Binding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

//    private fun getListWorkout(): ArrayList<Workout> {
//        val dataName = resources.getStringArray(R.array.data_set_name)
//        val dataTime = resources.getStringArray(R.array.data_set_time)
//        val dataImage = resources.obtainTypedArray(R.array.data_set_image)
//        val listWorkout = ArrayList<Workout>()
//        for (i in dataName.indices) {
//            val workout = Workout(dataName[i], dataTime[i], dataImage.getResourceId(i, -1))
//            listWorkout.add(workout)
//        }
//        return listWorkout
//    }



}