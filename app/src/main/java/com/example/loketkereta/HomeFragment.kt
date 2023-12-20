package com.example.loketkereta

import android.app.DatePickerDialog
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loketkereta.stasiun.Stasiun
import com.example.loketkereta.stasiun.StationApi
import com.google.firebase.firestore.DocumentSnapshot
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    val retrofit = Retrofit.Builder()
        .baseUrl("https://booking.kai.id/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(StationApi::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        fetchAndDisplayUserName()
        setupStationsSpinner()

        binding.swapIcon.setOnClickListener {
            swapDepartureAndDestination()
        }

        binding.buttonCariTiket.setOnClickListener{
            val departureStation = binding.spinnerKeberangkatan.selectedItem.toString().split(",")[0]
            val destinationStation = binding.spinnerTujuan.selectedItem.toString().split(",")[0]
            val departureDate = binding.tanggalKeberangkatan.text.toString()
            val passengerCount = binding.jumlahPenumpang.text.toString().toInt()

            if (departureStation.isNotEmpty() && destinationStation.isNotEmpty() && departureDate.isNotEmpty() && passengerCount > 0) {
                val intent = Intent(activity, DaftarJadwalActivity::class.java)
                intent.putExtra("departureStation", departureStation)
                intent.putExtra("destinationStation", destinationStation)
                startActivity(intent)
            } else {
                Toast.makeText(context, "Harap pilih stasiun, tanggal keberangkatan, dan jumlah penumpang", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun fetchAndDisplayUserName() {
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

    private fun setupStationsSpinner() {
        val call = api.getStations()
        call.enqueue(object : Callback<List<Stasiun>> {
            override fun onResponse(call: Call<List<Stasiun>>, response: Response<List<Stasiun>>) {
                if (response.isSuccessful) {
                    Log.d("HomeFragment", "Response: ${response.body()}")
                    val stations = response.body()
                    val stationDetails = stations?.map { Triple(it.code, "${it.name}, ${it.cityName}", it.code) }?.toMutableList() ?: mutableListOf()
                    stationDetails.add(0, Triple("", "Pilih Stasiun", ""))

                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stationDetails.map { it.second })
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    binding.spinnerKeberangkatan.adapter = adapter
                    binding.spinnerTujuan.adapter = adapter

                    binding.stasiunKeberangkatan.visibility = View.GONE
                    binding.stasiunTujuan.visibility = View.GONE

                    binding.spinnerKeberangkatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if (position != 0) {
                                val selectedStation = stationDetails[position]
                                val stationName = selectedStation.second.split(",")[0].toLowerCase().capitalize()
                                if (position == binding.spinnerTujuan.selectedItemPosition) {
                                    Toast.makeText(context, "Stasiun keberangkatan dan tujuan tidak boleh sama", Toast.LENGTH_SHORT).show()
                                    binding.spinnerKeberangkatan.setSelection(0)
                                } else {
                                    binding.stasiunKeberangkatan.setText("Stasiun " + stationName)
                                    binding.stasiunKeberangkatan.visibility = View.VISIBLE
                                }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            binding.stasiunKeberangkatan.visibility = View.GONE
                        }
                    }

                    binding.spinnerTujuan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if (position != 0) {
                                val selectedStation = stationDetails[position]
                                val stationName = selectedStation.second.split(",")[0].toLowerCase().capitalize()
                                if (position == binding.spinnerKeberangkatan.selectedItemPosition) {
                                    Toast.makeText(context, "Stasiun keberangkatan dan tujuan tidak boleh sama", Toast.LENGTH_SHORT).show()
                                    binding.spinnerTujuan.setSelection(0)
                                } else {
                                    binding.stasiunTujuan.setText("Stasiun " + stationName)
                                    binding.stasiunTujuan.visibility = View.VISIBLE
                                }
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            binding.stasiunTujuan.visibility = View.GONE
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Stasiun>>, t: Throwable) {
                Log.e("HomeFragment", "Error: ${t.message}")
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPassengerCounter()
        setupTanggalKeberangkatan()
    }

    private fun setupTanggalKeberangkatan() {
        val tanggalKeberangkatanLayout = binding.layoutTanggal
        val tanggalKeberangkatanTextView = binding.tanggalKeberangkatan

        tanggalKeberangkatanLayout.setOnClickListener {
            showDatePicker(tanggalKeberangkatanTextView)
        }
    }

    private fun showDatePicker(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = formatSelectedDate(selectedYear, selectedMonth, selectedDay)
            textView.text = formattedDate
        }, year, month, day)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun formatSelectedDate(year: Int, month: Int, day: Int): String {
        val selectedDate = Calendar.getInstance()
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.MONTH, month)
        selectedDate.set(Calendar.DAY_OF_MONTH, day)
        val date = selectedDate.time

        val sdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        return sdf.format(date)
    }

    private fun setupPassengerCounter() {
        val minusImageView = binding.iconMinus
        val addImageView = binding.iconPlus
        val passengerCount = binding.jumlahPenumpang

        minusImageView.setOnClickListener {
            val currentCount = passengerCount.text.toString().toInt()
            Log.d("PassengerCount", "Current Count (Before): $currentCount")
            if (currentCount > 0) {
                val newCount = currentCount - 1
                passengerCount.text = newCount.toString()
                Log.d("PassengerCount", "Current Count (After): $newCount")
            }
        }

        addImageView.setOnClickListener {
            val currentCount = passengerCount.text.toString().toInt()
            Log.d("PassengerCount", "Current Count (Before): $currentCount")
            val newCount = currentCount + 1
            passengerCount.text = newCount.toString()
            Log.d("PassengerCount", "Current Count (After): $newCount")
        }
    }

    private fun swapDepartureAndDestination() {
        val departureStation = binding.spinnerKeberangkatan.selectedItemPosition
        val destinationStation = binding.spinnerTujuan.selectedItemPosition

        if (departureStation == destinationStation) {
            Toast.makeText(context, "Stasiun keberangkatan dan tujuan tidak boleh sama", Toast.LENGTH_SHORT).show()
        } else {
            binding.spinnerKeberangkatan.setSelection(destinationStation)
            binding.spinnerTujuan.setSelection(departureStation)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}