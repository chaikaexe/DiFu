package com.example.demo1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.IOException
import android.util.Log
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class DBHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "DiFu.sql" // Имя базы данных в папке assets
        private const val DATABASE_VERSION = 1
        private const val DATABASE_PATH = "/data/data/com.example.demo1/databases/" // Путь к базе данных приложения
    }

    private var myDatabase: SQLiteDatabase? = null
    private val dbPath = context.getDatabasePath(DATABASE_NAME).path

    init {
        createDatabase()
        addPlaceData() // Добавляем данные при инициализации
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Создаем таблицу places, если она не существует
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS places (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                place_name TEXT,
                category_id TEXT,
                working_hours TEXT,
                address TEXT,
                contact_info TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Удаляем старую таблицу, если она существует
        db.execSQL("DROP TABLE IF EXISTS places")
        onCreate(db)
    }

    @Throws(IOException::class)
    fun createDatabase() {
        val dbExist = checkDatabase()
        if (!dbExist) {
            this.readableDatabase // Создаем пустую базу данных в файловой системе
            try {
                copyDatabase()
                Log.d("DBHelper", "База данных успешно скопирована")
            } catch (e: IOException) {
                throw Error("Error copying database")
            }
        } else {
            Log.d("DBHelper", "База данных уже существует")
        }
    }

    private fun checkDatabase(): Boolean {
        val dbPath = DATABASE_PATH + DATABASE_NAME
        return try {
            val file = context.getDatabasePath(DATABASE_NAME)
            file.exists()
        } catch (e: Exception) {
            false
        }
    }

    @Throws(IOException::class)
    private fun copyDatabase() {
        try {
            val dbFile = File(dbPath)
            if (dbFile.exists()) {
                dbFile.delete() // Удаляем старую базу данных
            }

            val inputStream: InputStream = context.assets.open(DATABASE_NAME)
            val outputStream: OutputStream = FileOutputStream(dbPath)

            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d("DBHelper", "Копирование завершено. Путь: $dbPath")
    }

    @Throws(IOException::class)
    fun openDatabase() {
        val dbPath = DATABASE_PATH + DATABASE_NAME
        myDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)
    }

    override fun close() {
        myDatabase?.close()
        super.close()
    }

    // Метод для добавления данных в таблицу places
    private fun addPlaceData() {
        val db = writableDatabase

        // Массив данных для добавления
        val placesData = arrayOf(
            arrayOf("Ski Club", "Активный отдых", "10:00 – 21:00", "Ул. Стахановская 1", "+7 (8332) 73-96-97"),
            arrayOf("ProSport", "Спортивный зал", "8:00 – 22:00", "Ул. Стахановская 1", "+79226688022"),
            arrayOf("Gym Station", "Спортивный зал", "6:30 – 23:00", "Ул. Воровского 43", "+7 (909) 716-13-69"),
            arrayOf("Spartak", "Спортивный зал", "7:00 – 22:00", "Ул. Октябрьский проспект 149", "+7 (8332) 57-79-02"),
            arrayOf("Arena Record gym", "Спортивный зал", "7:00 – 22:00", "Ул. Калинина 38Д", "+7832440266"),
            arrayOf("Море парк", "Спортивный зал", "6:00 – 22:00", "Ул. Некрасова 39", "+7832444999"),
            arrayOf("Modo Gym", "Спортивный зал", "7:00 – 22:00", "Ул. Воровского, 102А", "+7 (8332) 74-68-68"),
            arrayOf("Fitness family time", "Спортивный зал", "7:00 – 22:00", "Ул. Солнечная 25Б", "+7 (8332) 74-68-68"),
            arrayOf("Южный", "Активный отдых", "7:00 – 22:00", "Ул. Агрономическая 7", "+7 (912) 734-44-79"),
            arrayOf("Монро-Рассвет", "Спортивный зал", "8:30 – 20:00", "Ул. Воровского, 102А", "+7 (8332) 22-20-04"),
            arrayOf("Ski Club", "Активный отдых", "10:00 – 21:00", "ул. Подгорная 15", "+7 (8332) 73-96-97"),
            arrayOf("Ё/батон", "Здоровое питание", "10:00 – 20:00", "ул. Воровского, 114 ТЦ Атлант, этаж 2", "+7 (982) 389-43-49"),
            arrayOf("Eat For Day", "Здоровое питание", "09:00 – 20:00", "ул. Герцена, 88/1 этаж 1", "+7 (922) 947-74-07"),
            arrayOf("Siberian Wellness", "Здоровое питание", "10:00 – 20:00", "Московская ул., 25А", "+7 (8332) 38-31-10"),
            arrayOf("Живое масло", "Здоровое питание", "08:00 – 16:00", "Милицейская ул., 31", "+7 (922) 955-53-20"),
            arrayOf("Полезные продукты", "Здоровое питание", "09:00 – 20:00", "ул. Екатерины Кочкиной, 3Б", "+7 (8332) 69-95-51"),
            arrayOf("Чайхана 84/46", "Здоровое питание", "11:00 – 00:00", "ул. Карла Маркса, 84А", "+7 (8332) 41-84-46"),
            arrayOf("Брокколи", "Здоровое питание", "11:00 – 21:00", "ул. Горбачёва, 16", "+7 (8332) 21-75-50"),
            arrayOf("Сулугуни", "Здоровое питание", "10:55 – 23:00", "ул. Екатерины Кочкиной, 3Б, этаж 2", "+7 (8332) 44-00-70"),
            arrayOf("Самое популярное кафе Восток", "Здоровое питание", "12:00 – 03:00", "ул. Свободы, 11", "8 (800) 700-00-25"),
            arrayOf("Царское село", "Здоровое питание", "08:30 – 23:00", "Октябрьский просп., 125", "+7 (8332) 40-53-43")
        )

        // Цикл для добавления данных
        for (place in placesData) {
            val values = ContentValues().apply {
                put("place_name", place[0])          // Название места
                put("category_id", place[1])         // Идентификатор категории
                put("working_hours", place[2])       // Часы работы
                put("address", place[3])             // Адрес
                put("contact_info", place[4])        // Контактная информация
            }

            val rowId = db.insert("places", null, values)
            if (rowId != -1L) {
                Log.d("DBHelper", "Данные успешно добавлены в таблицу places: ${place[0]}")
            } else {
                Log.d("DBHelper", "Ошибка при добавлении данных в таблицу places: ${place[0]}")
            }
        }

        db.close() // Закрываем базу данных после завершения добавления
    }

    // Метод для получения информации о месте из базы данных
    fun getPlaceInfo(placeName: String): PlaceInfo? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT place_name, category_id, working_hours, address, contact_info FROM places WHERE place_name = ?",
            arrayOf(placeName)
        )

        var placeInfo: PlaceInfo? = null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val name = cursor.getString(cursor.getColumnIndexOrThrow("place_name"))
                val categoryId = cursor.getString(cursor.getColumnIndexOrThrow("category_id"))
                val workingHours = cursor.getString(cursor.getColumnIndexOrThrow("working_hours"))
                val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                val contactInfo = cursor.getString(cursor.getColumnIndexOrThrow("contact_info"))

                placeInfo = PlaceInfo(name, categoryId, workingHours, address, contactInfo)
            }
            cursor.close()
        }
        return placeInfo
    }
}

// Data class для хранения информации о месте
data class PlaceInfo(
    val placeName: String,
    val categoryId: String,
    val workingHours: String,
    val address: String,
    val contactInfo: String
)
