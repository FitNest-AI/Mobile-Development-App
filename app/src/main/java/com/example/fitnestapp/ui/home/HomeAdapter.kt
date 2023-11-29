package com.example.fitnestapp.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnestapp.R
import com.example.fitnestapp.model.Workout

class HomeAdapter(var listWorkout: ArrayList<Workout>): RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_set_name)
        val time: TextView = itemView.findViewById(R.id.tv_set_time)
        val image: ImageView = itemView.findViewById(R.id.img_set)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.set_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listWorkout.size

    fun filterList(filterlist: ArrayList<Workout>) {
        listWorkout = filterlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, time, image) = listWorkout[position]
        holder.name.text = name
        holder.time.text = time
        holder.image.setImageResource(image)

//        holder.itemView.setOnClickListener {
//            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
//            intentDetail.putExtra("key_drakor", listDrakor[holder.adapterPosition])
//            holder.itemView.context.startActivity(intentDetail)
//        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Workout)
    }
}