package com.example.fitnestapp.ui.workoutlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnestapp.R
import com.example.fitnestapp.databinding.FragmentWorkoutListBinding
import com.example.fitnestapp.model.Workout
import com.example.fitnestapp.ui.home.HomeAdapter

class WorkoutListFragment : Fragment(R.layout.fragment_workout_list) {

    private val list = ArrayList<Workout>()

    private var followBinding: FragmentWorkoutListBinding? = null

    lateinit var listWorkoutSet: HomeAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentWorkoutListBinding.bind(view)
        followBinding = binding

        binding.rvWorkoutList.setHasFixedSize(true)
        binding.rvWorkoutList.layoutManager = LinearLayoutManager(context)
        listWorkoutSet = HomeAdapter(list)
        binding.rvWorkoutList.adapter = listWorkoutSet

        list.addAll(getListWorkout())

        binding.search.clearFocus()
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(msg)
                return false
            }
        })

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

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<Workout> = ArrayList()

        // running a for loop to compare elements.
        for (item in list) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.title.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            listWorkoutSet.filterList(filteredlist)
        }
    }
}