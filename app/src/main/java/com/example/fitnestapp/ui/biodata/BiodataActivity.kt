package com.example.fitnestapp.ui.biodata

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnestapp.R
import com.example.fitnestapp.data.local.UserPreference
import com.example.fitnestapp.data.local.dataStore
import com.example.fitnestapp.data.remote.response.DietPrefItem
import com.example.fitnestapp.data.remote.response.GoalItem
import com.example.fitnestapp.data.remote.response.LevelItem
import com.example.fitnestapp.data.remote.response.TargetMuscleItem
import com.example.fitnestapp.databinding.ActivityBiodataBinding
import com.example.fitnestapp.factory.UserModelFactory
import com.example.fitnestapp.ui.MainActivity
import com.example.fitnestapp.utlis.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BiodataActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener, ItemChange {
    private lateinit var binding: ActivityBiodataBinding
    private var dueDateMillis: Long = System.currentTimeMillis()

    private lateinit var userPreferences: UserPreference

    private lateinit var  checkedGoal: ArrayList<String>
    private lateinit var  checkedLevel: String
    private lateinit var  checkedTm: ArrayList<String>
    private var  checkedDiet: String = ""
    private val viewModel by viewModels<BiodataViewModel> {
        UserModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBiodataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreference.getInstance(this.dataStore)


        val layoutManager = LinearLayoutManager(this)
        binding.rvGoal.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvGoal.addItemDecoration(itemDecoration)

        viewModel.goals.observe(this) { listUser ->
            setGoalData(listUser)
        }
        viewModel.getGoals()

        val layoutManagerLevel = LinearLayoutManager(this)
        binding.rvLevel.layoutManager = layoutManagerLevel
        val itemDecorationLevel = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvLevel.addItemDecoration(itemDecorationLevel)

        viewModel.level.observe(this) { listLevel ->
            setLevelData(listLevel)
        }
        viewModel.getLevel()

        val layoutManagerTm = LinearLayoutManager(this)
        binding.rvTargetMuscle.layoutManager = layoutManagerTm
        val itemDecorationTm = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvTargetMuscle.addItemDecoration(itemDecorationTm)

        viewModel.targetMuscle.observe(this) { listTm ->
            setTargetMuscleData(listTm)
        }
        viewModel.getTargetMuscle()

        val layoutManagerDiet = LinearLayoutManager(this)
        binding.rvDiet.layoutManager = layoutManagerDiet
        val itemDecorationDiet = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvDiet.addItemDecoration(itemDecorationDiet)

        viewModel.dietPref.observe(this) { listDiet ->
            setDietData(listDiet)
        }
        viewModel.getDietPref()



        binding.btnNext.setOnClickListener {

            val firstName = binding.bioFirstName.text.toString()
            val lastName = binding.bioLastName.text.toString()


            val dateOfBirthString = binding.addTvDueDate.text.toString()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val dateOfBirth: Date = dateFormat.parse(dateOfBirthString)


            val height = binding.bioHeight.text.toString().toIntOrNull() ?: 0
            val weight = binding.bioWeight.text.toString().toIntOrNull() ?: 0

            val selectedGenderId = binding.radioGrp.checkedRadioButtonId
            val gender = when (selectedGenderId) {
                R.id.radioMale -> "man"
                R.id.radioFemale -> "woman"
                else -> "Other"
            }
            val goalId = binding.rvGoal

            viewModel.getSession().observe(this){ user ->
                if (user.isLogin){
                    val token = user.token
                    Log.d("TokenUnik", token)
                    Log.d("Cobaan",token+firstName+lastName+gender+dateOfBirth+height+weight+checkedGoal.toString()+checkedLevel+checkedTm.toString()+checkedDiet)
                    viewModel.insertProfile(token,
                        firstName, lastName, gender, dateOfBirth, height, weight, checkedGoal, checkedLevel, checkedTm,checkedDiet)

                    observeProfile()
                }
            }



        }

    }

    override fun goalsItemChange(arrayList: ArrayList<String>) {
        checkedGoal = arrayList
//        Toast.makeText(this,"$checkedGoal", Toast.LENGTH_SHORT).show()
        super.goalsItemChange(arrayList)
    }

    override fun levelItemChange(string: String) {
        checkedLevel = string
//        Toast.makeText(this,"$checkedLevel", Toast.LENGTH_SHORT).show()
        super.levelItemChange(string)
    }

    override fun targetMuscleItemChange(arrayList: ArrayList<String>) {
        checkedTm = arrayList
//        Toast.makeText(this,"$checkedTm", Toast.LENGTH_SHORT).show()
        super.targetMuscleItemChange(arrayList)
    }

    override fun dietItemChange(string: String) {
        checkedDiet = string
//        Toast.makeText(this,"$checkedDiet", Toast.LENGTH_SHORT).show()
        super.dietItemChange(string)
    }

    private fun setGoalData(listGoal: List<GoalItem>) {
        val adapter = GoalAdapter(this,this)
        adapter.submitList(listGoal)
        binding.rvGoal.adapter = adapter
    }

    private fun setLevelData(listLevel: List<LevelItem>) {
        val adapter = LevelAdapter(this,this)
        adapter.submitList(listLevel)
        binding.rvLevel.adapter = adapter
    }

    private fun setTargetMuscleData(listLevel: List<TargetMuscleItem>) {
        val adapter = TargetMuscleAdapter(this,this)
        adapter.submitList(listLevel)
        binding.rvTargetMuscle.adapter = adapter
    }

    private fun setDietData(dietPref: List<DietPrefItem>) {
        val adapter = DietAdapter(this, this)
        adapter.submitList(dietPref)
        binding.rvDiet.adapter = adapter
    }

    private fun observeProfile() {
        viewModel.insertProfileStatus.observe(this) { isSuccess ->
            if (isSuccess) {
                startActivity(Intent(this@BiodataActivity, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Register failed.", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, "Register failed.", Toast.LENGTH_SHORT).show()
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
        val dateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
    }

}