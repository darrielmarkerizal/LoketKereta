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

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val keretaList: ArrayList<dataKereta> = arrayListOf()

        keretaList.add(dataKereta(
            "Rp149.000", "PWT", "14:00", "Sisa 2", "Joglosemarkerto",
            "SLO", "18:35", "Ekonomi - A", "6 jam 35 menit"
        ))

        keretaList.add(dataKereta(
            "Rp74.000", "PWT", "06:30", "Sisa 24", "Joglosemarkerto",
            "SLO", "11:35", "Ekonomi - C", "5 jam 5 menit"
        ))

        keretaList.add(dataKereta(
            "335.000", "PWT", "03:00", "", "Bima",
            "SLO", "06:35", "Efsekutif - A", "3 jam 35 menit"
        ))

        keretaList.add(dataKereta(
            "Rp149.000", "PWT", "14:00", "Sisa 2", "Bengawan",
            "SLO", "18:35", "Ekonomi - A", "6 jam 35 menit"
        ))


        val keretaAdapter = KeretaAdapter(this, keretaList)
        binding.recyclerViewJadwalKereta.adapter = keretaAdapter
    }
}