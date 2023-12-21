package com.example.loketkereta.kereta

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.loketkereta.R

class KeretaSayaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val labelWaktu: TextView = view.findViewById(R.id.labelWaktu)
    val labelNamaKereta: TextView = view.findViewById(R.id.labelNamaKereta)
    val labelRute: TextView = view.findViewById(R.id.labelRute)
}