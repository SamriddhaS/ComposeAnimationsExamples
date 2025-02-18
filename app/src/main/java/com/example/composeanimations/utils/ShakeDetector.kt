package com.example.composeanimations.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.composeanimations.AccelerometerValues
import kotlin.math.pow
import kotlin.math.sqrt

class ShakeDetector {
    private companion object {
        const val SHAKE_THRESHOLD_GRAVITY = 2.7f          // In "Gs" (one Earth gravity unit)

        const val SHAKE_SLOP_TIME = 2500                   // In [ms]
        const val SHAKE_COUNT_RESET_TIME = 3000
    }

    private var shakeListener: (() -> Unit)? = null      // argument - shakes count in a row
    private var shakeTimestamp: Long = 0
    private var shakesCount: Int = 0

    fun setOnShakeListener(listener: (() -> Unit)?) {
        shakeListener = listener
        if(listener == null) {
            shakesCount = 0
        }
    }

    fun onSensorChanged(accelerometerValues: AccelerometerValues) {
        val gX = (accelerometerValues.xAxis / SensorManager.GRAVITY_EARTH).toDouble()
        val gY =  accelerometerValues.yAxis / SensorManager.GRAVITY_EARTH
        val gZ = accelerometerValues.zAxis / SensorManager.GRAVITY_EARTH

        // gForce will be close to 1 when there is no movement.
        val gForce = sqrt(gX.pow(2) + gY.pow(2) + gZ.pow(2))

        //Log.d("ShakeDetector", "onSensorChanged gForce: $gForce")
        if (gForce > SHAKE_THRESHOLD_GRAVITY) {
            //Log.d("ShakeDetector", "onSensorChanged: true")

            val now = System.currentTimeMillis()

            // ignore shake events too close to each other (500ms)
            if (shakeTimestamp + SHAKE_SLOP_TIME > now) {
                return
            }

            // reset the shake count after 3 seconds of no shakes
//            if (shakeTimestamp + SHAKE_COUNT_RESET_TIME < now) {
//                shakesCount = 0
//            }

            shakeTimestamp = now
            shakesCount++
            Log.d("MainViewModel", "onSensorChanged: $shakesCount")
            shakeListener?.invoke()
        }
    }



}