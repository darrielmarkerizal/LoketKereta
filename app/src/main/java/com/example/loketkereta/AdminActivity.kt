package com.example.loketkereta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.loketkereta.databinding.ActivityAdminBinding
import com.example.loketkereta.keretaAdmin.AppDatabase
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

                    val roomDb = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java, "kereta-db"
                    ).build()
                    keretaList.forEach { kereta ->
                        Thread {
                            roomDb.keretaDao().upsert(kereta)
                        }.start()
                    }
                    checkDataInRoom()
                } else {
                    Log.d("AdminActivity", "Current data: null")
                }
            }
    }

    private fun checkDataInRoom() {
        val roomDb = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "kereta-db"
        ).build()

        Thread {
            val keretaList = roomDb.keretaDao().getAll()
            if (keretaList.isNotEmpty()) {
                Log.d("AdminActivity", "Data successfully saved in Room")
            } else {
                Log.d("AdminActivity", "No data found in Room")
            }
        }.start()
    }
}