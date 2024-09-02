package com.example.myapplication4

// LoginActivity.kt
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication4.MainActivity
import com.example.myapplication4.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput: EditText = findViewById(R.id.emailInput)
        val passwordInput: EditText = findViewById(R.id.passwordInput)
        val loginButton: Button = findViewById(R.id.loginButton)
        val forgotPasswordText: TextView = findViewById(R.id.forgotPasswordText)
        val registerPromptText: TextView = findViewById(R.id.registerPromptText)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            // Aquí iría la lógica de autenticación
            Toast.makeText(this, "Iniciando sesión con: $email", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        forgotPasswordText.setOnClickListener {
            // Aquí iría la lógica para recuperar la contraseña
            Toast.makeText(this, "Recuperar contraseña", Toast.LENGTH_SHORT).show()
        }

        registerPromptText.setOnClickListener {
            // Aquí iría la lógica para abrir la pantalla de registro
            Toast.makeText(this, "Abrir pantalla de registro", Toast.LENGTH_SHORT).show()
        }
    }
}