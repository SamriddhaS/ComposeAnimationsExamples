package com.example.composeanimations

class LowPassFilter(private val alpha: Float) {
    private var previousX: Float = 0f
    private var previousY: Float = 0f
    private var previousZ: Float = 0f

    fun apply(x: Float, y: Float, z: Float): Triple<Float, Float, Float> {
        // Apply the low-pass filter formula
        val filteredX = alpha * x + (1 - alpha) * previousX
        val filteredY = alpha * y + (1 - alpha) * previousY
        val filteredZ = alpha * z + (1 - alpha) * previousZ

        // Update previous values for the next iteration
        previousX = filteredX
        previousY = filteredY
        previousZ = filteredZ

        return Triple(filteredX, filteredY, filteredZ)
    }
}