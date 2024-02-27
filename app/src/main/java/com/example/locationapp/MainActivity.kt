package com.example.locationapp

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.locationapp.Utils.LocationUpdateManager
import com.example.locationapp.Utils.LoggingManagar
import com.example.locationapp.dataclasses.LocationData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    private val REQUEST_LOCATION = 1
    lateinit var btnGetLocation: Button
    private lateinit var showLocation: TextView
    private lateinit var locationManager: LocationManager
    private lateinit var locationUpdateManager: LocationUpdateManager
    private lateinit var loggingManagar: LoggingManagar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION
        )

        locationUpdateManager = LocationUpdateManager(this, REQUEST_LOCATION)
        loggingManagar = LoggingManagar(this, locationUpdateManager)

        showLocation = findViewById(R.id.showLocation)
        btnGetLocation = findViewById(R.id.btnGetLocation)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        btnGetLocation.setOnClickListener {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                onGPS()
            } else {
                loggingManagar.toggleLogging()
            }
        }
    }

    private fun onGPS() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Enable GPS")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, which ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("No") { dialog, which -> dialog.cancel() }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun updateLocationData(lat: Double, longi: Double, time: String) {
        showLocation.append("Latitude: $lat, Longitude: $longi, Time: $time\n")
    }

    /*override fun onDestroy() {
        super.onDestroy()
        loggingManagar.saveLogs(locationLogs)
    }*/

}

