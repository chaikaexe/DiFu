package com.example.demo1

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast


class MenuIkonko : AppCompatActivity() {
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        logoutButton = findViewById(R.id.logout_button)

        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        val username = sharedPreferences.getString("loggedInUser", "пользователь") // если имя не найдено, по умолчанию "пользователь"

        if (username != null) {
            logoutButton.visibility = View.VISIBLE
        } else {
            logoutButton.visibility = View.GONE
        }

        logoutButton.setOnClickListener {
            sharedPreferences.edit().remove("logged_in_user").apply() // Удаляем данные пользователя
            Toast.makeText(this, "Вы вышли из профиля", Toast.LENGTH_SHORT).show()
            // Вернуться на экран входа или на главную
            val intent = Intent(this, registActivity2::class.java)
            startActivity(intent)
            finish() // Закрыть текущую активность
        }

        // Находим TextView и устанавливаем имя пользователя
        val usernameTextView = findViewById<TextView>(R.id.textViewUsername)
        usernameTextView.text = "Привет, $username!"

        // Кнопка "Назад"
        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener {
            finish()  // Закрываем текущее Activity
        }

        // Инициализация других кнопок
        val izbranoButton = findViewById<Button>(R.id.menu_option1)
        val recommendationsButton = findViewById<Button>(R.id.menu_option2)
        val openRegistraButton = findViewById<Button>(R.id.open_registra)


        // Установка слушателей для кнопок
        izbranoButton.setOnClickListener {
            val intent = Intent(this, izbranoActivity2::class.java)
            startActivity(intent)
            finish()
        }

        recommendationsButton.setOnClickListener {
            val intent = Intent(this, RecommendationsActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Обработка системной кнопки "Назад"
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()  // Закрываем текущее Activity
            }
        })

        openRegistraButton.setOnClickListener {
            val intent = Intent(this, registra4iaiaActivity2::class.java)
            startActivity(intent)
        }
    }
}
