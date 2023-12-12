package com.example.loketkereta

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.loketkereta.databinding.ActivityBookingBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class BookingActivity : AppCompatActivity() {
    private val hargaTiketPerKelas = intArrayOf(0, 100000, 150000, 200000, 250000, 300000)

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


    private lateinit var binding: ActivityBookingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var jumlahAnak = 0
        var jumlahDewasa = 0

        val stasiunArray = resources.getStringArray(R.array.stasiun_kereta)
        val kelasArray = resources.getStringArray(R.array.kelas_kereta)

        val spinnerBerangkat = binding.spinnerBerangkat
        val spinnerTujuan = binding.spinnerTujuan
        val spinnerKelas = binding.spinnerKelas
        val totalHargaTextView = binding.totalHarga
        val jumlahAnakTextView = binding.textJumlahAnak
        val jumlahDewasaTextView = binding.textJumlahDewasa
        val chipGroup = binding.chipGroup

        val anakMinusButton = binding.minus1
        val anakPlusButton = binding.add1
        val dewasaMinusButton = binding.minus2
        val dewasaPlusButton = binding.add2

        val stasiunPlaceholder = "Pilih stasiun"
        val stasiunList = mutableListOf(stasiunPlaceholder)
        stasiunList.addAll(stasiunArray)
        val stasiunAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, stasiunList.toTypedArray())

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

        var kelasTerpilih = 0
        var stasiunAwalTerpilih = -1
        var stasiunAkhirTerpilih = -1

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            var additionalPrice = 0
            val checkedChipIds = group.checkedChipIds
            for (chipId in checkedChipIds) {
                val chip = group.findViewById<Chip>(chipId)
                val chipText = chip.text.toString()
                val chipPrice = when (chipText) {
                    "Akses Prioritas" -> 50000
                    "WiFi" -> 100000
                    "Makan dan Minum" -> 150000
                    "Entertainment" -> 200000
                    "Lounge Ekslusif" -> 250000
                    "Bantal" -> 300000
                    "Selimut" -> 350000
                    else -> 0
                }
                additionalPrice += chipPrice
            }
            Log.d("BookingActivity", "Additional price: $additionalPrice")
            if (isValidInput(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih)) {
                updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView, additionalPrice)
            }
        }

        val inputTanggal = binding.inputTanggal
        inputTanggal.setOnClickListener {
            showDatePickerDialog()
        }



        spinnerKelas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                kelasTerpilih = position
                Log.d("BookingActivity", "Kelas terpilih: $kelasTerpilih")
                if (isValidInput(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih)) {
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
                Log.d("BookingActivity", "Jumlah anak: $jumlahAnak")
                if (isValidInput(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih)) {
                    updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView)
                }
            }
        }

        anakPlusButton.setOnClickListener {
            jumlahAnak++
            jumlahAnakTextView.text = jumlahAnak.toString()
            Log.d("BookingActivity", "Jumlah anak: $jumlahAnak")
            if (isValidInput(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih)) {
                updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView)
            }
        }

        dewasaMinusButton.setOnClickListener {
            if (jumlahDewasa > 0) {
                jumlahDewasa--
                jumlahDewasaTextView.text = jumlahDewasa.toString()
                Log.d("BookingActivity", "Jumlah dewasa: $jumlahDewasa")
                if (isValidInput(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih)) {
                    updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView)
                }
            }
        }

        dewasaPlusButton.setOnClickListener {
            jumlahDewasa++
            jumlahDewasaTextView.text = jumlahDewasa.toString()
            Log.d("BookingActivity", "Jumlah dewasa: $jumlahDewasa")
            if (isValidInput(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih)) {
                updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView)
            }
        }

        spinnerBerangkat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                stasiunAwalTerpilih = position
                Log.d("BookingActivity", "Stasiun awal: $stasiunAwalTerpilih")
                if (stasiunAwalTerpilih == stasiunAkhirTerpilih) {
                    Toast.makeText(applicationContext, "Stasiun asal dan tujuan tidak boleh sama.", Toast.LENGTH_SHORT).show()
                    spinnerBerangkat.setSelection(0)
                    stasiunAwalTerpilih = 0
                }
                if (isValidInput(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih)) {
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
                Log.d("BookingActivity", "Stasiun akhir: $stasiunAkhirTerpilih")
                if (stasiunAwalTerpilih == stasiunAkhirTerpilih) {
                    Toast.makeText(applicationContext, "Stasiun asal dan tujuan tidak boleh sama.", Toast.LENGTH_SHORT).show()
                    spinnerTujuan.setSelection(0)
                    stasiunAkhirTerpilih = 0
                }
                if (isValidInput(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih)) {
                    updateTotalHarga(jumlahAnak, jumlahDewasa, kelasTerpilih, stasiunAwalTerpilih, stasiunAkhirTerpilih, totalHargaTextView)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        val btnBooking = binding.btnBooking

        btnBooking.setOnClickListener {
            val selectedDate = inputTanggal.text.toString()
            val stasiunAsal = spinnerBerangkat.selectedItem.toString()
            val stasiunTujuan = spinnerTujuan.selectedItem.toString()

            if (selectedDate.isNotEmpty() && stasiunAsal != "Pilih stasiun" && stasiunTujuan != "Pilih stasiun") {
                val intent = Intent(this, DashboardActivity::class.java)
                val tanggal = intent.getStringExtra("tanggal")
                intent.putExtra("tanggal", selectedDate)
                intent.putExtra("stasiunAsal", stasiunAsal)
                intent.putExtra("stasiunTujuan", stasiunTujuan)

                val selectedChips = chipGroup.checkedChipIds.map { chipId ->
                    chipGroup.findViewById<Chip>(chipId).text.toString()
                }
                intent.putStringArrayListExtra("paketTambahan", ArrayList(selectedChips))


                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Lengkapi semua data sebelum pesan perjalanan.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val selectedDate = "$year-${month + 1}-$dayOfMonth"
            val inputTanggal = findViewById<TextInputEditText>(R.id.inputTanggal)
            inputTanggal.setText(selectedDate)
        }, year, month, day)
        datePickerDialog.show()
    }



    private fun updateTotalHarga(jumlahAnak: Int, jumlahDewasa: Int, kelas: Int, stasiunAwal: Int, stasiunAkhir: Int, totalHargaTextView: TextView, additionalPrice: Int = 0) {
        if (jumlahAnak < 0 || jumlahDewasa < 0 || kelas < 0 || stasiunAwal < 0 || stasiunAkhir < 0 || stasiunAwal >= hargaTambahanStasiun.size || stasiunAkhir >= hargaTambahanStasiun[0].size) {
            throw IllegalArgumentException("Invalid argument")
        }
        if (stasiunAwal == stasiunAkhir) {
            totalHargaTextView.text = "Rp 0"
            return
        }
        if (jumlahAnak == 0 && jumlahDewasa == 0) {
            totalHargaTextView.text = "Rp 0"
            return
        }

        val hargaTiketDewasa = hargaTiketPerKelas[kelas] + hargaTambahanStasiun[stasiunAwal][stasiunAkhir]
        val hargaTiketAnak = hargaTiketDewasa / 2
        val totalHarga = (hargaTiketDewasa * jumlahDewasa + hargaTiketAnak * jumlahAnak + additionalPrice)
        totalHargaTextView.text = "Rp $totalHarga"
    }

    private fun isValidInput(jumlahAnak: Int, jumlahDewasa: Int, kelas: Int, stasiunAwal: Int, stasiunAkhir: Int): Boolean {
        if (jumlahAnak < 0 || jumlahDewasa < 0) {
            return false
        }

        if (kelas <= 0 || stasiunAwal <= 0 || stasiunAkhir <= 0) {
            return false
        }

        if (stasiunAwal == stasiunAkhir) {
            return false
        }

        if (stasiunAwal >= hargaTambahanStasiun.size || stasiunAkhir >= hargaTambahanStasiun[0].size) {
            return false
        }

        return true
    }
}