package com.example.demo1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AchievementsActivity2 : AppCompatActivity() {
    private lateinit var checkboxAchievement1: CheckBox
    private lateinit var checkboxAchievement2: CheckBox
    private lateinit var checkboxAchievement3: CheckBox

    private fun getCurrentUsername(): String {
        val sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE)
        return sharedPreferences.getString("loggedInUser", "пользователь") ?: "пользователь"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_achievements2)

        // Инициализация чекбоксов
        checkboxAchievement1 = findViewById(R.id.checkbox_achievement1)
        checkboxAchievement2 = findViewById(R.id.checkbox_achievement2)
        checkboxAchievement3 = findViewById(R.id.checkbox_achievement3)

        // Загружаем текущего пользователя и его достижения
        val currentUser = getCurrentUsername()
        val sharedPreferences = getSharedPreferences("user_achievements", MODE_PRIVATE)

        // Устанавливаем состояние чекбоксов из сохраненных данных
        checkboxAchievement1.isChecked = sharedPreferences.getBoolean("${currentUser}_achievement1", false)
        checkboxAchievement2.isChecked = sharedPreferences.getBoolean("${currentUser}_achievement2", false)
        checkboxAchievement3.isChecked = sharedPreferences.getBoolean("${currentUser}_achievement3", false)

        // Настраиваем сохранение состояния чекбоксов при изменении
        checkboxAchievement1.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("${currentUser}_achievement1", isChecked).apply()
        }
        checkboxAchievement2.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("${currentUser}_achievement2", isChecked).apply()
        }
        checkboxAchievement3.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("${currentUser}_achievement3", isChecked).apply()
        }

        // Настройка отступов для системных панелей
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Кнопка "Назад"
        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()  // Используем новый способ обработки кнопки "Назад"
        }

        // Обработка системной кнопки "Назад" с использованием onBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Возвращаемся к главному экрану приложения
                val intent = Intent(this@AchievementsActivity2, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()  // Закрываем текущее Activity
            }
        })
    }
}
