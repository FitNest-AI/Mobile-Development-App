package com.example.fitnestapp.ui.biodata

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnestapp.data.remote.response.LevelItem
import com.example.fitnestapp.databinding.ItemLevelBinding

class LevelAdapter(val context: Context, var itemChange: ItemChange) : ListAdapter<LevelItem, LevelAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION
    var checkedLevel: String = String()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, context, itemChange)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review, position)
    }

    inner class MyViewHolder(val binding: ItemLevelBinding, val context: Context,  var itemChange: ItemChange) : RecyclerView.ViewHolder(binding.root) {

        fun bind(levelItem: LevelItem, position: Int) {
            binding.radioBtn.text = levelItem.name

            // Set the checked state based on whether the current item is selected
            binding.radioBtn.isChecked = position == selectedItemPosition

            binding.radioBtn.setOnClickListener {
                // Update the selected item position
                selectedItemPosition = adapterPosition
                checkedLevel = levelItem.id
                // Notify data set changed to update the UI
                notifyDataSetChanged()

                itemChange.levelItemChange(checkedLevel)
                // You can also perform additional actions when a radio button is clicked
                // For example, you can trigger an event or update the selected item in your data
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LevelItem>() {
            override fun areItemsTheSame(oldItem: LevelItem, newItem: LevelItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: LevelItem, newItem: LevelItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}