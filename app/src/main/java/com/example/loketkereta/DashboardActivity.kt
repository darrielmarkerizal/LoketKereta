package com.example.loketkereta

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import com.example.loketkereta.databinding.ActivityDashboardBinding
import android.content.Context

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val fullName = sharedPref.getString("fullName", "")

        binding.namaUser.text = fullName

        binding.pesanTiket.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }
        val tanggal = intent.getStringExtra("tanggal")
        val stasiunAsal = intent.getStringExtra("stasiunAsal")
        val stasiunTujuan = intent.getStringExtra("stasiunTujuan")
        val paketTambahanList = intent.getStringArrayListExtra("paketTambahan")

        binding.tanggal.text = tanggal
        binding.stasiunAsal.text = stasiunAsal
        binding.stasiunTujuan.text = stasiunTujuan
        val paketTambahanText = paketTambahanList?.joinToString(", ") ?: "Tidak ada paket tambahan"
        binding.paketTambahan.text = paketTambahanText

        binding.calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"

            if (tanggal == selectedDate) {
                Toast.makeText(this, "Rencana perjalanan tersedia di tanggal ini.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Rencana perjalanan tidak tersedia di tanggal ini.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}