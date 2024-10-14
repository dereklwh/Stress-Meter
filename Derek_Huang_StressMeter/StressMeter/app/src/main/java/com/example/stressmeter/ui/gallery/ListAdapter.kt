package com.example.stressmeter.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stressmeter.R

class ListAdapter(private val data: List<Pair<Long, Int?>>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampValue)
        val scoreTextView: TextView = itemView.findViewById(R.id.stressValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (timestamp, score) = data[position]
        holder.timestampTextView.text = timestamp.toString()
        holder.scoreTextView.text = score?.toString() ?: "N/A"
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

