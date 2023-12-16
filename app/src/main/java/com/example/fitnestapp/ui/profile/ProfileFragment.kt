package com.example.fitnestapp.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.fitnestapp.R
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.local.dataStore
import com.example.fitnestapp.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var followBinding: FragmentProfileBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProfileBinding.bind(view)
        followBinding = binding

    }

}