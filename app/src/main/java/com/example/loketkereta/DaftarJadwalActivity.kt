package com.example.loketkereta

import KeretaAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.loketkereta.kereta.dataKereta
import com.example.loketkereta.databinding.ActivityDaftarJadwalBinding

class DaftarJadwalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarJadwalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val departureStation = intent.getStringExtra("departureStation")
        val destinationStation = intent.getStringExtra("destinationStation")

        binding.ruteKereta.text = "$departureStation - $destinationStation"

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val keretaList: ArrayList<dataKereta> = arrayListOf()

        keretaList.add(dataKereta(
            "Rp149.000", "Purwokerto", "PWT", "14:00", "Sisa 2", "Joglosemarkerto",
            "Solo Balapan", "SLO", "18:35", "Ekonomi - A", "6 jam 35 menit", "2023-12-25"
        ))

        keretaList.add(dataKereta(
            "Rp74.000", "Purwokerto", "PWT", "06:30", "Sisa 24", "Joglosemarkerto",
            "Solo Balapan", "SLO", "11:35", "Ekonomi - C", "5 jam 5 menit", "2023-12-23"
        ))

        keretaList.add(dataKereta(
            "335.000", "Tanjung Karang", "PWT", "03:00", "", "Bima",
            "Bekri", "SLO", "06:35", "Efsekutif - A", "3 jam 35 menit", "2023-12-22"
        ))

        keretaList.add(dataKereta(
            "Rp149.000", "Ketapang", "PWT", "14:00", "Sisa 2", "Bengawan",
            "Giham", "SLO", "18:35", "Ekonomi - A", "6 jam 35 menit", "2023-12-21"
        ))

        val filteredKeretaList = ArrayList(keretaList.filter {
            it.stasiunKeberangkatan.lowercase() == departureStation?.lowercase() &&
                    it.stasiunTujuan.lowercase() == destinationStation?.lowercase()
        })

        val keretaAdapter = KeretaAdapter(this, filteredKeretaList)
        binding.recyclerViewJadwalKereta.adapter = keretaAdapter
    }
}