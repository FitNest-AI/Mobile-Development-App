package com.example.fitnestapp.ui.biodata

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnestapp.data.remote.response.GoalItem
import com.example.fitnestapp.databinding.ItemListBinding

class GoalAdapter(val context: Context, var itemChange: ItemChange) : ListAdapter<GoalItem, GoalAdapter.MyViewHolder>(DIFF_CALLBACK) {

    var checkedGoals: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, context, itemChange)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val goalItem = getItem(position)
        holder.bind(goalItem, checkedGoals)
    }

    class MyViewHolder(val binding: ItemListBinding, val context: Context,var itemChange: ItemChange) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(goalItem: GoalItem, checkedGoals: ArrayList<String>) {
            binding.checkBox.text = goalItem.name

            // Check if the goalItem is in the checkedGoals list
            binding.checkBox.isChecked = checkedGoals.contains(goalItem.id)

            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Add the goalItem to the checkedGoals list when the checkbox is checked
                    checkedGoals.add(goalItem.id)
                } else {
                    // Remove the goalItem from the checkedGoals list when the checkbox is unchecked
                    checkedGoals.remove(goalItem.id)
                }
                itemChange.goalsItemChange(checkedGoals)
            }

            // Your other binding logic...
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GoalItem>() {
            override fun areItemsTheSame(oldItem: GoalItem, newItem: GoalItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: GoalItem, newItem: GoalItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}