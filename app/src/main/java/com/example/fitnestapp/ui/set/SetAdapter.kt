package com.example.fitnestapp.ui.set

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnestapp.R
import com.example.fitnestapp.data.model.Exercise
import com.example.fitnestapp.data.model.Workout
import com.example.fitnestapp.ui.home.HomeAdapter


class SetAdapter(var listSet: ArrayList<Exercise>): RecyclerView.Adapter<SetAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameSet)
        val time: TextView = itemView.findViewById(R.id.repSet)
        val photo: ImageView = itemView.findViewById(R.id.photoSet)
        val desc: TextView = itemView.findViewById(R.id.descSet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.exercise_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listSet.size

    fun filterList(filterlist: ArrayList<Exercise>) {
        listSet = filterlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, time, photo, desc) = listSet[position]
        holder.name.text = name
        holder.time.text = time
        holder.photo.setImageResource(photo)
        holder.desc.text = desc

//        holder.btnPlay.setOnClickListener {
//            val intentDetail = Intent(holder.itemView.context, SetActivity::class.java)
//            holder.itemView.context.startActivity(intentDetail)
////            intentDetail.putExtra("key_drakor", listDrakor[holder.adapterPosition])
//        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Workout)
    }
}