package com.example.fitnestapp.ui.biodata

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnestapp.data.remote.response.DietPrefItem
import com.example.fitnestapp.data.remote.response.LevelItem
import com.example.fitnestapp.databinding.ItemLevelBinding

class DietAdapter(val context: Context, var itemChange: ItemChange) : ListAdapter<DietPrefItem, DietAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION
    var checkedDiet: String = String()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, context, itemChange)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review, position)
    }

    inner class MyViewHolder(val binding: ItemLevelBinding, val context: Context, var itemChange: ItemChange) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dietPref: DietPrefItem, position: Int) {
            binding.radioBtn.text = dietPref.name

            // Set the checked state based on whether the current item is selected
            binding.radioBtn.isChecked = position == selectedItemPosition

            binding.radioBtn.setOnClickListener {
                // Update the selected item position
                selectedItemPosition = adapterPosition
                selectedItemPosition = adapterPosition
                checkedDiet = dietPref.id
                // Notify data set changed to update the UI
                notifyDataSetChanged()
                itemChange.dietItemChange(checkedDiet)
                // You can also perform additional actions when a radio button is clicked
                // For example, you can trigger an event or update the selected item in your data
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DietPrefItem>() {
            override fun areItemsTheSame(oldItem: DietPrefItem, newItem: DietPrefItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DietPrefItem, newItem: DietPrefItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}