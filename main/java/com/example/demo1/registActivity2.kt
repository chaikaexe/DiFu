package com.example.demo1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class registActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_regist2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loginEditText = findViewById<EditText>(R.id.editTextLogin)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<CardView>(R.id.voiti)
        val registerButton = findViewById<CardView>(R.id.registr) // Обработка кнопки регистрации

        loginButton.setOnClickListener {
            val username = loginEditText.text.toString()
            val password = passwordEditText.text.toString()
            performLogin(username, password)
        }

        registerButton.setOnClickListener {
            // Переход к экрану регистрации
            val intent = Intent(this, registra4iaiaActivity2::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin(username: String, password: String) {
        // Получение SharedPreferences
        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)

        // Проверка, существуют ли данные пользователя
        val storedPassword = sharedPreferences.getString(username, null)

        if (storedPassword != null && storedPassword == password) {
            // Успешный вход
            sharedPreferences.edit().putString("loggedInUser", username).apply()
            Toast.makeText(this, "Вход успешен", Toast.LENGTH_SHORT).show()
            // Создание Intent и передача логина в MenuIkonko
            val intent = Intent(this, MenuIkonko::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
            // Перенаправление на следующий экран (можно изменить на нужный)
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Ошибка при входе
            Toast.makeText(this, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
        }
    }
}
