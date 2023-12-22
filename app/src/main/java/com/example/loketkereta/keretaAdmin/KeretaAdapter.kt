package com.example.loketkereta.keretaAdmin

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.loketkereta.AddJadwal
import com.example.loketkereta.databinding.KeretaAdminItemBinding
import com.google.firebase.firestore.FirebaseFirestore

class KeretaAdapter(private val keretaList: List<Kereta>, private val context: Context) : RecyclerView.Adapter<KeretaAdapter.KeretaViewHolder>() {

    class KeretaViewHolder(val binding: KeretaAdminItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeretaViewHolder {
        val binding = KeretaAdminItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeretaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KeretaViewHolder, position: Int) {
        val kereta = keretaList[position]
        holder.binding.namaKeretaTextView.text = kereta.namaKereta
        holder.binding.ruteTextView.text = "${kereta.stasiunKeberangkatan} - ${kereta.stasiunTujuan}"
        holder.binding.tanggalTextView.text = kereta.tanggalBerangkat

        holder.binding.deleteButton.setOnClickListener {
            deleteKereta(position)
        }

        holder.binding.updateButton.setOnClickListener {
            val intent = Intent(context, AddJadwal::class.java)
            intent.putExtra("kereta", kereta)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = keretaList.size

    private fun deleteKereta(position: Int) {
        val kereta = keretaList[position]
        val db = FirebaseFirestore.getInstance()
        db.collection("dataKereta")
            .document(kereta.id)
            .delete()
            .addOnSuccessListener {
                Log.d("KeretaAdapter", "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w("KeretaAdapter", "Error deleting document", e)
            }
    }
}