package com.example.myapplication4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication4.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val registerButton: Button = findViewById(R.id.registerButton)
        val loginButton: Button = findViewById(R.id.loginButton)

        registerButton.setOnClickListener {
            Toast.makeText(this, "Registro iniciado", Toast.LENGTH_SHORT).show()
            // Aquí iría la lógica para iniciar el proceso de registro
        }

        loginButton.setOnClickListener {
            Toast.makeText(this, "Inicio de sesión iniciado", Toast.LENGTH_SHORT).show()
            // Aquí iría la lógica para iniciar el proceso de inicio de sesión
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}