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
    private lateinit var sensorManager: SensorManager

    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private var linearAcceleration: Sensor? = null
    private var magnetometer: Sensor? = null
    private var proximity: Sensor? = null
    private var pressure: Sensor? = null
    private var light: Sensor? = null


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

        initializeSensors()
        initializeSensorViews()

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

        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        gyroscope?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        linearAcceleration?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
        magnetometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST) }

        proximity?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        light?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        pressure?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }

    }

    override fun onPause() {
        super.onPause()
        // Disable sensors when app is not active
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val (x, y, z) = event.values
                accelerometerX.text = getString(R.string.accelerometer_x, x)
                accelerometerY.text = getString(R.string.accelerometer_y, y)
                accelerometerZ.text = getString(R.string.accelerometer_z, z)
            }
            Sensor.TYPE_GYROSCOPE -> {
                val (x, y, z) = event.values
                gyroscopeX.text = getString(R.string.gyroscope_x, x)
                gyroscopeY.text = getString(R.string.gyroscope_y, y)
                gyroscopeZ.text = getString(R.string.gyroscope_z, z)
            }
            Sensor.TYPE_LINEAR_ACCELERATION -> {
                val (x, y, z) = event.values
                linearAccelerationX.text = getString(R.string.linear_acceleration_x, x)
                linearAccelerationY.text = getString(R.string.linear_acceleration_y, y)
                linearAccelerationZ.text = getString(R.string.linear_acceleration_z, z)
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                val (x, y, z) = event.values
                magnetometerX.text = getString(R.string.magnetometer_x, x)
                magnetometerY.text = getString(R.string.magnetometer_y, y)
                magnetometerZ.text = getString(R.string.magnetometer_z, z)
            }
            Sensor.TYPE_PROXIMITY -> {
                val distance = event.values[0]
                proximityView.text = getString(R.string.proximity_value, distance)
            }
            Sensor.TYPE_LIGHT -> {
                val illuminance = event.values[0]
                lightView.text = getString(R.string.light_value, illuminance)
            }
            Sensor.TYPE_PRESSURE -> {
                val pressure = event.values[0]
                pressureView.text = getString(R.string.pressure_value, pressure)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //
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