package com.example.loketkereta

import KeretaAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loketkereta.databinding.FragmentTicketBinding
import com.example.loketkereta.kereta.TiketSayaAdapter
import com.example.loketkereta.kereta.dataKereta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TicketFragment : Fragment() {
    private lateinit var binding: FragmentTicketBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("tickets").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d("Firestore", "DocumentSnapshot data: ${document.data}")
                        val keretaMap = document.get("kereta") as? Map<String, Any>
                        if (keretaMap != null) {
                            val kereta = dataKereta().apply {
                                durasiPerjalanan = keretaMap["durasiPerjalanan"] as? String
                                kelasKereta = keretaMap["kelasKereta"] as? String
                                tanggalBerangkat = keretaMap["tanggalBerangkat"] as? String
                                harga = keretaMap["harga"] as? String
                                stasiunKeberangkatan = keretaMap["stasiunKeberangkatan"] as? String
                                namaKereta = keretaMap["namaKereta"] as? String
                                jamTiba = keretaMap["jamTiba"] as? String
                                stasiunTujuan = keretaMap["stasiunTujuan"] as? String
                                sisaTiket = keretaMap["sisaTiket"] as? String
                                jamBerangkat = keretaMap["jamBerangkat"] as? String
                            }
                            Log.d("Firestore", "Received data: $kereta")
                            val keretaList = ArrayList<dataKereta>()
                            keretaList.add(kereta)
                            val tiketSayaAdapter = TiketSayaAdapter(requireContext(), keretaList)
                            binding.ticketList.layoutManager = LinearLayoutManager(requireContext())
                            binding.ticketList.adapter = tiketSayaAdapter
                        } else {
                            Log.d("Firestore", "Data 'kereta' is null or not a Map")
                        }
                    } else {
                        Log.d("Firestore", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Firestore", "get failed with ", exception)
                }
        }
    }
}
