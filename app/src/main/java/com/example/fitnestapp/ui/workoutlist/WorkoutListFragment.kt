package com.example.fitnestapp.ui.workoutlist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnestapp.R
import com.example.fitnestapp.data.model.Diet
import com.example.fitnestapp.data.model.Workout
import com.example.fitnestapp.data.remote.response.RecommendationItem
import com.example.fitnestapp.data.remote.response.WorkoutItem
import com.example.fitnestapp.databinding.FragmentWorkoutListBinding
import com.example.fitnestapp.factory.WorkoutModelFactory
import com.example.fitnestapp.ui.home.HomeAdapter

class WorkoutListFragment : Fragment(R.layout.fragment_workout_list) {

    private val list = ArrayList<Workout>()

    private var followBinding: FragmentWorkoutListBinding? = null

    private lateinit var listWorkoutSet: HomeAdapter
    private val viewModel by viewModels<WorkoutViewModel> {
        WorkoutModelFactory.getInstance(this.requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentWorkoutListBinding.bind(view)
        followBinding = binding

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

//        list.addAll(getListWorkout())

        binding.search.clearFocus()
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                filter(msg)
                return false
            }
        })

        viewModel.getWorkout()

        viewModel.workout.observe(viewLifecycleOwner) { workoutItems ->
            val diets =
                workoutItems?.map {
                    convertFoodItemToDiet(it)
                }?.let { ArrayList(it) } ?: arrayListOf()

            binding.rvWorkoutList.setHasFixedSize(true)
            binding.rvWorkoutList.layoutManager = LinearLayoutManager(context)
            listWorkoutSet = HomeAdapter(diets)
            binding.rvWorkoutList.adapter = listWorkoutSet
        }
    }

    private fun convertFoodItemToDiet(workoutItem: WorkoutItem): Workout {
        return Workout(
            title = workoutItem.name.toString(),
            time = workoutItem.time.toString(),
        )
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

    private fun filter(text: String) {

        val filteredlist: ArrayList<Workout> = ArrayList()

        for (item in list) {
            if (item.title.toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            listWorkoutSet.filterList(filteredlist)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        followBinding?.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}