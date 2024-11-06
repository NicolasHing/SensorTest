package com.example.sensortest

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), SensorEventListener {
    // https://developer.android.com/develop/sensors-and-location/sensors/sensors_overview#kotlin

    private lateinit var sensorManager: SensorManager

    // TODO : create null "Sensor" variables (accelerometer, gyroscope ...)
    private var accelerometer: ---
    private var gyroscope: ---
    private var linearAcceleration: ---
    private var magnetometer: ---
    private var proximity: ---
    private var light: ---
    private var pressure: ---



    private lateinit var accelerometerX: TextView
    private lateinit var accelerometerY: TextView
    private lateinit var accelerometerZ: TextView
    private lateinit var gyroscopeX: TextView
    private lateinit var gyroscopeY: TextView
    private lateinit var gyroscopeZ: TextView
    private lateinit var linearAccelerationX: TextView
    private lateinit var linearAccelerationY: TextView
    private lateinit var linearAccelerationZ: TextView
    private lateinit var magnetometerX: TextView
    private lateinit var magnetometerY: TextView
    private lateinit var magnetometerZ: TextView
    private lateinit var proximityView: TextView
    private lateinit var pressureView: TextView
    private lateinit var lightView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // TODO : Initialize sensors. hold CTRL and click on the method
        initializeSensors()

        //
        initializeSensorViews()

        // Display all sensors in Log
        val sensorsList: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        for (sensor in sensorsList) {
            Log.d("SensorList", """
                Name : ${sensor.name}
                Max range : ${sensor.maximumRange}
                Resolution : ${sensor.resolution}
                Power consumption : ${sensor.power} mA
            """.trimIndent())
        }
    }


    override fun onResume() {
        super.onResume()

        // TODO : Register sensors to SensorManager when app is active
        // try different sampling rate in last parameter
        accelerometer?.let { sensorManager.registerListener(this, ---, ---) }
        gyroscope?.let { sensorManager.registerListener(this, ---, ---) }
        linearAcceleration?.let { sensorManager.registerListener(this, ---, ---) }
        magnetometer?.let { sensorManager.registerListener(this, ---, ---) }

        proximity?.let { sensorManager.registerListener(this, ---, ---) }
        light?.let { sensorManager.registerListener(this, ---, ---) }
        pressure?.let { sensorManager.registerListener(this, ---, ---) }

    }

    override fun onPause() {
        super.onPause()
        // Unregister sensors when app is inactive
        sensorManager.---(this)
    }

    // systematically called when a sensor has changed value
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            --- -> {
                val (x, y, z) = // TODO
                accelerometerX.text = getString(R.string.accelerometer_x, x)
                accelerometerY.text = getString(R.string.accelerometer_y, y)
                accelerometerZ.text = getString(R.string.accelerometer_z, z)
            }
            --- -> {
                val (x, y, z) = // TODO
                gyroscopeX.text = getString(R.string.gyroscope_x, x)
                gyroscopeY.text = getString(R.string.gyroscope_y, y)
                gyroscopeZ.text = getString(R.string.gyroscope_z, z)
            }
            --- -> {
                val (x, y, z) = // TODO
                linearAccelerationX.text = getString(R.string.linear_acceleration_x, x)
                linearAccelerationY.text = getString(R.string.linear_acceleration_y, y)
                linearAccelerationZ.text = getString(R.string.linear_acceleration_z, z)
            }
            --- -> {
                val (x, y, z) = // TODO
                magnetometerX.text = getString(R.string.magnetometer_x, x)
                magnetometerY.text = getString(R.string.magnetometer_y, y)
                magnetometerZ.text = getString(R.string.magnetometer_z, z)
            }
            --- -> {
                val distance = // TODO
                proximityView.text = getString(R.string.proximity_value, distance)
            }
            --- -> {
                val illuminance = // TODO
                lightView.text = getString(R.string.light_value, illuminance)
            }
            --- -> {
                val pressure = // TODO
                pressureView.text = getString(R.string.pressure_value, pressure)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // Nothing to do here
        // https://developer.android.com/reference/android/hardware/SensorListener#onAccuracyChanged(int,%20int)
    }


    private fun initializeSensors() {
        // TODO: get default sensors, and save them in this class
        accelerometer = sensorManager.
        gyroscope = sensorManager.
        linearAcceleration = sensorManager.
        magnetometer = sensorManager.
        proximity = sensorManager.
        light = sensorManager.
        pressure = sensorManager.
    }

    private fun initializeSensorViews() {
        accelerometerX = findViewById(R.id.accelerometer_x)
        accelerometerY = findViewById(R.id.accelerometer_y)
        accelerometerZ = findViewById(R.id.accelerometer_z)

        gyroscopeX = findViewById(R.id.gyroscope_x)
        gyroscopeY = findViewById(R.id.gyroscope_y)
        gyroscopeZ = findViewById(R.id.gyroscope_z)

        linearAccelerationX = findViewById(R.id.linear_acceleration_x)
        linearAccelerationY = findViewById(R.id.linear_acceleration_y)
        linearAccelerationZ = findViewById(R.id.linear_acceleration_z)

        magnetometerX = findViewById(R.id.magnetometer_x)
        magnetometerY = findViewById(R.id.magnetometer_y)
        magnetometerZ = findViewById(R.id.magnetometer_z)

        proximityView = findViewById(R.id.proximity)
        pressureView = findViewById(R.id.pressure)
        lightView = findViewById(R.id.light)
    }

}