package com.example.loketkereta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.loketkereta.databinding.ActivityBookingDetailBinding
import com.example.loketkereta.databinding.PassengerCardBinding
import com.example.loketkereta.kereta.dataKereta

class BookingDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val kereta = intent.getSerializableExtra("kereta") as dataKereta

        binding.jadwalSelected.hargaTiket.text = kereta.harga
        binding.jadwalSelected.stasiunKeberangkatan.text = kereta.stasiunKeberangkatan
        binding.jadwalSelected.jamBerangkat.text = kereta.jamBerangkat
        binding.jadwalSelected.sisaKursi.text = kereta.sisaTiket
        binding.jadwalSelected.namaKereta.text = kereta.namaKereta
        binding.jadwalSelected.stasiunTujuan.text = kereta.stasiunTujuan
        binding.jadwalSelected.jamTiba.text = kereta.jamTiba
        binding.jadwalSelected.kelasKereta.text = kereta.kelasKereta
        binding.jadwalSelected.waktuPerjalanan.text = kereta.durasiPerjalanan
        binding.jadwalSelected.selectKeretaButton.visibility = View.GONE

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