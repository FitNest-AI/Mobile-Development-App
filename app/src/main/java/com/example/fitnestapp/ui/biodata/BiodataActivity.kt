package com.example.fitnestapp.ui.biodata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.fitnestapp.R
import com.example.fitnestapp.data.remote.response.ResponseGoal
import com.example.fitnestapp.databinding.ActivityBiodataBinding
import com.example.fitnestapp.factory.UserModelFactory
import com.example.fitnestapp.ui.MainActivity
import com.example.fitnestapp.ui.auth.createaccount.CreateAccViewModel
import com.example.fitnestapp.utlis.DatePickerFragment
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BiodataActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {
    private lateinit var binding: ActivityBiodataBinding
    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var gender: String

    private val viewModel by viewModels<BiodataViewModel> {
        UserModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiodataBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.btnNext.setOnClickListener {
//            val firstname = binding.bioFirstName.text.toString()
//            val lastname = binding.bioLastName.text.toString()
//            val dateOfbirth = binding.addTvDueDate.text.toString()
//            val height = binding.bioHeight.text.toString().toInt()
//            val weight = binding.bioWeight.text.toString().toInt()
//
//
//            binding.radioGrp.setOnCheckedChangeListener { group, checkedId ->
//                when (checkedId) {
//                    R.id.radioMale -> {
//                        gender = binding.radioMale.text.toString()
//                    }
//                    R.id.radioFemale -> {
//                        gender = binding.radioFemale.text.toString()
//                    }
//                }
//            }
//
//            viewModel.insertProfile(firstname,lastname, gender, dateOfbirth, height, weight)
//            observeSignup()
//        }

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this@BiodataActivity, GoalActivity::class.java))
            finish()
        }


        viewModel.goals.observe(this, { goals ->
            val goalNames = goals.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, goalNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        })
        viewModel.getGoals()

        viewModel.level.observe(this, { level ->
            val levelNames = level.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, levelNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spLevel.adapter = adapter
        })
        viewModel.getLevel()

        viewModel.targetMuscle.observe(this, { targetMuscle ->
            val targetMuscle = targetMuscle.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, targetMuscle)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spTargetMuscle.adapter = adapter
        })
        viewModel.getTargetMuscle()

        viewModel.dietPref.observe(this, { dietPref ->
            val dietPref = dietPref.map { it.name }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dietPref)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spDietPref.adapter = adapter
        })
        viewModel.getDietPref()
    }

    fun genderOnRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked
            val gender: String
            // Check which radio button was clicked
            when (binding.radioGrp.getId()) {
                R.id.radioMale ->
                    if (checked) {
                        gender = binding.radioMale.text.toString()
                    }
                R.id.radioFemale ->
                    if (checked) {
                        gender = binding.radioFemale.text.toString()
                    }
            }
        }
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }
}