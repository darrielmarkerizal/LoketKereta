package com.example.loketkereta

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.loketkereta.databinding.ActivityAddJadwalBinding
import com.example.loketkereta.keretaAdmin.AppDatabase
import com.example.loketkereta.keretaAdmin.Kereta
import com.example.loketkereta.stasiun.Stasiun
import com.example.loketkereta.stasiun.StationApi
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class AddJadwal : AppCompatActivity() {
    private lateinit var binding: ActivityAddJadwalBinding

    val retrofit = Retrofit.Builder()
        .baseUrl("https://booking.kai.id/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(StationApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJadwalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val kereta = intent.getParcelableExtra<Kereta>("kereta")
        if (kereta != null) {
            populateFields(kereta)
            binding.saveButton.setOnClickListener {
                updateDataToFirestore(kereta.id)
                updateDataToRoom(kereta.id)
            }
        } else {
            binding.saveButton.setOnClickListener {
                saveDataToFirestore()
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveButton.setOnClickListener {
            saveDataToFirestore()
            saveDataToRoom()
        }

        setupStationsSpinner()
        setupDatePicker()
        setupTimePicker()
    }

    private fun setupStationsSpinner() {
        val call = api.getStations()
        call.enqueue(object : Callback<List<Stasiun>> {
            override fun onResponse(call: Call<List<Stasiun>>, response: Response<List<Stasiun>>) {
                if (response.isSuccessful) {
                    Log.d("AddJadwal", "Response: ${response.body()}")
                    val stations = response.body()
                    val stationNames = stations?.map { it.name.split(" ").joinToString(" ") { word -> word.lowercase(
                        Locale.getDefault()).replaceFirstChar { it.uppercase() } } }?.toMutableList() ?: mutableListOf()
                    stationNames.add(0, "Pilih Stasiun")

                    val adapter = ArrayAdapter(this@AddJadwal, android.R.layout.simple_spinner_item, stationNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    binding.spinnerStasiunKeberangkatan.adapter = adapter
                    binding.spinnerStasiunTujuan.adapter = adapter

                    binding.spinnerStasiunKeberangkatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if (position != 0) {
                                val selectedStation = stationNames[position]
                                if (position == binding.spinnerStasiunTujuan.selectedItemPosition) {
                                    Toast.makeText(this@AddJadwal, "Stasiun keberangkatan dan tujuan tidak boleh sama", Toast.LENGTH_SHORT).show()
                                    binding.spinnerStasiunKeberangkatan.setSelection(0)
                                }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Do nothing
                        }
                    }

                    binding.spinnerStasiunTujuan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if (position != 0) {
                                val selectedStation = stationNames[position]
                                if (position == binding.spinnerStasiunKeberangkatan.selectedItemPosition) {
                                    Toast.makeText(this@AddJadwal, "Stasiun keberangkatan dan tujuan tidak boleh sama", Toast.LENGTH_SHORT).show()
                                    binding.spinnerStasiunTujuan.setSelection(0)
                                }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Do nothing
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Stasiun>>, t: Throwable) {
                Log.e("AddJadwal", "Error: ${t.message}")
            }
        })
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.datePicker.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.datePicker.setText(selectedDate)
            }, year, month, day)

            datePickerDialog.show()
        }
    }

    private fun setupTimePicker() {
        binding.timePickerKeberangkatan.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.timePickerKeberangkatan.setText(selectedTime)
            }, hour, minute, true)

            timePickerDialog.show()
        }

        binding.timePickerTiba.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.timePickerTiba.setText(selectedTime)
            }, hour, minute, true)

            timePickerDialog.show()
        }
    }

    private fun saveDataToFirestore() {
        val keretaName = binding.keretaNameEditText.text.toString()
        val stasiunKeberangkatan = binding.spinnerStasiunKeberangkatan.selectedItem.toString()
        val stasiunTujuan = binding.spinnerStasiunTujuan.selectedItem.toString()
        val tanggalBerangkat = binding.datePicker.text.toString()
        val jamBerangkat = binding.timePickerKeberangkatan.text.toString()
        val jamTiba = binding.timePickerTiba.text.toString()
        val hargaInput = binding.editTextHarga.text.toString().toDouble()
        val harga = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(hargaInput)
        val kelasKereta = binding.editTextKelas.text.toString()

        val format = SimpleDateFormat("HH:mm")
        val date1 = format.parse(jamBerangkat)
        val date2 = format.parse(jamTiba)
        var difference = date2.time - date1.time
        if (difference < 0) {
            difference += 24 * 60 * 60 * 1000
        }
        val hours = difference / (60 * 60 * 1000)
        val minutes = difference / (60 * 1000) % 60
        val durasiPerjalanan = "$hours jam $minutes menit"

        val kereta = hashMapOf(
            "namaKereta" to keretaName,
            "stasiunKeberangkatan" to stasiunKeberangkatan,
            "stasiunTujuan" to stasiunTujuan,
            "tanggalBerangkat" to tanggalBerangkat,
            "jamBerangkat" to jamBerangkat,
            "jamTiba" to jamTiba,
            "harga" to harga,
            "kelasKereta" to kelasKereta,
            "durasiPerjalanan" to durasiPerjalanan
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("dataKereta")
            .add(kereta)
            .addOnSuccessListener { documentReference ->
                Log.d("AddJadwal", "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Log.w("AddJadwal", "Error adding document", e)
                Toast.makeText(this, "Error saving data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun populateFields(kereta: Kereta) {
        binding.keretaNameEditText.setText(kereta.namaKereta)
        (binding.spinnerStasiunKeberangkatan as? AutoCompleteTextView)?.setText(kereta.stasiunKeberangkatan, false)
        (binding.spinnerStasiunTujuan as? AutoCompleteTextView)?.setText(kereta.stasiunTujuan, false)
        binding.datePicker.setText(kereta.tanggalBerangkat)
        binding.timePickerKeberangkatan.setText(kereta.jamBerangkat)
        binding.timePickerTiba.setText(kereta.jamTiba)
        binding.editTextHarga.setText(kereta.harga.removePrefix("Rp"))
        binding.editTextKelas.setText(kereta.kelasKereta)
    }

    private fun getIndex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value) {
                return i
            }
        }
        return 0
    }

    private fun updateDataToFirestore(id: String) {
        val keretaName = binding.keretaNameEditText.text.toString()
        val stasiunKeberangkatan = binding.spinnerStasiunKeberangkatan.selectedItem.toString()
        val stasiunTujuan = binding.spinnerStasiunTujuan.selectedItem.toString()
        val tanggalBerangkat = binding.datePicker.text.toString()
        val jamBerangkat = binding.timePickerKeberangkatan.text.toString()
        val jamTiba = binding.timePickerTiba.text.toString()
        val hargaInput = binding.editTextHarga.text.toString().toDouble()
        val harga = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(hargaInput)
        val kelasKereta = binding.editTextKelas.text.toString()

        val format = SimpleDateFormat("HH:mm")
        val date1 = format.parse(jamBerangkat)
        val date2 = format.parse(jamTiba)
        var difference = date2.time - date1.time
        if (difference < 0) {
            difference += 24 * 60 * 60 * 1000
        }
        val hours = difference / (60 * 60 * 1000)
        val minutes = difference / (60 * 1000) % 60
        val durasiPerjalanan = "$hours jam $minutes menit"

        val kereta = hashMapOf(
            "namaKereta" to keretaName,
            "stasiunKeberangkatan" to stasiunKeberangkatan,
            "stasiunTujuan" to stasiunTujuan,
            "tanggalBerangkat" to tanggalBerangkat,
            "jamBerangkat" to jamBerangkat,
            "jamTiba" to jamTiba,
            "harga" to harga,
            "kelasKereta" to kelasKereta,
            "durasiPerjalanan" to durasiPerjalanan
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("dataKereta")
            .document(id)
            .set(kereta)
            .addOnSuccessListener {
                Log.d("AddJadwal", "DocumentSnapshot successfully updated!")
                Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Log.w("AddJadwal", "Error updating document", e)
                Toast.makeText(this, "Error updating data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveDataToRoom() {
        val keretaName = binding.keretaNameEditText.text.toString()
        val stasiunKeberangkatan = binding.spinnerStasiunKeberangkatan.selectedItem.toString()
        val stasiunTujuan = binding.spinnerStasiunTujuan.selectedItem.toString()
        val tanggalBerangkat = binding.datePicker.text.toString()
        val jamBerangkat = binding.timePickerKeberangkatan.text.toString()
        val jamTiba = binding.timePickerTiba.text.toString()
        val hargaInput = binding.editTextHarga.text.toString().toDouble()
        val harga = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(hargaInput)
        val kelasKereta = binding.editTextKelas.text.toString()

        val format = SimpleDateFormat("HH:mm")
        val date1 = format.parse(jamBerangkat)
        val date2 = format.parse(jamTiba)
        var difference = date2.time - date1.time
        if (difference < 0) {
            difference += 24 * 60 * 60 * 1000
        }
        val hours = difference / (60 * 60 * 1000)
        val minutes = difference / (60 * 1000) % 60
        val durasiPerjalanan = "$hours jam $minutes menit"

        val kereta = Kereta(
            id = UUID.randomUUID().toString(),
            namaKereta = keretaName,
            stasiunKeberangkatan = stasiunKeberangkatan,
            stasiunTujuan = stasiunTujuan,
            tanggalBerangkat = tanggalBerangkat,
            jamBerangkat = jamBerangkat,
            jamTiba = jamTiba,
            harga = harga,
            kelasKereta = kelasKereta,
            durasiPerjalanan = durasiPerjalanan
        )

        val roomDb = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "kereta-db"
        ).build()
        Thread {
            roomDb.keretaDao().upsert(kereta)
            Log.d("AddJadwal", "Data successfully saved in Room")
        }.start()
    }

    private fun updateDataToRoom(id: String) {
        val keretaName = binding.keretaNameEditText.text.toString()
        val stasiunKeberangkatan = binding.spinnerStasiunKeberangkatan.selectedItem.toString()
        val stasiunTujuan = binding.spinnerStasiunTujuan.selectedItem.toString()
        val tanggalBerangkat = binding.datePicker.text.toString()
        val jamBerangkat = binding.timePickerKeberangkatan.text.toString()
        val jamTiba = binding.timePickerTiba.text.toString()
        val hargaInput = binding.editTextHarga.text.toString().toDouble()
        val harga = "Rp" + NumberFormat.getNumberInstance(Locale("id", "ID")).format(hargaInput)
        val kelasKereta = binding.editTextKelas.text.toString()

        val format = SimpleDateFormat("HH:mm")
        val date1 = format.parse(jamBerangkat)
        val date2 = format.parse(jamTiba)
        var difference = date2.time - date1.time
        if (difference < 0) {
            difference += 24 * 60 * 60 * 1000
        }
        val hours = difference / (60 * 60 * 1000)
        val minutes = difference / (60 * 1000) % 60
        val durasiPerjalanan = "$hours jam $minutes menit"

        val kereta = Kereta(
            id = id,
            namaKereta = keretaName,
            stasiunKeberangkatan = stasiunKeberangkatan,
            stasiunTujuan = stasiunTujuan,
            tanggalBerangkat = tanggalBerangkat,
            jamBerangkat = jamBerangkat,
            jamTiba = jamTiba,
            harga = harga,
            kelasKereta = kelasKereta,
            durasiPerjalanan = durasiPerjalanan
        )

        val roomDb = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "kereta-db"
        ).build()
        Thread {
            roomDb.keretaDao().update(kereta)
        }.start()
    }
}