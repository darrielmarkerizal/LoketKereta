package com.example.loketkereta

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val pesanTiketCardView = findViewById<View>(R.id.pesan_tiket)

        pesanTiketCardView.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }
        val tanggal = intent.getStringExtra("tanggal")
        val stasiunAsal = intent.getStringExtra("stasiunAsal")
        val stasiunTujuan = intent.getStringExtra("stasiunTujuan")
        val paketTambahanList = intent.getStringArrayListExtra("paketTambahan")

        val tanggalTextView = findViewById<TextView>(R.id.tanggal)
        val stasiunAsalTextView = findViewById<TextView>(R.id.stasiunAsal)
        val stasiunTujuanTextView = findViewById<TextView>(R.id.stasiunTujuan)
        val paketTambahanTextView = findViewById<TextView>(R.id.paketTambahan)

        tanggalTextView.text = tanggal
        stasiunAsalTextView.text = stasiunAsal
        stasiunTujuanTextView.text = stasiunTujuan
        val paketTambahanText = paketTambahanList?.joinToString(", ") ?: "Tidak ada paket tambahan"
        paketTambahanTextView.text = paketTambahanText

        val calendarView = findViewById<CalendarView>(R.id.calendar)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"

            if (tanggal == selectedDate) {
                Toast.makeText(this, "Rencana perjalanan tersedia di tanggal ini.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Rencana perjalanan tidak tersedia di tanggal ini.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

