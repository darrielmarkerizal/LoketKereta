package com.example.loketkereta

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.loketkereta.databinding.FragmentBookBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentBook : Fragment() {
    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpinners()
        setUpDatePicker()
    }

    private fun setUpSpinners() {
        val stations = resources.getStringArray(R.array.stasiun_kereta)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, stations)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerAsal.adapter = adapter
        binding.spinnerTujuan.adapter = adapter
    }

    private fun setUpDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.inputTanggal.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val selectedDate = dateFormat.format(selectedCalendar.time)
                binding.inputTanggal.setText(selectedDate)
            }, year, month, day)

            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}