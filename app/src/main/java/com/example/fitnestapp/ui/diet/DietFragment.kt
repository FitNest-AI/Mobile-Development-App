package com.example.fitnestapp.ui.diet

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnestapp.R
import com.example.fitnestapp.data.model.Diet
import com.example.fitnestapp.data.remote.response.RecommendationItem
import com.example.fitnestapp.databinding.FragmentDietBinding
import com.example.fitnestapp.factory.FoodModelFactory

class DietFragment : Fragment(R.layout.fragment_diet) {

    private lateinit var binding: FragmentDietBinding
    private val list = ArrayList<Diet>()

    private val viewModel by viewModels<DietViewModel> {
        FoodModelFactory.getInstance(this.requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDietBinding.bind(view)
        viewModel.food.observe(viewLifecycleOwner) { foodItems ->
            val diets =
                foodItems?.map {
                    convertFoodItemToDiet(it)
                }?.let { ArrayList(it) } ?: arrayListOf()

            binding.rvDiet.setHasFixedSize(true)
            binding.rvDiet.layoutManager = LinearLayoutManager(context)
            binding.rvDiet.adapter = DietFragmentAdapter(diets)
            Log.d("ResponFood", foodItems.toString())

        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.getSession().observe(viewLifecycleOwner) { userModel ->
            val token = userModel.token
            viewModel.getFood()
            Log.d("ResponSession", userModel.toString())
        }


    }

    private fun convertFoodItemToDiet(foodItem: RecommendationItem): Diet {
        return Diet(
            image = foodItem.image.toString(),
            name = foodItem.label.toString(),
            calorie = foodItem.calories.toString(),
            fat = foodItem.fat.toString(),
            carbohydrate = foodItem.carbs.toString(),
            protein = foodItem.protein.toString()
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

//    private fun getListDiet(): ArrayList<Diet> {
//        val name = resources.getStringArray(R.array.data_diet_name)
//        val calorie = resources.getStringArray(R.array.data_diet_calorie)
//        val fat = resources.getStringArray(R.array.data_diet_fat)
//        val carbohydrate = resources.getStringArray(R.array.data_diet_carbohydrate)
//        val protein = resources.getStringArray(R.array.data_diet_protein)
//        val listDiet = ArrayList<Diet>()
//        for (i in name.indices) {
//            val diet = Diet(name[i], calorie[i], fat[i], carbohydrate[i], protein[i])
//            listDiet.add(diet)
//        }
//        return listDiet
//    }
}