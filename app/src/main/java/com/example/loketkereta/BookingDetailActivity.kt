package com.example.loketkereta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import com.example.loketkereta.databinding.ActivityBookingDetailBinding
import com.example.loketkereta.databinding.PassengerCardBinding
import com.example.loketkereta.kereta.dataKereta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookingDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner = binding.passengerCard1.spinnerIdentitas
        val adapter = ArrayAdapter.createFromResource(this, R.array.identitas, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)

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

        binding.btnLanjut.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()
            val userId = auth.currentUser?.uid

            if (userId != null) {
                val namaPenumpang = binding.passengerCard1.editTextNamaPenumpang.text.toString()
                val nomorIdentitas = binding.passengerCard1.editTextNomorIdentitas.text.toString()
                val jenisIdentitas = binding.passengerCard1.spinnerIdentitas.selectedItem.toString()

                val ticketData = hashMapOf(
                    "userId" to userId,
                    "kereta" to kereta,
                    "namaPenumpang" to namaPenumpang,
                    "nomorIdentitas" to nomorIdentitas,
                    "jenisIdentitas" to jenisIdentitas
                )

                db.collection("tickets").document(userId)
                    .set(ticketData)
                    .addOnSuccessListener {
                        Log.d("Firestore", "DocumentSnapshot successfully written!")
                        val intent = Intent(this, BookingSuccessActivity::class.java)
                        intent.putExtra("kereta", kereta)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error writing document", e)
                    }
            } else {
                Log.w("Firestore", "User is not logged in")
            }
        }
    }
}