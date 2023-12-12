package com.example.loketkereta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import android.app.DatePickerDialog
import android.util.Log
import com.example.loketkereta.databinding.ActivityRegisterBinding
import java.util.Calendar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.content.Context

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private var selectedBirthDate: String = ""
    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val sharedPreferences = getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        val registerButton = binding.btnToLogin
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
                Toast.makeText(this, "Anda harus berusia minimal 15 tahun untuk mendaftar.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.birthInput.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun registerUser(email: String, password: String, fullName: String, birthDate: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d(TAG, "createUserWithEmail:success")
                    saveUserDataToDatabase(user?.uid, fullName, birthDate)
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
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

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            selectedBirthDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            binding.birthInput.setText(selectedBirthDate)
        }, year, month, day)

        datePickerDialog.show()
    }
}