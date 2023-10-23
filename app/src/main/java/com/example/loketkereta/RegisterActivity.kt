package com.example.loketkereta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import android.app.DatePickerDialog
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var birthInput: EditText
    private lateinit var rememberCheckBox: CheckBox
    private var selectedBirthDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailInput = findViewById(R.id.emailInput)
        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)
        birthInput = findViewById(R.id.birthInput)
        rememberCheckBox = findViewById(R.id.rememberCheckBox)

        val registerButton: Button = findViewById(R.id.btnToLogin)
        registerButton.setOnClickListener {
            val email = emailInput.text.toString()
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (validateAge(selectedBirthDate)) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("email", email)
                intent.putExtra("username", username)
                intent.putExtra("password", password)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Anda harus berusia minimal 15 tahun untuk mendaftar.", Toast.LENGTH_SHORT).show()
            }
        }

        birthInput.setOnClickListener {
            showDatePickerDialog()
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
            birthInput.setText(selectedBirthDate)
        }, year, month, day)

        datePickerDialog.show()
    }
}