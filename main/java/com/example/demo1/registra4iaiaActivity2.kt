package com.example.demo1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Intent
import androidx.cardview.widget.CardView


class registra4iaiaActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registra4iaia2)
        Log.d("RegistraActivity", "Activity opened") // Логируем открытие активности

        // Инициализация кнопки "Есть аккаунт"
        val loginButton = findViewById<CardView>(R.id.logg)

        loginButton.setOnClickListener {
            // Переход к экрану входа
            val intent = Intent(this, registActivity2::class.java)
            startActivity(intent)
        }

        val registerCardView = findViewById<CardView>(R.id.registr)
        registerCardView.setOnClickListener {
            val username = findViewById<EditText>(R.id.view11).text.toString()
            val password = findViewById<EditText>(R.id.view10).text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                registerUser(username, password)
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }

    }
    // Глобальный массив для хранения пользователей (логин и пароль)
    private val usersData = mutableListOf<Array<String>>() // Массив всех пользователей
    // Метод для регистрации нового пользователя
    private fun registerUser(username: String, password: String) {
        // Получение SharedPreferences
        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)

        // Проверка, существует ли пользователь с таким логином
        if (sharedPreferences.contains(username)) {
            Toast.makeText(this, "Пользователь уже существует", Toast.LENGTH_SHORT).show()
            return
        }

        // Сохранение логина и пароля
        val editor = sharedPreferences.edit()
        editor.putString(username, password)
        editor.apply()
        Log.d("Registration", "Пользователь успешно зарегистрирован: $username")

        // Сообщение об успешной регистрации
        Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show()

        // Перенаправление на экран входа
        startActivity(Intent(this, registActivity2::class.java))
    }
}