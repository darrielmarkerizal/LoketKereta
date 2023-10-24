package com.example.loketkereta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class BookingActivity : AppCompatActivity() {
    // Harga tiket berdasarkan kelas
    private val hargaTiketPerKelas = intArrayOf(0, 100000, 150000, 200000, 250000, 300000)

    // Harga tambahan berdasarkan stasiun awal dan akhir
    private val hargaTambahanStasiun = arrayOf(
        intArrayOf(0, 50000, 100000, 150000, 200000, 250000, 300000, 350000, 400000, 450000, 500000, 550000, 600000, 650000, 700000),
        intArrayOf(50000, 0, 75000, 125000, 175000, 225000, 275000, 325000, 375000, 425000, 475000, 525000, 575000, 625000, 675000),
        intArrayOf(100000, 75000, 0, 125000, 175000, 225000, 275000, 325000, 375000, 425000, 475000, 525000, 575000, 625000, 675000),
        intArrayOf(150000, 125000, 75000, 0, 175000, 225000, 275000, 325000, 375000, 425000, 475000, 525000, 575000, 625000, 675000),
        intArrayOf(200000, 175000, 125000, 75000, 0, 225000, 275000, 325000, 375000, 425000, 475000, 525000, 575000, 625000, 675000),
        intArrayOf(250000, 225000, 175000, 125000, 75000, 0, 275000, 325000, 375000, 425000, 475000, 525000, 575000, 625000, 675000),
        intArrayOf(300000, 275000, 225000, 175000, 125000, 75000, 0, 325000, 375000, 425000, 475000, 525000, 575000, 625000, 675000),
        intArrayOf(350000, 325000, 275000, 225000, 175000, 125000, 75000, 0, 375000, 425000, 475000, 525000, 575000, 625000, 675000),
        intArrayOf(400000, 375000, 325000, 275000, 225000, 175000, 125000, 75000, 0, 425000, 475000, 525000, 575000, 625000, 675000),
        intArrayOf(450000, 425000, 375000, 325000, 275000, 225000, 175000, 125000, 75000, 0, 475000, 525000, 575000, 625000, 675000),
        intArrayOf(500000, 475000, 425000, 375000, 325000, 275000, 225000, 175000, 125000, 75000, 0, 525000, 575000, 625000, 675000),
        intArrayOf(550000, 525000, 475000, 425000, 375000, 325000, 275000, 225000, 175000, 125000, 75000, 0, 575000, 625000, 675000),
        intArrayOf(600000, 575000, 525000, 475000, 425000, 375000, 325000, 275000, 225000, 175000, 125000, 75000, 0, 625000, 675000),
        intArrayOf(650000, 625000, 575000, 525000, 475000, 425000, 375000, 325000, 375000, 425000, 475000, 525000, 575000, 625000, 675000),
        intArrayOf(700000, 675000, 625000, 575000, 525000, 475000, 425000, 375000, 325000, 275000, 225000, 175000, 125000, 75000, 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        val stasiunArray = resources.getStringArray(R.array.stasiun_kereta)
        val kelasArray = resources.getStringArray(R.array.kelas_kereta)

        val spinnerBerangkat = findViewById<Spinner>(R.id.spinner_berangkat)
        val spinnerTujuan = findViewById<Spinner>(R.id.spinner_tujuan)
        val spinnerKelas = findViewById<Spinner>(R.id.spinner_kelas)
        val totalHargaTextView = findViewById<TextView>(R.id.totalHarga)
        val jumlahAnakTextView = findViewById<TextView>(R.id.textJumlahAnak)
        val jumlahDewasaTextView = findViewById<TextView>(R.id.textJumlahDewasa)

        val anakMinusButton = findViewById<ImageView>(R.id.minus1)
        val anakPlusButton = findViewById<ImageView>(R.id.add1)
        val dewasaMinusButton = findViewById<ImageView>(R.id.minus2)
        val dewasaPlusButton = findViewById<ImageView>(R.id.add2)

        // Tambahkan placeholder "Pilih stasiun" pada spinner
        val stasiunPlaceholder = "Pilih stasiun"
        val stasiunList = mutableListOf(stasiunPlaceholder)
        stasiunList.addAll(stasiunArray)
        val stasiunAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stasiunList.toTypedArray())

        // Tambahkan placeholder "Pilih kelas" pada spinner
        val kelasPlaceholder = "Pilih kelas"
        val kelasList = mutableListOf(kelasPlaceholder)
        kelasList.addAll(kelasArray)
        val kelasAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kelasList.toTypedArray())
        kelasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKelas.adapter = kelasAdapter

        stasiunAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        kelasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerBerangkat.adapter = stasiunAdapter
        spinnerTujuan.adapter = stasiunAdapter
        spinnerKelas.adapter = kelasAdapter

        spinnerBerangkat.prompt = stasiunPlaceholder
        spinnerTujuan.prompt = stasiunPlaceholder
        spinnerKelas.prompt = kelasPlaceholder

        var jumlahAnak = 0
        var jumlahDewasa = 0
        var kelasTerpilih = 0
        var stasiunAwalTerpilih = -1
        var stasiunAkhirTerpilih = -1

        spinnerKelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                kelasTerpilih = position
                updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        spinnerBerangkat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                stasiunAwalTerpilih = position
                // Periksa apakah stasiun asal dan tujuan berbeda sebelum memanggil fungsi updateTotalHarga
                if (stasiunAwalTerpilih == stasiunAkhirTerpilih) {
                    // Stasiun asal dan tujuan sama, tampilkan pesan kesalahan
                    Toast.makeText(applicationContext, "Stasiun asal dan tujuan tidak boleh sama.", Toast.LENGTH_SHORT).show()
                    // Reset stasiun asal ke placeholder "Pilih stasiun"
                    spinnerBerangkat.setSelection(0)
                } else {
                    updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        spinnerTujuan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                stasiunAkhirTerpilih = position
                // Periksa apakah stasiun asal dan tujuan berbeda sebelum memanggil fungsi updateTotalHarga
                if (stasiunAwalTerpilih == stasiunAkhirTerpilih) {
                    // Stasiun asal dan tujuan sama, tampilkan pesan kesalahan
                    Toast.makeText(applicationContext, "Stasiun asal dan tujuan tidak boleh sama.", Toast.LENGTH_SHORT).show()
                    // Reset stasiun tujuan ke placeholder "Pilih stasiun"
                    spinnerTujuan.setSelection(0)
                } else {
                    updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        anakMinusButton.setOnClickListener {
            if (jumlahAnak > 0) {
                jumlahAnak--
                jumlahAnakTextView.text = jumlahAnak.toString()
                updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView)
            }
        }

        anakPlusButton.setOnClickListener {
            jumlahAnak++
            jumlahAnakTextView.text = jumlahAnak.toString()
            updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView)
        }

        dewasaMinusButton.setOnClickListener {
            if (jumlahDewasa > 0) {
                jumlahDewasa--
                jumlahDewasaTextView.text = jumlahDewasa.toString()
                updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView)
            }
        }

        dewasaPlusButton.setOnClickListener {
            jumlahDewasa++
            jumlahDewasaTextView.text = jumlahDewasa.toString()
            updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView)
        }
    }

    private fun updateTotalHarga(jumlahAnak: Int, jumlahDewasa: Int, kelas: Int, stasiunAwal: Int, stasiunAkhir: Int, totalHargaTextView: TextView) {
        if (kelas >= 0 && stasiunAwal >= 0 && stasiunAkhir >= 0 && stasiunAwal != stasiunAkhir && (jumlahAnak > 0 || jumlahDewasa > 0)) {
            val hargaTiket = hargaTiketPerKelas[kelas]
            val hargaTambahan = hargaTambahanStasiun[stasiunAwal][stasiunAkhir]

            val hargaAnak = hargaTiket / 2

            val totalHarga = (hargaTiket * jumlahDewasa + hargaAnak * jumlahAnak + hargaTambahan)

            totalHargaTextView.text = "Rp $totalHarga"
        } else {
            totalHargaTextView.text = "Rp 0"
        }
    }
}
