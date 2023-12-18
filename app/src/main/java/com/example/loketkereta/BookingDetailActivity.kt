package com.example.loketkereta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.loketkereta.databinding.ActivityBookingDetailBinding
import com.example.loketkereta.databinding.PassengerCardBinding

class BookingDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.linkTextTambahPenumpang.setOnClickListener {
            val passengerCardBinding = PassengerCardBinding.inflate(LayoutInflater.from(this))

            binding.parentLayout.addView(passengerCardBinding.root)
        }


    }
}