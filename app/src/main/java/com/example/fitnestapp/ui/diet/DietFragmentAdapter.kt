package com.example.fitnestapp.ui.diet

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnestapp.R
import com.example.fitnestapp.data.model.Diet
import com.example.fitnestapp.data.model.Workout
import com.example.fitnestapp.ui.home.HomeAdapter
import com.example.fitnestapp.ui.set.SetActivity

class DietFragmentAdapter(var listDiet: ArrayList<Diet>): RecyclerView.Adapter<DietFragmentAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.nameDiet)
        val calorie: TextView = itemView.findViewById(R.id.caloriDiet)
        val fat: TextView = itemView.findViewById(R.id.fatDiet)
        val carbohydrate: TextView = itemView.findViewById(R.id.carboDiet)
        val protein: TextView = itemView.findViewById(R.id.proteinDiet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.diet_item, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listDiet.size

    fun filterList(filterlist: ArrayList<Diet>) {
        listDiet = filterlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (name, calorie, fat, carbohydrate, protein) = listDiet[position]
        holder.name.text = name
        holder.calorie.text = calorie
        holder.fat.text = fat
        holder.carbohydrate.text = carbohydrate
        holder.protein.text = protein

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