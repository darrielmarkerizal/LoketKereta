package com.example.loketkereta.kereta

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loketkereta.R
import com.example.loketkereta.databinding.JadwalKeretaSayaBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class KeretaSayaAdapter(private val keretaList: List<dataKereta>) : RecyclerView.Adapter<KeretaSayaViewHolder>() {
    private lateinit var binding: JadwalKeretaSayaBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeretaSayaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.jadwal_kereta_saya, parent, false)
        return KeretaSayaViewHolder(view)
    }

    override fun onBindViewHolder(holder: KeretaSayaViewHolder, position: Int) {
        val kereta = keretaList[position]
        holder.labelNamaKereta.text = kereta.namaKereta
        holder.labelRute.text = "${kereta.stasiunKeberangkatan} - ${kereta.stasiunTujuan}"

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        val departureDate = sdf.parse(kereta.tanggalBerangkat)
        val currentDate = Date()
        val diff = departureDate.time - currentDate.time
        val diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)

        if (diffDays == 1L) {
            holder.labelWaktu.text = "BESOK"
        } else if (diffDays > 1) {
            holder.labelWaktu.text = "$diffDays hari"
        }
    }

    override fun getItemCount(): Int {
        return keretaList.size
    }
}