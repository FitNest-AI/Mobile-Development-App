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
import com.example.fitnestapp.factory.UserModelFactory

class DietFragment : Fragment(R.layout.fragment_diet) {

    private lateinit var binding: FragmentDietBinding
    private val viewModel by viewModels<DietViewModel> {
        UserModelFactory.getInstance(this.requireContext())
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

        viewModel.getFood()

    }


    private fun convertFoodItemToDiet(foodItem: RecommendationItem): Diet {
        return Diet(
            name = foodItem.label.toString(),
            calorie = foodItem.calories.toString(),
            fat = foodItem.fat.toString(),
            carbohydrate = foodItem.carbs.toString(),
            protein = foodItem.protein.toString()
        )
    }


//    private fun getListDiet(foodItem: RecommendationItem): ArrayList<Diet> {
//        val label = foodItem.label.toString()
//        val calorie = foodItem.calories
//        val fat = foodItem.fat
//        val carbohydrate = foodItem.carbs
//        val protein = foodItem.protein
//        val listDiet = ArrayList<Diet>()
//        for (i in label.indices) {
//            val diet = Diet(label[i], calorie[i], fat[i], carbohydrate[i], protein[i])
//            listDiet.add(diet)
//        }
//        return listDiet
//    }
}