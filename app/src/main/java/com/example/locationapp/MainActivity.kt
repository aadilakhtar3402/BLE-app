package com.example.locationapp

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.locationapp.Utils.LocationUpdateManager
import com.example.locationapp.Utils.LoggingManagar
import com.example.locationapp.presentation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val REQUEST_LOCATION = 1
    lateinit var btnGetLocation: Button
    private lateinit var showLocation: TextView
    private lateinit var locationManager: LocationManager
    private lateinit var locationUpdateManager: LocationUpdateManager
    private lateinit var loggingManagar: LoggingManagar
    @Inject lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        setContent {
            Navigation(
                onBluetoothStateChanged = {
                    showBluetoothDialog()
                }
            )
        }

        /*ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION
        )*/

        //locationUpdateManager = LocationUpdateManager(this, REQUEST_LOCATION)
       // loggingManagar = LoggingManagar(this, locationUpdateManager)
       // locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        /*showLocation = findViewById(R.id.showLocation)
        btnGetLocation = findViewById(R.id.btnGetLocation)



        btnGetLocation.setOnClickListener {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                onGPS()
            } else {
                loggingManagar.toggleLogging()
            }
        }*/
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

    override fun onStart() {
        super.onStart()
        showBluetoothDialog()
    }

    private var isBluetoothDialogAlreadyShown = false
    private fun showBluetoothDialog(){
        if (!bluetoothAdapter.isEnabled) {
            if (!isBluetoothDialogAlreadyShown) {
                val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startBluetoothIntentForResult.launch(enableBluetoothIntent)
                isBluetoothDialogAlreadyShown = true
            }
        }
    }

    private val startBluetoothIntentForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            isBluetoothDialogAlreadyShown = false
            if (result.resultCode != Activity.RESULT_OK) {
                showBluetoothDialog()
            }
        }

    /*override fun onDestroy() {
        super.onDestroy()
        loggingManagar.saveLogs(locationLogs)
    }*/

}

