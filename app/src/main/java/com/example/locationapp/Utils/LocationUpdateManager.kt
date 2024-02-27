package com.example.locationapp.Utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.locationapp.MainActivity
import com.example.locationapp.R
import com.example.locationapp.dataclasses.LocationData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class LocationUpdateManager(private val activity: MainActivity, private val REQUEST_LOCATION: Int) {

    private lateinit var locationManager: LocationManager
    private var showLocation: TextView? = null

    init {
        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        showLocation = activity.findViewById(R.id.showLocation)
    }

    private var timer: Timer? = null
    private val UPDATE_INTERVAL: Long = 10000 // 10 seconds
    private val locationDataList = mutableListOf<LocationData>()

    fun startLocationUpdates() {
        // Cancel any existing timer
        stopLocationUpdates()

        // Create a new timer
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                fetchLocation()
            }
        }, 0, UPDATE_INTERVAL)
    }

    fun stopLocationUpdates() {
        timer?.cancel()
        timer?.purge()
        timer = null
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
        } else {
            val locationGPS: Location? =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (locationGPS != null) {
                val lat: Double = locationGPS.latitude
                val longi: Double = locationGPS.longitude
                val currentTimeMillis = System.currentTimeMillis()
                val currentTime = SimpleDateFormat(
                    "HH:mm:ss",
                    Locale.getDefault()
                ).format(Date(currentTimeMillis))

                // Store the location data or update UI as needed
                // For example, you can append the data to a list or display it in a TextView
                // Example: Append data to a list
                locationDataList.add(LocationData(lat, longi, currentTime))

                // Example: Display data in a TextView
                activity.runOnUiThread {
                    showLocation?.append("Latitude: $lat, Longitude: $longi, Time: $currentTime\n")
                }
            } else {
                activity.runOnUiThread {
                    Toast.makeText(activity, "Unable to find location.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getLocationDataList(): List<LocationData> {
        return locationDataList
    }
}