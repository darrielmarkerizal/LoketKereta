package com.example.loketkereta

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameInput = findViewById(R.id.usernameInput)
        passwordInput = findViewById(R.id.passwordInput)

        val loginButton: Button = findViewById(R.id.btnToDashboard)
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (validateLogin(username, password)) {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Login gagal. Pastikan data yang Anda masukkan benar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateLogin(username: String, password: String): Boolean {
        val registeredUsername = intent.getStringExtra("username")
        val registeredPassword = intent.getStringExtra("password")

        return username == registeredUsername && password == registeredPassword
    }
}