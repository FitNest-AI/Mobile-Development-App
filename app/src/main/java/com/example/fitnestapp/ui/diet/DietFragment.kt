package com.example.fitnestapp.ui.diet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnestapp.R
import com.example.fitnestapp.databinding.FragmentWorkoutListBinding

class DietFragment : Fragment(R.layout.fragment_diet) {

    private lateinit var binding: FragmentWorkoutListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWorkoutListBinding.bind(view)

    }
}