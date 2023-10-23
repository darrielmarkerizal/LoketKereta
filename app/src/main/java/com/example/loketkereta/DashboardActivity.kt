package com.example.loketkereta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val pesanTiketCardView = findViewById<View>(R.id.pesan_tiket)

        pesanTiketCardView.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }
    }
}

