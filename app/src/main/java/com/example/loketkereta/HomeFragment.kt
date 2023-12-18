package com.example.loketkereta

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loketkereta.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        fetchUserName()
        fetchLatestBooking()

        return view
    }

    private fun fetchUserName() {
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
                    Log.w("HomeFragment", "Failed to read value.", error.toException())
                }
            })
        }
    }

    private fun fetchLatestBooking() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("users").document(userId).collection("bookings")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (!documents.isNullOrEmpty()) {
                        updateUIWithLatestBooking(documents[0])
                    }
                } else {
                    Log.w("HomeFragment", "Error getting documents.", task.exception)
                }
            }
    }

    private fun updateUIWithLatestBooking(document: DocumentSnapshot) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.beginTransaction().apply {
            replace(R.id.book_fragment_container, FragmentBook())
            commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}