package com.example.demo1

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import androidx.cardview.widget.CardView
import android.widget.Button

class CartochaActivity2 : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var nazvaneTextView: TextView
    private lateinit var opisanieTextView: TextView
    private lateinit var vremyaTextView: TextView
    private lateinit var adresTextView: TextView
    private lateinit var contactTextView: TextView
    private lateinit var nomerTextView: TextView
    private lateinit var izbranoButton: CardView // Добавляем это сюда

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cartocha2)

        // Инициализация TextView
        nazvaneTextView = findViewById(R.id.nazvane)
        opisanieTextView = findViewById(R.id.opisanie)
        vremyaTextView = findViewById(R.id.vrema)
        adresTextView = findViewById(R.id.adres)
        contactTextView = findViewById(R.id.contact)
        nomerTextView = findViewById(R.id.nomer)

        // Инициализация кнопки назад
        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // Завершаем CartochaActivity2 и возвращаемся в MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Инициализация CardView (кнопки "Добавить в избранное")
        izbranoButton = findViewById(R.id.izbrano)
        izbranoButton.setOnClickListener {
            val placeName = nazvaneTextView.text.toString()
            saveToFavorites(placeName)
            Toast.makeText(this, "$placeName добавлено в избранное", Toast.LENGTH_SHORT).show()
        }

        // Получаем название места из интента
        val placeName = intent.getStringExtra("PLACE_NAME")
        if (placeName != null) {
            dbHelper = DBHelper(this)
            displayPlaceInfo(placeName)
        } else {
            Toast.makeText(this, "Место не найдено", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // Метод для отображения информации о месте
    private fun displayPlaceInfo(placeName: String) {
        val placeInfo = dbHelper.getPlaceInfo(placeName)
        if (placeInfo != null) {
            nazvaneTextView.text = placeInfo.placeName
            opisanieTextView.text = placeInfo.categoryId // Замените на фактическое описание, если оно есть
            vremyaTextView.text = placeInfo.workingHours
            adresTextView.text = placeInfo.address
            contactTextView.text = "Контакты" // Замените на фактические контакты, если они есть
            nomerTextView.text = placeInfo.contactInfo // Замените на фактические контакты, если они есть
        } else {
            Toast.makeText(this, "Информация о месте недоступна", Toast.LENGTH_SHORT).show()
        }
    }

    // Метод для сохранения в избранное
    private fun saveToFavorites(placeName: String) {
        val sharedPreferences = getSharedPreferences("favorites", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(placeName, placeName) // Сохраняем название места
        editor.apply()
    }
}
