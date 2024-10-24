package com.example.myapplication4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication4.model.AppDatabase
import com.example.myapplication4.repository.UserRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var userRepository: UserRepositoryImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar el repositorio
        val database = AppDatabase.getDatabase(applicationContext)
        userRepository = UserRepositoryImpl(database.userDao())

        // Inicializar usuarios por defecto (Por si no hay ninguno en la BD)
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.initializeDefaultUsers()
        }

        val emailInput: EditText = findViewById(R.id.emailInput)
        val passwordInput: EditText = findViewById(R.id.passwordInput)
        val loginButton: Button = findViewById(R.id.loginButton)
        val forgotPasswordText: TextView = findViewById(R.id.forgotPasswordText)
        val registerPromptText: TextView = findViewById(R.id.registerPromptText)

        loginButton.setOnClickListener {
            val username = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val user = userRepository.authenticateUser(username, password)

                withContext(Dispatchers.Main) {
                    if (user != null) {
                        // Usuario autenticado correctamente
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.putExtra("USER_ID", user.id)
                        intent.putExtra("USER_NAME", user.nombre)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Usuario o contraseña incorrectos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
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