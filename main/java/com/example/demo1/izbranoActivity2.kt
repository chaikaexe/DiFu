package com.example.demo1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class izbranoActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_izbrano2)

        // Обработка кнопки "Назад" на экране
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, MenuIkonko::class.java)
            startActivity(intent)
            finish() // Закрываем текущую активность
        }

        // Обработка системной кнопки "Назад"
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@izbranoActivity2, MenuIkonko::class.java)
                startActivity(intent)
                finish()
            }
        })

        // Получение избранных мест из SharedPreferences
        val sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE)
        val allFavorites = sharedPreferences.all

        // LinearLayout для добавления избранных мест
        val favoritesLayout = findViewById<LinearLayout>(R.id.favorites_layout)

        if (allFavorites.isNotEmpty()) {
            for ((key, value) in allFavorites) {
                val placeName = key.toString()

                // Создаем TextView для каждого избранного места
                val textView = TextView(this).apply {
                    text = placeName
                    textSize = 24f
                    setTextColor(resources.getColor(R.color.white))
                    // Можно добавить и другие стили
                }

                // Добавляем TextView в LinearLayout
                favoritesLayout.addView(textView)
            }
        } else {
            // Если нет избранных мест
            val noFavoritesTextView = TextView(this).apply {
                text = "Нет избранных мест"
                textSize = 24f
                setTextColor(resources.getColor(R.color.white))
            }
            favoritesLayout.addView(noFavoritesTextView)
        }

        // Устанавливаем отступы для системных панелей
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
