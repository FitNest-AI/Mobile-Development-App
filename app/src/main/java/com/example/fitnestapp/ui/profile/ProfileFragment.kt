package com.example.fitnestapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.fitnestapp.R
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.local.dataStore
import com.example.fitnestapp.databinding.FragmentProfileBinding
import com.example.fitnestapp.factory.UserModelFactory
import com.example.fitnestapp.factory.WorkoutModelFactory
import com.example.fitnestapp.ui.onboarding.OnboardingActivity
import com.example.fitnestapp.ui.workoutlist.WorkoutViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var followBinding: FragmentProfileBinding? = null

    private lateinit var userPreferences: UserPreference

    private val viewModel by viewModels<ProfileViewModel> {
        UserModelFactory.getInstance(this.requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentProfileBinding.bind(view)
        followBinding = binding

        userPreferences = UserPreference.getInstance(requireContext().dataStore)

        binding.btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Logout")
            builder.setMessage("Are you sure want to Logout?")
            builder.setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    userPreferences.logout()
                }
                val intent = Intent(requireContext(), OnboardingActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
            builder.create()
        }
        viewModel.getUser()
        viewModel.username.observe(viewLifecycleOwner, Observer { username ->
            binding.username.text = username.toString()
            Log.d("namaku","Username : $username")
        })

        binding.btnEdit.setOnClickListener {
            startActivity(Intent(requireContext(),EditProfileActivity::class.java))
        }
    }


}