package com.example.loketkereta.kereta

data class dataKereta(
    val harga: String,
    val stasiunKeberangkatan: String,
    val stasiunKeberangkatanCode: String,
    val jamBerangkat: String,
    val sisa: String,
    val namaKereta: String,
    val stasiunTujuan: String,
    val stasiunTujuanCode: String,
    val jamTiba: String,
    val kelasKereta: String,
    val durasiPerjalanan: String,
    val tanggalBerangkat: String
)
