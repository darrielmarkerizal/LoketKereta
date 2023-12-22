package com.example.loketkereta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loketkereta.databinding.ActivityAdminBinding
import com.example.loketkereta.keretaAdmin.Kereta
import com.example.loketkereta.keretaAdmin.KeretaAdapter
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var adapter: KeretaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddJadwal::class.java))
        }

        setupRecyclerView()
        fetchKeretaData()
    }

    private fun setupRecyclerView() {
        adapter = KeretaAdapter(mutableListOf(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun fetchKeretaData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("dataKereta")
            .orderBy("tanggalBerangkat")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("AdminActivity", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.documents.isNotEmpty()) {
                    val keretaList = snapshot.documents.mapNotNull { document ->
                        document.toObject(Kereta::class.java)?.copy(id = document.id)
                    }.toMutableList()
                    adapter = KeretaAdapter(keretaList, this)
                    binding.recyclerView.adapter = adapter
                } else {
                    Log.d("AdminActivity", "Current data: null")
                }
            }
    }
}