package com.example.loketkereta

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.loketkereta.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "RegisterFragment"
    private var selectedBirthDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val sharedPreferences = requireActivity().getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        val registerButton = binding.btnToDashboardRegister
        registerButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            val fullName = binding.fullNameInput.text.toString()
            val birthDate = binding.birthInput.text.toString()

            Log.d(TAG, "Attempting to register with email: $email")

            if (validateAge(selectedBirthDate)) {
                registerUser(email, password, fullName, birthDate)
            } else {
                Log.d(TAG, "User age validation failed")
                Toast.makeText(requireContext(), "Anda harus berusia minimal 15 tahun untuk mendaftar.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.birthInput.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun registerUser(email: String, password: String, fullName: String, birthDate: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d(TAG, "createUserWithEmail:success")
                    saveUserDataToDatabase(user?.uid, fullName, birthDate)

                    // Mengubah Intent ke MainActivity
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)

                    // Memperbarui SharedPreferences setelah berhasil mendaftar
                    val sharedPreferences = requireActivity().getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()

                    requireActivity().finish()
                } else {
                    Log.d(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserDataToDatabase(userId: String?, fullName: String, birthDate: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users")

        val userRole = "user"

        if (userId != null) {
            myRef.child(userId).child("role").setValue(userRole)
            myRef.child(userId).child("name").setValue(fullName)
            myRef.child(userId).child("birthDate").setValue(birthDate)
            Log.d(TAG, "User role, name and birth date set in database for userId: $userId")
        }
    }

    private fun validateAge(birthdate: String): Boolean {
        val currentDate = Calendar.getInstance()
        val selectedDate = Calendar.getInstance()

        val birthDateParts = birthdate.split("-")
        if (birthDateParts.size != 3) {
            return false
        }

        val birthYear = birthDateParts[0].toInt()
        val birthMonth = birthDateParts[1].toInt()
        val birthDay = birthDateParts[2].toInt()

        selectedDate.set(birthYear, birthMonth - 1, birthDay)

        var age = currentDate.get(Calendar.YEAR) - selectedDate.get(Calendar.YEAR)

        if (currentDate.get(Calendar.MONTH) < selectedDate.get(Calendar.MONTH) ||
            (currentDate.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                    currentDate.get(Calendar.DAY_OF_MONTH) < selectedDate.get(Calendar.DAY_OF_MONTH))) {
            age--
        }

        return age >= 15
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            selectedBirthDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            binding.birthInput.setText(selectedBirthDate)
        }, year, month, day)

        datePickerDialog.show()
    }
}