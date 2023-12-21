package com.example.fitnestapp.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnestapp.R
import com.example.fitnestapp.data.model.Workout
import com.example.fitnestapp.data.remote.response.ResponseWorkout
import com.example.fitnestapp.data.remote.response.WorkoutItem
import com.example.fitnestapp.ui.set.SetActivity

class HomeAdapter(var listWorkout: List<WorkoutItem>): RecyclerView.Adapter<HomeAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_set_name)
        val time: TextView = itemView.findViewById(R.id.tv_set_time)
        val image: ImageView = itemView.findViewById(R.id.img_set)
        val btnPlay : Button = itemView.findViewById(R.id.btn_set_start)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.set_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listWorkout.size

    fun filterList(filterlist: List<WorkoutItem>) {
        listWorkout = filterlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       val _listWorkout = listWorkout[position]
        holder.name.text = _listWorkout?.name
        holder.time.text = _listWorkout?.time
//        holder.image.setImageResource(image)

        holder.btnPlay.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, SetActivity::class.java)
            intentDetail.putExtra("data", _listWorkout)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Workout)
    }
}