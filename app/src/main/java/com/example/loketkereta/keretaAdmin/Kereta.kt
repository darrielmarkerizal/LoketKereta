package com.example.loketkereta.keretaAdmin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Kereta(
    val id: String = "",
    val namaKereta: String = "",
    val stasiunKeberangkatan: String = "",
    val stasiunTujuan: String = "",
    val tanggalBerangkat: String = "",
    val jamBerangkat: String = "",
    val jamTiba: String = "",
    val harga: String = "",
    val kelasKereta: String = "",
    val durasiPerjalanan: String = ""
) : Parcelable