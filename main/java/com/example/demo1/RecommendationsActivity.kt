package com.example.demo1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecommendationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recomend)
        // Обработка кнопки "Назад" на экране
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            // Переход на экран MenuIkonko
            val intent = Intent(this, MenuIkonko::class.java)
            startActivity(intent)
            finish() // Закрываем текущую активность
        }
        // Обработка системной кнопки "Назад" через OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Переход на экран MenuIkonko при нажатии системной кнопки "Назад"
                val intent = Intent(this@RecommendationsActivity, MenuIkonko::class.java)
                startActivity(intent)
                finish() // Закрываем текущую активность
            }
        })
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
