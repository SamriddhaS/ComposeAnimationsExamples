package com.example.composeanimations

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModel:ViewModel() {

    private val _accelerometerData = mutableStateOf(AccelerometerValues())
    val accelerometerData: AccelerometerValues
        get() = _accelerometerData.value

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