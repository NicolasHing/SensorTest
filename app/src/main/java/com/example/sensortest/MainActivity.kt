package com.example.sensortest

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority


class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private var linearAcceleration: Sensor? = null
    private var magnetometer: Sensor? = null
    private var proximity: Sensor? = null
    private var pressure: Sensor? = null
    private var light: Sensor? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private lateinit var locationCallback: LocationCallback

    private var accelerometerValues by mutableStateOf(Triple(0f, 0f, 0f))
    private var gyroscopeValues by mutableStateOf(Triple(0f, 0f, 0f))
    private var linearAccelerationValues by mutableStateOf(Triple(0f, 0f, 0f))
    private var magnetometerValues by mutableStateOf(Triple(0f, 0f, 0f))
    private var proximityValue by mutableFloatStateOf(0f)
    private var pressureValue by mutableFloatStateOf(0f)
    private var lightValue by mutableFloatStateOf(0f)

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startLocationUpdates()
        } else {
            Toast.makeText(this, "GPS permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        initializeSensors()

        setContent {
            SensorDisplayScreen(
                accelerometerValues = accelerometerValues,
                gyroscopeValues = gyroscopeValues,
                linearAccelerationValues = linearAccelerationValues,
                magnetometerValues = magnetometerValues,
                proximityValue = proximityValue,
                pressureValue = pressureValue,
                lightValue = lightValue,
                gpsLocation = currentLocation
            )
        }
    }

    private fun checkLocationPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                startLocationUpdates()
            }
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) -> {
                startLocationUpdates()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun initializeSensors() {
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        linearAcceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    }

    // Register listeners at startup and on resume
    override fun onResume() {
        super.onResume()
        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        gyroscope?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        linearAcceleration?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
        magnetometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST) }
        proximity?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        light?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        pressure?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        startLocationUpdates()
    }

    // Unregister listeners on pause
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    accelerometerValues = Triple(event.values[0], event.values[1], event.values[2])
                }
                Sensor.TYPE_GYROSCOPE -> {
                    gyroscopeValues = Triple(event.values[0], event.values[1], event.values[2])
                }
                Sensor.TYPE_LINEAR_ACCELERATION -> {
                    linearAccelerationValues = Triple(event.values[0], event.values[1], event.values[2])
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    magnetometerValues = Triple(event.values[0], event.values[1], event.values[2])
                }
                Sensor.TYPE_PROXIMITY -> {
                    proximityValue = if (event.values[0] == 0f) 1f else 0f
                }
                Sensor.TYPE_PRESSURE -> {
                    pressureValue = event.values[0]
                }
                Sensor.TYPE_LIGHT -> {
                    lightValue = event.values[0]
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        // Create a location request
        val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .build()
        // Create a location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    currentLocation = location
                }
            }
        }
        // Request location updates
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper() // Use the main looper to update the UI
        )
    }

}

@Composable
fun SensorDisplayScreen(
    accelerometerValues: Triple<Float, Float, Float>,
    gyroscopeValues: Triple<Float, Float, Float>,
    linearAccelerationValues: Triple<Float, Float, Float>,
    magnetometerValues: Triple<Float, Float, Float>,
    proximityValue: Float,
    pressureValue: Float,
    lightValue: Float,
    gpsLocation: Location?
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        SensorDataSection(title = "Accelerometer (m/s^2)", values = accelerometerValues)
        SensorDataSection(title = "Gyroscope (rad/s)", values = gyroscopeValues)
        SensorDataSection(title = "Linear Acceleration (m/s^2)", values = linearAccelerationValues)
        SensorDataSection(title = "Magnetometer (μT)", values = magnetometerValues)

        SensorDataSingleValue(title = "Proximity", value = proximityValue)
        SensorDataSingleValue(title = "Pressure (hPa)", value = pressureValue)
        SensorDataSingleValue(title = "Light (lux)", value = lightValue)

        SensorDataLocation(gpsLocation)
    }
}

@Composable
fun SensorDataSection(title: String, values: Triple<Float, Float, Float>) {
    Column {
        Text(text = title, fontSize = 18.sp, style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "X: ${values.first}", fontSize = 16.sp)
        Text(text = "Y: ${values.second}", fontSize = 16.sp)
        Text(text = "Z: ${values.third}", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun SensorDataSingleValue(title: String, value: Float) {
    Column {
        Text(text = title, fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
        Text(text = "value: $value", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun SensorDataLocation(gpsLocation: Location?) {
    Column {
        Text(text = "GPS Location", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        gpsLocation?.let { location ->
            Text("Latitude: ${location.latitude}", fontSize = 16.sp)
            Text("Longitude: ${location.longitude}", fontSize = 16.sp)
            Text("Accuracy: ${location.accuracy}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }  ?: run {
            Text("Permission denied, or waiting for location", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}