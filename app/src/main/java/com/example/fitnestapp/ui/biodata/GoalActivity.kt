package com.example.fitnestapp.ui.biodata

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.compose.ui.graphics.Color
import com.example.fitnestapp.R
import com.example.fitnestapp.databinding.ActivityGoalBinding
import com.example.fitnestapp.ui.MainActivity

class GoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalBinding
    private var activeButtonGoal: Button? = null
    private var activeButtonTarget: Button? = null
    private var activeButtonFood: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            startActivity(Intent(this@GoalActivity, MainActivity::class.java))
            finish()
        }

        buttonClick()
    }

    private fun buttonClick() {
         val onClickListener = View.OnClickListener { view ->
            when(view.id){
                R.id.btnGainMuscle -> handleButtonClickGoal(binding.btnGainMuscle)
                R.id.btnLoseWeight -> handleButtonClickGoal(binding.btnLoseWeight)
                R.id.btnKeepFit -> handleButtonClickGoal(binding.btnKeepFit)
                R.id.btnImprovingPosture -> handleButtonClickGoal(binding.btnImprovingPosture)

                R.id.btnLegs -> handleButtonClickTarget(binding.btnLegs)
                R.id.btnTriceps -> handleButtonClickTarget(binding.btnTriceps)
                R.id.btnGlutes -> handleButtonClickTarget(binding.btnGlutes)
                R.id.btnBack -> handleButtonClickTarget(binding.btnBack)
                R.id.btnChest -> handleButtonClickTarget(binding.btnChest)
                R.id.btnAbs -> handleButtonClickTarget(binding.btnAbs)
                R.id.btnShoulder -> handleButtonClickTarget(binding.btnShoulder)
                R.id.btnHand -> handleButtonClickTarget(binding.btnHand)
                R.id.btnBiceps -> handleButtonClickTarget(binding.btnBiceps)
                R.id.btnHamstring -> handleButtonClickTarget(binding.btnHamstring)

                R.id.btnFlexitarian -> handleButtonClickFood(binding.btnFlexitarian)
                R.id.btnVegan -> handleButtonClickFood(binding.btnVegan)
                R.id.btnVegetarian -> handleButtonClickFood(binding.btnVegetarian)
                R.id.btnPescetarian -> handleButtonClickFood(binding.btnPescetarian)
                R.id.btnCarnivore -> handleButtonClickFood(binding.btnCarnivore)
            }
        }
        binding.btnGainMuscle.setOnClickListener(onClickListener)
        binding.btnLoseWeight.setOnClickListener(onClickListener)
        binding.btnKeepFit.setOnClickListener(onClickListener)
        binding.btnImprovingPosture.setOnClickListener(onClickListener)

        binding.btnLegs.setOnClickListener(onClickListener)
        binding.btnTriceps.setOnClickListener(onClickListener)
        binding.btnGlutes.setOnClickListener(onClickListener)
        binding.btnBack.setOnClickListener(onClickListener)
        binding.btnChest.setOnClickListener(onClickListener)
        binding.btnAbs.setOnClickListener(onClickListener)
        binding.btnShoulder.setOnClickListener(onClickListener)
        binding.btnHand.setOnClickListener(onClickListener)
        binding.btnBiceps.setOnClickListener(onClickListener)
        binding.btnHamstring.setOnClickListener(onClickListener)

        binding.btnFlexitarian.setOnClickListener(onClickListener)
        binding.btnVegan.setOnClickListener(onClickListener)
        binding.btnVegetarian.setOnClickListener(onClickListener)
        binding.btnPescetarian.setOnClickListener(onClickListener)
        binding.btnCarnivore.setOnClickListener(onClickListener)

    }

    private fun handleButtonClickGoal(button: Button) {
        if (activeButtonGoal != null) {
            resetButtonColor(activeButtonGoal!!)
        }

        button.setBackgroundColor(getColor(R.color.red))
        activeButtonGoal = button
    }

    private fun handleButtonClickTarget(button: Button) {
        if (activeButtonTarget != null) {
            resetButtonColor(activeButtonTarget!!)
        }

        button.setBackgroundColor(getColor(R.color.red))
        activeButtonTarget = button
    }

    private fun handleButtonClickFood(button: Button) {
        if (activeButtonFood != null) {
            resetButtonColor(activeButtonFood!!)
        }

        button.setBackgroundColor(getColor(R.color.red))
        activeButtonFood = button
    }

    private fun resetButtonColor(button: Button) {
        button.setBackgroundColor(getColor(R.color.white))
    }
}