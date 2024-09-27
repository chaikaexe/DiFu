package com.example.demo1

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.widget.EditText
import android.widget.Toast
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var searchField: EditText
    private val markers = mutableListOf<Marker>() // список всех маркеров для поиска
    private lateinit var barview: View
    private val foodMarkers = mutableListOf<Marker>()
    private val gymMarkers = mutableListOf<Marker>()
    private var foodVisible = false
    private var gymVisible = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient



    private var currentMarker: Marker? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        val dbHelper = DBHelper(this)
        dbHelper.createDatabase()

        searchField = findViewById(R.id.poisk) // Поиск по полю

        searchField.setOnEditorActionListener { _, _, _ ->
            val query = searchField.text.toString()
            searchMarker(query)
            true
        }

        barview = findViewById(R.id.barview)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getLocationPermission()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val layoutParams = barview.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topMargin = 100
        barview.layoutParams = layoutParams

        val foodCardView = findViewById<androidx.cardview.widget.CardView>(R.id.eda)
        foodCardView.setOnClickListener {
            togglePlacesVisibility("food")
        }

        val gymCardView = findViewById<androidx.cardview.widget.CardView>(R.id.zal)
        gymCardView.setOnClickListener {
            togglePlacesVisibility("gym")
        }

        val achievementsCardView = findViewById<androidx.cardview.widget.CardView>(R.id.dostijania)
        achievementsCardView.setOnClickListener {
            Log.d("MainActivity", "Button dostijania clicked")
            startActivity(Intent(this, AchievementsActivity2::class.java))
        }

        val showLocationButton = findViewById<androidx.cardview.widget.CardView>(R.id.mesto)
        showLocationButton.setOnClickListener {
            getDeviceLocation()
        }


        val plusButton = findViewById<View>(R.id.plus)
        val minusButton = findViewById<View>(R.id.minus)

        plusButton.setOnClickListener {
            zoomIn()
        }

        minusButton.setOnClickListener {
            zoomOut()
        }

        val menuButton = findViewById<androidx.cardview.widget.CardView>(R.id.pro)
        menuButton.setOnClickListener {
            val intent = Intent(this, MenuIkonko::class.java)
            startActivity(intent)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.isBuildingsEnabled = false
        mMap.isIndoorEnabled = false
        mMap.isTrafficEnabled = false
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = false

        // Перемещаем камеру на Киров
        val kirovLocation = LatLng(58.6056, 49.6615) // координаты Кирова
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kirovLocation, 12f)) // 12f - уровень зума

        addMarkers()

        mMap.setOnMarkerClickListener { marker ->
            val placeName = marker.title ?: return@setOnMarkerClickListener false

            val intent = Intent(this, CartochaActivity2::class.java)
            intent.putExtra("PLACE_NAME", placeName) // Передаем название места
            startActivity(intent)

            true // Обработано
        }

        searchField.setOnEditorActionListener { _, _, _ ->
            val query = searchField.text.toString()
            searchMarker(query)
            true
        }

        getLocationPermission()
    }

    private fun addMarkers() {
        val marker1 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.596513, 49.591684))
                .title("Ё/батон")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker2 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.599058, 49.645079))
                .title("Eat For Day")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker3 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.599660, 49.656269))
                .title("Царское село")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker4 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.604260090433414, 49.65691012708583))
                .title("Gym Station")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker5 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.639890, 49.630302))
                .title("Ski Club")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )

        val marker6 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.628583, 49.632242))
                .title("ProSport")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker7 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.614514, 49.675837))
                .title("Самое популярное кафе Восток")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker8 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.586439, 49.657303))
                .title("Spartak")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker9 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.584952, 49.623389))
                .title("Arena Record gym")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker10 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.582729, 49.631874))
                .title("Море парк")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker11 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.596621, 49.605877))
                .title("Modo Gym")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker12 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.590312, 49.603535))
                .title("Fitness family time")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker13 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.570668, 49.673299))
                .title("Южный")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker14 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.590462, 49.641700))
                .title("Монро-Рассвет")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker15 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.603711, 49.675170))
                .title("Siberian Wellness")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker16 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.587719, 49.675891))
                .title("Живое масло")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker17 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.611451, 49.679329))
                .title("Полезные продукты")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker18 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.600031, 49.668707))
                .title("Чайхана 84/46")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker19 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.600192, 49.684202))
                .title("Брокколи")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )
        val marker20 = mMap.addMarker(
            MarkerOptions().position(LatLng(58.603249, 49.680767))
                .title("Сулугуни")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mesto))
        )



        if (marker1 != null) {
            foodMarkers.add(marker1)
            marker1.isVisible = false
        }

        if (marker2 != null) {
            foodMarkers.add(marker2)
            marker2.isVisible = false
        }

        if (marker3 != null) {
            foodMarkers.add(marker3)
            marker3.isVisible = false
        }

        if (marker4 != null) {
            gymMarkers.add(marker4)
            markers.add(marker4)
            marker4.isVisible = false
        }

        if (marker5 != null) {
            gymMarkers.add(marker5)
            markers.add(marker5)
            marker5.isVisible = false
        }

        if (marker6 != null) {
            gymMarkers.add(marker6)
            markers.add(marker6)
            marker6.isVisible = false
        }

        if (marker7 != null) {
            foodMarkers.add(marker7)
            marker7.isVisible = false
        }

        if (marker8 != null) {
            gymMarkers.add(marker8)
            markers.add(marker8)
            marker8.isVisible = false
        }

        if (marker9 != null) {
            gymMarkers.add(marker9)
            markers.add(marker9)
            marker9.isVisible = false
        }

        if (marker10 != null) {
            gymMarkers.add(marker10)
            markers.add(marker10)
            marker10.isVisible = false
        }

        if (marker11 != null) {
            gymMarkers.add(marker11)
            markers.add(marker11)
            marker11.isVisible = false
        }

        if (marker12 != null) {
            gymMarkers.add(marker12)
            markers.add(marker12)
            marker12.isVisible = false
        }

        if (marker13 != null) {
            gymMarkers.add(marker13)
            markers.add(marker13)
            marker13.isVisible = false
        }

        if (marker14 != null) {
            gymMarkers.add(marker14)
            markers.add(marker14)
            marker14.isVisible = false
        }

        if (marker15 != null) {
            foodMarkers.add(marker15)
            marker15.isVisible = false
        }

        if (marker16 != null) {
            foodMarkers.add(marker16)
            marker16.isVisible = false
        }

        if (marker17 != null) {
            foodMarkers.add(marker17)
            marker17.isVisible = false
        }

        if (marker18 != null) {
            foodMarkers.add(marker18)
            marker18.isVisible = false
        }

        if (marker19 != null) {
            foodMarkers.add(marker19)
            marker19.isVisible = false
        }

        if (marker20 != null) {
            foodMarkers.add(marker20)
            marker20.isVisible = false
        }
    }

    private fun searchMarker(query: String) {
        // Поиск маркера по названию
        for (marker in markers) {
            if (marker.title?.contains(query, true) == true) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 15f))
                Toast.makeText(this, "Точка найдена: ${marker.title}", Toast.LENGTH_SHORT).show()
                return
            }
        }
        Toast.makeText(this, "Точка не найдена", Toast.LENGTH_SHORT).show()
    }

    private fun zoomIn() {
        mMap.animateCamera(CameraUpdateFactory.zoomIn())
    }

    private fun zoomOut() {
        mMap.animateCamera(CameraUpdateFactory.zoomOut())
    }

    private fun togglePlacesVisibility(category: String) {
        when (category) {
            "food" -> {
                foodVisible = !foodVisible
                for (marker in foodMarkers) {
                    marker.isVisible = foodVisible
                }
                gymVisible = false
                for (marker in gymMarkers) {
                    marker.isVisible = gymVisible
                }
            }

            "gym" -> {
                gymVisible = !gymVisible
                for (marker in gymMarkers) {
                    marker.isVisible = gymVisible
                }
                foodVisible = false
                for (marker in foodMarkers) {
                    marker.isVisible = foodVisible
                }
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getDeviceLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val currentLatLng = LatLng(location.latitude, location.longitude)

                        // Создайте Bitmap из ресурса
                        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.gps)

                        // Установите фиксированный размер для иконки
                        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 60, 60, false)

                        // Создайте BitmapDescriptor из масштабированного Bitmap
                        val icon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)

                        currentMarker?.remove()

                        // Добавьте маркер с фиксированным размером иконки
                        currentMarker = mMap.addMarker(
                            MarkerOptions()
                                .position(currentLatLng)
                                .title("Ваше местоположение")
                                .icon(icon)
                        )
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    } else {
                        Log.d("MainActivity", "Не удалось получить местоположение")
                        Toast.makeText(this, "Не удалось получить местоположение", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("MainActivity", "SecurityException: ${e.message}")
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceLocation()
            } else {
                Log.e("MainActivity", "Разрешение на доступ к геопозиции отклонено.")
            }
        }
    }

    private fun drawRoute(origin: LatLng, destination: LatLng) {
        val url = getDirectionUrl(origin, destination)
        val client = OkHttpClient()

        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "Ошибка маршрута: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val data = response.body?.string()
                val route = parseRoute(data)
                runOnUiThread {
                    mMap.addPolyline(PolylineOptions().addAll(route).width(10f).color(ContextCompat.getColor(this@MainActivity, R.color.routeColor)))
                }
            }
        })
    }

    private fun getDirectionUrl(origin: LatLng, destination: LatLng): String {
        val originParam = "origin=${origin.latitude},${origin.longitude}"
        val destinationParam = "destination=${destination.latitude},${destination.longitude}"
        val key = "AIzaSyDrFXYHFR21NPDYHzsudZI8wWT3fearBuU" // Replace with your actual API key
        return "https://maps.googleapis.com/maps/api/directions/json?$originParam&$destinationParam&key=$key"
    }

    private fun parseRoute(jsonData: String?): List<LatLng> {
        val result = mutableListOf<LatLng>()
        try {
            val jsonObject = JSONObject(jsonData)
            val routes = jsonObject.getJSONArray("routes")
            if (routes.length() > 0) {
                val legs = routes.getJSONObject(0).getJSONArray("legs")
                val steps = legs.getJSONObject(0).getJSONArray("steps")
                for (i in 0 until steps.length()) {
                    val step = steps.getJSONObject(i)
                    val polyline = step.getJSONObject("polyline").getString("points")
                    result.addAll(decodePolyline(polyline))
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Ошибка парсинга маршрута: ${e.message}")
        }
        return result
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val poly = mutableListOf<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng(lat / 1E5, lng / 1E5)
            poly.add(latLng)
        }

        return poly
    }
}
