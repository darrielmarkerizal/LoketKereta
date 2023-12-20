package com.example.loketkereta

import KeretaAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.loketkereta.kereta.dataKereta
import com.example.loketkereta.databinding.ActivityDaftarJadwalBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.google.firebase.firestore.FirebaseFirestore

class DaftarJadwalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarJadwalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()

        val departureStation = intent.getStringExtra("departureStation")
        val destinationStation = intent.getStringExtra("destinationStation")
        val departureDate = intent.getStringExtra("departureDate")

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        val date = sdf.parse(departureDate)

        val sdfDay = SimpleDateFormat("dd", Locale("id", "ID"))
        val sdfDayOfWeek = SimpleDateFormat("EEE", Locale("id", "ID"))

        val sdfMonth = SimpleDateFormat("MM", Locale("id", "ID"))
        val year = Calendar.getInstance().get(Calendar.YEAR)

        binding.tanggalSelected.text = sdfDay.format(date)
        binding.hariSelected.text = sdfDayOfWeek.format(date).uppercase(Locale.ROOT)

        val calendar = Calendar.getInstance()
        calendar.time = date

        val tanggalViews = listOf(binding.tanggal1, binding.tanggal2, binding.tanggal4, binding.tanggal5)
        val hariViews = listOf(binding.hari1, binding.hari2, binding.hari4, binding.hari5)
        for (i in tanggalViews.indices) {
            calendar.add(Calendar.DATE, if (i == 0) -2 else if (i == 1) -1 else if (i == 2) 1 else 2)
            tanggalViews[i].text = sdfDay.format(calendar.time)
            hariViews[i].text = sdfDayOfWeek.format(calendar.time).uppercase(Locale.ROOT)
            calendar.time = date
        }

//        ToDo : Logic update tanggal masih salah
        val linearTanggalViews = listOf(binding.linearTanggal1, binding.linearTanggal2, binding.linearTanggalSelected, binding.linearTanggal4, binding.linearTanggal5)

        for (i in linearTanggalViews.indices) {
            linearTanggalViews[i].setOnClickListener {
                val selectedTanggal = tanggalViews[i].text.toString()
                val selectedHari = hariViews[i].text.toString()

                binding.tanggalSelected.text = selectedTanggal
                binding.hariSelected.text = selectedHari

                val calendar = Calendar.getInstance()
                calendar.time = sdf.parse("$selectedTanggal-${sdfMonth.format(date)}-$year")

                for (j in tanggalViews.indices) {
                    val diff = j - i
                    calendar.add(Calendar.DATE, diff)
                    if (j != i) { // Avoid accessing the selected date
                        tanggalViews[j].text = sdfDay.format(calendar.time)
                        hariViews[j].text = sdfDayOfWeek.format(calendar.time).uppercase(Locale.ROOT)
                    }
                    calendar.time = sdf.parse("$selectedTanggal-${sdfMonth.format(date)}-$year")
                }
            }
        }
        binding.ruteKereta.text = "$departureStation - $destinationStation"

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val keretaList: ArrayList<dataKereta> = arrayListOf()

//        keretaList.add(dataKereta(
//            "Rp130.000", "Malang Kota Lama", "MLK", "08:00", "", "Argo Muria",
//            "Semarang Tawang", "SMT", "11:30", "Ekonomi - B", "3 jam 30 menit", "2023-12-24"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp150.000", "Gambir", "GMR", "14:30", "Sisa 10", "Bima",
//            "Surabaya Pasar Turi", "SBI", "20:05", "Eksekutif - A", "5 jam 35 menit", "2023-12-24"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp115.000", "Jatinegara", "JNG", "18:45", "", "Lodaya",
//            "Solo Balapan", "SLO", "22:20", "Ekonomi - C", "3 jam 35 menit", "2023-12-24"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp170.000", "Wates", "WT", "05:15", "Sisa 8", "Argo Bromo Anggrek",
//            "Surabaya Gubeng", "SGU", "10:50", "Eksekutif - B", "5 jam 35 menit", "2023-12-25"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp95.000", "Purwosari", "PWS", "12:30", "", "Taksaka",
//            "Yogyakarta", "YK", "18:05", "Ekonomi - C", "5 jam 35 menit", "2023-12-25"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp120.000", "Surabaya Gubeng", "SGU", "15:45", "Sisa 6", "Bengawan",
//            "Giham", "LPN", "20:20", "Ekonomi - A", "4 jam 35 menit", "2023-12-25"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp200.000", "Malang", "ML", "09:20", "", "Gajayana",
//            "Yogyakarta", "YK", "14:55", "Eksekutif - B", "5 jam 35 menit", "2023-12-26"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp110.000", "Pasarsenen", "PSE", "17:00", "Sisa 5", "Argo Parahyangan",
//            "Bandung", "BD", "20:30", "Ekonomi - C", "3 jam 30 menit", "2023-12-26"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp160.000", "Juanda", "JUA", "11:15", "", "Turangga",
//            "Surabaya Gubeng", "SGU", "16:50", "Eksekutif - A", "5 jam 35 menit", "2023-12-26"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp180.000", "Yogyakarta", "YK", "08:10", "Sisa 8", "Bima",
//            "Malang", "ML", "13:45", "Eksekutif - B", "5 jam 35 menit", "2023-12-27"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp115.000", "Lempuyangan", "LPN", "16:30", "", "Argo Muria",
//            "Semarang Tawang", "SMT", "20:00", "Ekonomi - B", "3 jam 30 menit", "2023-12-27"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp140.000", "Purwosari", "PWS", "10:45", "Sisa 12", "Gajayana",
//            "Malang", "ML", "16:20", "Ekonomi - A", "5 jam 35 menit", "2023-12-27"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp100.000", "Solo Balapan", "SLO", "13:30", "Sisa 20", "Taksaka",
//            "Yogyakarta", "YK", "19:05", "Ekonomi - C", "5 jam 35 menit", "2023-12-28"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp175.000", "Wonokromo", "WO", "05:00", "", "Malioboro",
//            "Yogyakarta", "YK", "10:35", "Eksekutif - A", "5 jam 35 menit", "2023-12-28"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp130.000", "Surabaya Gubeng", "SGU", "14:15", "Sisa 6", "Argo Lawu",
//            "Solo Balapan", "SLO", "19:50", "Ekonomi - B", "5 jam 35 menit", "2023-12-28"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp195.000", "Surabaya Pasar Turi", "SBI", "08:40", "", "Gajayana",
//            "Yogyakarta", "YK", "14:15", "Eksekutif - B", "5 jam 35 menit", "2023-12-29"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp105.000", "Lawang", "LW", "16:00", "Sisa 18", "Lodaya",
//            "Solo Balapan", "SLO", "20:35", "Ekonomi - C", "4 jam 35 menit", "2023-12-29"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp150.000", "Malang", "ML", "12:20", "", "Argo Bromo Anggrek",
//            "Surabaya Gubeng", "SGU", "17:55", "Ekonomi - A", "5 jam 35 menit", "2023-12-29"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp160.000", "Malang Kota Lama", "MLK", "09:15", "Sisa 14", "Malioboro",
//            "Yogyakarta", "YK", "14:50", "Ekonomi - B", "5 jam 35 menit", "2023-12-30"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp135.000", "Kepanjen", "KPN", "17:30", "", "Turangga",
//            "Surabaya Gubeng", "SGU", "22:05", "Ekonomi - C", "4 jam 35 menit", "2023-12-30"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp110.000", "Sumberpucung", "SBP", "13:45", "Sisa 16", "Argo Parahyangan",
//            "Bandung", "BD", "17:15", "Ekonomi - A", "3 jam 30 menit", "2023-12-30"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp150.000", "Gambir", "GMR", "12:15", "", "Lodaya",
//            "Solo Balapan", "SLO", "16:50", "Eksekutif - B", "4 jam 35 menit", "2023-12-31"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp120.000", "Juanda", "JUA", "20:30", "Sisa 9", "Gajayana",
//            "Yogyakarta", "YK", "02:05", "Ekonomi - C", "5 jam 35 menit", "2023-12-31"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp205.000", "Pasarsenen", "PSE", "18:45", "", "Bima",
//            "Surabaya Pasar Turi", "SBI", "23:20", "Eksekutif - A", "4 jam 35 menit", "2023-12-31"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp135.000", "Jatinegara", "JNG", "12:00", "Sisa 11", "Argo Parahyangan",
//            "Bandung", "BD", "15:30", "Ekonomi - C", "3 jam 30 menit", "2024-01-01"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp185.000", "Wates", "WT", "07:15", "", "Turangga",
//            "Surabaya Gubeng", "SGU", "12:50", "Eksekutif - B", "5 jam 35 menit", "2024-01-01"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp195.000", "Yogyakarta", "YK", "15:40", "Sisa 7", "Bima",
//            "Malang", "ML", "21:15", "Eksekutif - A", "5 jam 35 menit", "2024-01-01"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp105.000", "Lempuyangan", "LPN", "23:45", "", "Argo Muria",
//            "Semarang Tawang", "SMT", "03:15", "Ekonomi - C", "3 jam 30 menit", "2024-01-02"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp140.000", "Purwosari", "PWS", "19:00", "Sisa 14", "Gajayana",
//            "Malang", "ML", "00:35", "Ekonomi - A", "5 jam 35 menit", "2024-01-02"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp95.000", "Solo Balapan", "SLO", "21:45", "", "Taksaka",
//            "Yogyakarta", "YK", "03:20", "Ekonomi - B", "5 jam 35 menit", "2024-01-02"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp150.000", "Wonokromo", "WO", "06:30", "Sisa 20", "Argo Bromo Anggrek",
//            "Surabaya Gubeng", "SGU", "12:05", "Eksekutif - A", "5 jam 35 menit", "2024-01-03"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp110.000", "Purwosari", "PWS", "13:45", "", "Taksaka",
//            "Yogyakarta", "YK", "19:20", "Ekonomi - C", "5 jam 35 menit", "2024-01-03"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp130.000", "Surabaya Gubeng", "SGU", "16:00", "Sisa 6", "Argo Lawu",
//            "Solo Balapan", "SLO", "21:35", "Ekonomi - B", "5 jam 35 menit", "2024-01-03"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp180.000", "Surabaya Pasar Turi", "SBI", "10:25", "", "Gajayana",
//            "Yogyakarta", "YK", "15:00", "Eksekutif - B", "4 jam 35 menit", "2024-01-04"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp100.000", "Lawang", "LW", "17:45", "Sisa 18", "Lodaya",
//            "Solo Balapan", "SLO", "22:20", "Ekonomi - C", "4 jam 35 menit", "2024-01-04"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp145.000", "Malang", "ML", "14:05", "", "Argo Bromo Anggrek",
//            "Surabaya Gubeng", "SGU", "19:40", "Ekonomi - A", "5 jam 35 menit", "2024-01-04"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp160.000", "Malang Kota Lama", "MLK", "10:45", "Sisa 14", "Malioboro",
//            "Yogyakarta", "YK", "16:20", "Ekonomi - B", "5 jam 35 menit", "2024-01-05"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp135.000", "Kepanjen", "KPN", "19:00", "", "Turangga",
//            "Surabaya Gubeng", "SGU", "23:35", "Ekonomi - C", "4 jam 35 menit", "2024-01-05"
//        ))


//        keretaList.add(dataKereta(
//            "Rp149.000", "Purwokerto", "PWT", "14:00", "Sisa 2", "Joglosemarkerto",
//            "Solo Balapan", "SLO", "18:35", "Ekonomi - A", "6 jam 35 menit", "2023-12-23"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp74.000", "Purwokerto", "PWT", "06:30", "Sisa 24", "Joglosemarkerto",
//            "Solo Balapan", "SLO", "11:35", "Ekonomi - C", "5 jam 5 menit", "2023-12-24"
//        ))
//
//        keretaList.add(dataKereta(
//            "335.000", "Tanjung Karang", "TJN", "03:00", "", "Bima",
//            "Bekri", "SBP", "06:35", "Eksekutif - A", "3 jam 35 menit", "2023-12-25"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp149.000", "Ketapang", "KTP", "14:00", "Sisa 2", "Bengawan",
//            "Giham", "LPN", "18:35", "Ekonomi - A", "6 jam 35 menit", "2023-12-26"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp120.000", "Gambir", "GMR", "07:15", "Sisa 10", "Joglosemarkerto",
//            "Yogyakarta", "YK", "12:50", "Ekonomi - B", "5 jam 35 menit", "2023-12-27"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp90.000", "Juanda", "JUA", "10:30", "Sisa 15", "Bima",
//            "Surabaya Gubeng", "SGU", "15:45", "Ekonomi - C", "5 jam 15 menit", "2023-12-28"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp200.000", "Pasarsenen", "PSE", "08:45", "", "Gajayana",
//            "Yogyakarta", "YK", "14:20", "Eksekutif - B", "5 jam 35 menit", "2023-12-29"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp110.000", "Jatinegara", "JNG", "16:00", "Sisa 5", "Argo Parahyangan",
//            "Bandung", "BD", "19:30", "Ekonomi - C", "3 jam 30 menit", "2023-12-30"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp160.000", "Wates", "WT", "12:20", "", "Turangga",
//            "Surabaya Gubeng", "SGU", "17:55", "Eksekutif - A", "5 jam 35 menit", "2023-12-31"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp180.000", "Yogyakarta", "YK", "09:10", "Sisa 8", "Bima",
//            "Malang", "ML", "14:45", "Eksekutif - B", "5 jam 35 menit", "2024-01-01"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp120.000", "Lempuyangan", "LPN", "17:30", "", "Argo Muria",
//            "Semarang Tawang", "SMT", "21:00", "Ekonomi - B", "3 jam 30 menit", "2024-01-02"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp135.000", "Purwosari", "PWS", "11:45", "Sisa 12", "Gajayana",
//            "Malang", "ML", "17:20", "Ekonomi - A", "5 jam 35 menit", "2024-01-03"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp95.000", "Solo Balapan", "SLO", "14:30", "Sisa 20", "Taksaka",
//            "Yogyakarta", "YK", "20:05", "Ekonomi - C", "5 jam 35 menit", "2024-01-04"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp170.000", "Wonokromo", "WO", "06:00", "", "Malioboro",
//            "Yogyakarta", "YK", "11:35", "Eksekutif - A", "5 jam 35 menit", "2024-01-05"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp125.000", "Surabaya Gubeng", "SGU", "15:15", "Sisa 6", "Argo Lawu",
//            "Solo Balapan", "SLO", "20:50", "Ekonomi - B", "5 jam 35 menit", "2024-01-06"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp190.000", "Surabaya Pasar Turi", "SBI", "09:40", "", "Gajayana",
//            "Yogyakarta", "YK", "15:15", "Eksekutif - B", "5 jam 35 menit", "2024-01-07"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp100.000", "Lawang", "LW", "17:00", "Sisa 18", "Lodaya",
//            "Solo Balapan", "SLO", "21:35", "Ekonomi - C", "4 jam 35 menit", "2024-01-08"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp145.000", "Malang", "ML", "13:20", "", "Argo Bromo Anggrek",
//            "Surabaya Gubeng", "SGU", "18:55", "Ekonomi - A", "5 jam 35 menit", "2024-01-09"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp155.000", "Malang Kota Lama", "MLK", "10:15", "Sisa 14", "Malioboro",
//            "Yogyakarta", "YK", "15:50", "Ekonomi - B", "5 jam 35 menit", "2024-01-10"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp130.000", "Kepanjen", "KPN", "18:30", "", "Turangga",
//            "Surabaya Gubeng", "SGU", "23:05", "Ekonomi - C", "4 jam 35 menit", "2024-01-11"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp110.000", "Sumberpucung", "SBP", "14:45", "Sisa 16", "Argo Parahyangan",
//            "Bandung", "BD", "18:15", "Ekonomi - A", "3 jam 30 menit", "2024-01-12"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp140.000", "Gambir", "GMR", "12:00", "", "Lodaya",
//            "Solo Balapan", "SLO", "16:35", "Eksekutif - B", "4 jam 35 menit", "2024-01-13"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp120.000", "Juanda", "JUA", "20:15", "Sisa 9", "Gajayana",
//            "Yogyakarta", "YK", "01:50", "Ekonomi - C", "5 jam 35 menit", "2024-01-14"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp210.000", "Pasarsenen", "PSE", "18:30", "", "Bima",
//            "Surabaya Pasar Turi", "SBI", "23:05", "Eksekutif - A", "4 jam 35 menit", "2024-01-15"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp130.000", "Jatinegara", "JNG", "11:45", "Sisa 11", "Argo Parahyangan",
//            "Bandung", "BD", "15:15", "Ekonomi - C", "3 jam 30 menit", "2024-01-16"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp180.000", "Wates", "WT", "07:00", "", "Turangga",
//            "Surabaya Gubeng", "SGU", "12:35", "Eksekutif - B", "5 jam 35 menit", "2024-01-17"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp190.000", "Yogyakarta", "YK", "15:20", "Sisa 7", "Bima",
//            "Malang", "ML", "20:55", "Eksekutif - A", "5 jam 35 menit", "2024-01-18"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp110.000", "Lempuyangan", "LPN", "23:30", "", "Argo Muria",
//            "Semarang Tawang", "SMT", "03:00", "Ekonomi - C", "3 jam 30 menit", "2024-01-19"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp145.000", "Purwosari", "PWS", "18:45", "Sisa 14", "Gajayana",
//            "Malang", "ML", "00:20", "Ekonomi - A", "5 jam 35 menit", "2024-01-20"
//        ))
//
//        keretaList.add(dataKereta(
//            "Rp100.000", "Solo Balapan", "SLO", "21:30", "", "Taksaka",
//            "Yogyakarta", "YK", "03:05", "Ekonomi - B", "5 jam 35 menit", "2024-01-21"
//        ))


        for (kereta in keretaList) {
            db.collection("dataKereta")
                .add(kereta)
                .addOnSuccessListener { documentReference ->
                    Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error adding document", e)
                }
        }

        db.collection("dataKereta").get()
            .addOnSuccessListener { result ->
                Log.d("Firestore", "Success getting documents")
                val keretaList = result.documents.mapNotNull { it.toObject(dataKereta::class.java) }
                val filteredKeretaList = ArrayList(keretaList.filter {
                    it.stasiunKeberangkatan.equals(departureStation, ignoreCase = true) &&
                            it.stasiunTujuan.equals(destinationStation, ignoreCase = true) &&
                            it.tanggalBerangkat == departureDate
                })
                val keretaAdapter = KeretaAdapter(this, filteredKeretaList)
                binding.recyclerViewJadwalKereta.adapter = keretaAdapter
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
            }
    }
}
