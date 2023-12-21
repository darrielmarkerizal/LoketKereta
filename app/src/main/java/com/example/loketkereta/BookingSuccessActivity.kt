package com.example.loketkereta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loketkereta.databinding.ActivityBookingSuccessBinding
import com.example.loketkereta.kereta.dataKereta
import java.text.SimpleDateFormat
import java.util.Locale

class BookingSuccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingSuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val kereta = intent.getSerializableExtra("kereta") as dataKereta

        binding.jamKeberangkatan.text = kereta.jamBerangkat
        binding.jamTiba.text = kereta.jamTiba
        binding.kotaKeberangkatan.text = kereta.stasiunKeberangkatan
        binding.kotaTiba.text = kereta.stasiunTujuan

        val originalFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val targetFormat = SimpleDateFormat("EEEE, dd MMM yy", Locale("in"))
        val date = originalFormat.parse(kereta.tanggalBerangkat)
        val formattedDate = targetFormat.format(date)
        binding.tanggalKeberangkatan.text = formattedDate
        binding.tanggalKeberangkatan.text = kereta.tanggalBerangkat
        binding.waktuKeberangkatan.text = kereta.jamBerangkat

        val buttonSelesai = binding.btnSelesai
        buttonSelesai.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}