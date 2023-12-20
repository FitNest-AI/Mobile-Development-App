package com.example.fitnestapp.ui.diet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnestapp.R
import com.example.fitnestapp.data.model.Diet
import com.example.fitnestapp.data.model.Workout
import com.example.fitnestapp.databinding.FragmentDietBinding
import com.example.fitnestapp.databinding.FragmentWorkoutListBinding
import com.example.fitnestapp.ui.home.HomeAdapter

class DietFragment : Fragment(R.layout.fragment_diet) {

    private lateinit var binding: FragmentDietBinding
    private val list = ArrayList<Diet>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDietBinding.bind(view)

        binding.rvDiet.setHasFixedSize(true)
        binding.rvDiet.layoutManager = LinearLayoutManager(context)
        val popularSetAdapter = DietFragmentAdapter(list)
        binding.rvDiet.adapter = popularSetAdapter

        list.addAll(getListDiet())
    }

    private fun getListDiet(): ArrayList<Diet> {
        val name = resources.getStringArray(R.array.data_diet_name)
        val calorie = resources.getStringArray(R.array.data_diet_calorie)
        val fat = resources.getStringArray(R.array.data_diet_fat)
        val carbohydrate = resources.getStringArray(R.array.data_diet_carbohydrate)
        val protein = resources.getStringArray(R.array.data_diet_protein)
        val listDiet = ArrayList<Diet>()
        for (i in name.indices) {
            val diet = Diet(name[i], calorie[i], fat[i], carbohydrate[i], protein[i])
            listDiet.add(diet)
        }
        return listDiet
    }
}