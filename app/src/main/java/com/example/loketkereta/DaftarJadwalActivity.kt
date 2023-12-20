package com.example.loketkereta

import KeretaAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.loketkereta.kereta.dataKereta
import com.example.loketkereta.databinding.ActivityDaftarJadwalBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DaftarJadwalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarJadwalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val departureStation = intent.getStringExtra("departureStation")
        val destinationStation = intent.getStringExtra("destinationStation")
        val departureDate = intent.getStringExtra("departureDate")

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        val date = sdf.parse(departureDate)

        val sdfDay = SimpleDateFormat("dd", Locale("id", "ID"))
        val sdfDayOfWeek = SimpleDateFormat("EEE", Locale("id", "ID"))

        binding.tanggalSelected.text = sdfDay.format(date)
        binding.hariSelected.text = sdfDayOfWeek.format(date).uppercase(Locale.ROOT)

        val calendar = Calendar.getInstance()
        calendar.time = date

        val tanggalViews = listOf(binding.tanggal1, binding.tanggal2, binding.tanggal4, binding.tanggal5)
        val hariViews = listOf(binding.hari1, binding.hari2, binding.hari4, binding.hari5)
        for (i in tanggalViews.indices) {
            calendar.add(Calendar.DATE, if (i == 0) -2 else if (i == 1) -1 else if (i == 2) 1 else 2)
            tanggalViews[i].text = sdfDay.format(calendar.time)
            hariViews[i].text = sdfDayOfWeek.format(calendar.time).uppercase(Locale.ROOT)
            calendar.time = sdf.parse(departureDate)
        }
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
                    it.stasiunTujuan.lowercase() == destinationStation?.lowercase() &&
                    it.tanggalBerangkat == departureDate
        })

        val keretaAdapter = KeretaAdapter(this, filteredKeretaList)
        binding.recyclerViewJadwalKereta.adapter = keretaAdapter
    }
}