package com.example.locationapp.Utils

import android.os.Environment
import com.example.locationapp.MainActivity
import com.example.locationapp.data.LocationData
import com.google.gson.Gson
import java.io.File
import java.io.FileOutputStream

class LoggingManagar(private val activity: MainActivity, private val locationUpdateManager: LocationUpdateManager) {

    private var isLogging = false
    private val LOG_FILENAME = "location_logs.json"

    fun toggleLogging() {
        if (isLogging) {
            stopLogging()
        } else {
            startLogging()
        }
    }

    private fun startLogging() {
        // Start logging
        isLogging = true
        activity.btnGetLocation.text = "Stop"
        // Start location updates or any other logging process
        locationUpdateManager.startLocationUpdates()
    }

    private fun stopLogging() {
        // Stop logging
        isLogging = false
        activity.btnGetLocation.text = "Start"
        // Stop location updates or any other logging process
        locationUpdateManager.stopLocationUpdates()
        saveLocationData(locationUpdateManager.getLocationDataList())
    }

    // Method to save logs to a file in internal storage
    /*fun saveLogs(locationLogs: List<LocationData>) {
        val json = Gson().toJson(locationLogs)
        try {
            val file = File(activity.filesDir, LOG_FILENAME)
            val writer = FileWriter(file)
            writer.use {
                it.write(json)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }*/

    // Method to load logs from a file in internal storage
    /*fun loadLogs(): List<LocationData> {
        val file = File(activity.filesDir, LOG_FILENAME)
        return if (file.exists()) {
            try {
                val json = file.readText()
                Gson().fromJson(json, object : TypeToken<List<LocationData>>() {}.type)
            } catch (e: IOException) {
                e.printStackTrace()
                emptyList()
            }
        } else {
            emptyList()
        }
    }*/

    fun saveLocationData(locationDataList: List<LocationData>) {
        val gson = Gson()
        val jsonData = gson.toJson(locationDataList)

        val fileName = "location_data.json"
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        try {
            val file = File(folder, fileName)
            val fos = FileOutputStream(file)
            fos.write(jsonData.toByteArray())
            fos.close()
            // Optionally, you can show a toast message or log the file path
            // Toast.makeText(context, "Location data saved: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle any exceptions that occur during file writing
        }
    }
}