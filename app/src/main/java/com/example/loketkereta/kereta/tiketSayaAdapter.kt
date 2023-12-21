package com.example.loketkereta.kereta

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loketkereta.BookingDetailActivity
import com.example.loketkereta.BookingSuccessActivity
import com.example.loketkereta.R
import com.example.loketkereta.databinding.ListJadwalKeretaBinding
import com.example.loketkereta.kereta.dataKereta

class TiketSayaAdapter(private val context: Context, private val arrayList: ArrayList<dataKereta>) : RecyclerView.Adapter<TiketSayaAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ListJadwalKeretaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListJadwalKeretaBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val kereta = arrayList[position]
        holder.binding.hargaTiket.text = kereta.harga
        holder.binding.stasiunKeberangkatan.text = kereta.stasiunKeberangkatan
        holder.binding.jamBerangkat.text = kereta.jamBerangkat
        holder.binding.sisaKursi.text = kereta.sisaTiket
        holder.binding.namaKereta.text = kereta.namaKereta
        holder.binding.stasiunTujuan.text = kereta.stasiunTujuan
        holder.binding.jamTiba.text = kereta.jamTiba
        holder.binding.kelasKereta.text = kereta.kelasKereta
        holder.binding.waktuPerjalanan.text = kereta.durasiPerjalanan

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, BookingSuccessActivity::class.java)
            intent.putExtra("kereta", kereta)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}