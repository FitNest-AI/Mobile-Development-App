package com.example.fitnestapp.ui.biodata

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnestapp.data.remote.response.GoalItem
import com.example.fitnestapp.data.remote.response.TargetMuscleItem
import com.example.fitnestapp.databinding.ItemListBinding

class TargetMuscleAdapter (val context: Context, var itemChange: ItemChange): ListAdapter<TargetMuscleItem, TargetMuscleAdapter.MyViewHolder>(DIFF_CALLBACK) {
    var checkedTarget: ArrayList<String> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding,context,itemChange)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        holder.bind(review, checkedTarget)
    }
    class MyViewHolder(val binding: ItemListBinding, val context: Context, var itemChange: ItemChange) : RecyclerView.ViewHolder(binding.root) {
        fun bind(goalItem: TargetMuscleItem,  checkedTarget: ArrayList<String>){
            binding.checkBox.text = goalItem.name

            binding.checkBox.isChecked = checkedTarget.contains(goalItem.id)

            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Add the goalItem to the checkedGoals list when the checkbox is checked
                    checkedTarget.add(goalItem.id)
                } else {
                    // Remove the goalItem from the checkedGoals list when the checkbox is unchecked
                    checkedTarget.remove(goalItem.id)
                }
                itemChange.targetMuscleItemChange(checkedTarget)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TargetMuscleItem>() {
            override fun areItemsTheSame(oldItem: TargetMuscleItem, newItem: TargetMuscleItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: TargetMuscleItem, newItem: TargetMuscleItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}