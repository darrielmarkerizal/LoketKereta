package com.example.loketkereta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.loketkereta.databinding.ActivityBookingDetailBinding
import com.example.loketkereta.databinding.PassengerCardBinding

class BookingDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBatal.setOnClickListener {
            val intent = Intent(this, DaftarJadwalActivity::class.java)
            startActivity(intent)
        }

        binding.passengerCard1.linearLayout4.visibility = View.VISIBLE
        binding.passengerCard1.linearLayout5.visibility = View.VISIBLE
        binding.passengerCard1.linearLayout6.visibility = View.VISIBLE
        binding.passengerCard1.textView3.visibility = View.VISIBLE

        binding.passengerCard1.openCloseButton.setOnClickListener{
            if(binding.passengerCard1.linearLayout4.visibility == View.VISIBLE) {
                binding.passengerCard1.linearLayout4.visibility = View.GONE
                binding.passengerCard1.linearLayout5.visibility = View.GONE
                binding.passengerCard1.linearLayout6.visibility = View.GONE
                binding.passengerCard1.textView3.visibility = View.GONE
            } else {
                binding.passengerCard1.linearLayout4.visibility = View.VISIBLE
                binding.passengerCard1.linearLayout5.visibility = View.VISIBLE
                binding.passengerCard1.linearLayout6.visibility = View.VISIBLE
                binding.passengerCard1.textView3.visibility = View.VISIBLE
            }
        }
    }
}