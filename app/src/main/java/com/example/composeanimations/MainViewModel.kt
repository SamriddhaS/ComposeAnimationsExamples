package com.example.composeanimations

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.composeanimations.utils.ShakeDetector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel:ViewModel() {

    private val _accelerometerData = mutableStateOf(AccelerometerValues())
    val accelerometerData: AccelerometerValues
        get() = _accelerometerData.value

    private var shakeDetector:ShakeDetector? = null

    private val _shakeEvent = MutableStateFlow(false) // Default value
    val shakeEvent: StateFlow<Boolean> = _shakeEvent

    init {
        shakeDetector = ShakeDetector()
        shakeDetector?.setOnShakeListener {
            _shakeEvent.value = !_shakeEvent.value
        }
    }

    fun setShakeEvent(value: Boolean) {
        _shakeEvent.value = value
    }

    fun setAccelerometerData(
        xAxis: Float?,
        yAxis: Float?,
        zAxis: Float?
    ) {
        _accelerometerData.value = AccelerometerValues(
            xAxis = xAxis?:0f,
            yAxis = yAxis?:0f,
            zAxis = zAxis?:0f
        )
    }

    fun setAccelerometerDataForShakeDetection(
        xAxis: Float?,
        yAxis: Float?,
        zAxis: Float?
    ){
        val latestValue = AccelerometerValues(
            xAxis = xAxis?:0f,
            yAxis = yAxis?:0f,
            zAxis = zAxis?:0f
        )
        _accelerometerData.value = latestValue
        shakeDetector?.onSensorChanged(accelerometerValues = latestValue)
    }

}

class MainViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

data class AccelerometerValues(
    val xAxis:Float = 0f,
    val yAxis:Float = 0f,
    val zAxis:Float = 0f,
)