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
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.protobuf.Value
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val userId = auth.currentUser?.uid
        if (userId != null) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("users").child(userId).child("name")

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userName = dataSnapshot.getValue(String::class.java)
                    binding.namaUser.text = userName
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("DashboardActivity", "Failed to read value.", error.toException())
                }
            })
        }

        binding.pesanTiket.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            startActivity(intent)
        }

        val tanggal = intent.getStringExtra("tanggal")

        val db = FirebaseFirestore.getInstance()
        val userId2 = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("users").document(userId2).collection("bookings")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (!documents.isNullOrEmpty()) {
                        val document = documents[0]
                        val tanggal = document.getString("tanggal")
                        val stasiunAsal = document.getString("stasiunBerangkat")
                        val stasiunTujuan = document.getString("stasiunTiba")
                        val paketTambahanList = document.get("paketTambahan") as List<String>?

                        binding.tanggal.text = tanggal
                        binding.stasiunAsal.text = stasiunAsal
                        binding.stasiunTujuan.text = stasiunTujuan

                        if (paketTambahanList.isNullOrEmpty()) {
                            binding.paketTambahan.text = "Tidak ada paket tambahan"
                        } else {
                            val paketTambahanText = paketTambahanList.joinToString(", ")
                            binding.paketTambahan.text = paketTambahanText
                        }
                    }
                } else {
                    Log.w("DashboardActivity", "Error getting documents.", task.exception)
                }
            }

        binding.calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"

            db.collection("users").document(userId2).collection("bookings")
                .whereEqualTo("tanggal", selectedDate)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val documents = task.result?.documents
                        if (!documents.isNullOrEmpty()) {
                            Toast.makeText(this, "Rencana perjalanan tersedia di tanggal ini.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Rencana perjalanan tidak tersedia di tanggal ini.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.w("DashboardActivity", "Error getting documents.", task.exception)
                    }
                }
        }
    }
}