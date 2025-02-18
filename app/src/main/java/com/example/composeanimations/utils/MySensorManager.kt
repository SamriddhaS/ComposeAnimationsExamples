package com.example.composeanimations.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs

private const val ALPHA = 0.3f
private const val THRESHOLD = 1f

object MySensorManager {

    private var sensorManager: SensorManager?=null
    val lowPassFilter = LowPassFilter(ALPHA)
    var previousFilteredValues = Triple(0f, 0f, 0f)

    fun init(context: Context?){
        if (sensorManager==null) sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    fun registerAccelerometerSensor(
        callBack:(xAxis:Float?,yAxis:Float?,zAxis:Float?)->Unit
    ){

        if (isSensorAvailable(Sensor.TYPE_ACCELEROMETER)){

            val sensorList = sensorManager?.getSensorList(Sensor.TYPE_ACCELEROMETER)?: listOf()
            sensorManager?.registerListener(object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
                        val values = event.values
                        val x = values?.get(0)
                        val y = values?.get(1)
                        val z = values?.get(2)
                        val filteredValues = lowPassFilter.apply(x?:0f,y?:0f,z?:0f)
                        if (checkSignificantChange(filteredValues,previousFilteredValues)){
                            previousFilteredValues = filteredValues
                            callBack(x,y,z)
                        }
                        //callBack(x,y,z)
                    }
                }

                override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                }

            }, sensorList[0], SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun checkSignificantChange(
        current: Triple<Float, Float, Float>,
        previous: Triple<Float, Float, Float>,
    ): Boolean {
        val deltaX = abs(current.first - previous.first)
        val deltaY = abs(current.second - previous.second)
        val deltaZ = abs(current.third - previous.third)

        return deltaX > THRESHOLD || deltaY > THRESHOLD || deltaZ > THRESHOLD
    }

    private fun isSensorAvailable(sensorType:Int):Boolean{
        return sensorManager?.getDefaultSensor(sensorType) != null
    }
}