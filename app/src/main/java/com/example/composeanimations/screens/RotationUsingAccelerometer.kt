package com.example.composeanimations.screens

import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.composeanimations.MainViewModel
import com.example.composeanimations.utils.MySensorManager
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AccelerometerRotateScreen(
    mainViewModel: MainViewModel,
    context: Context?,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(Unit) {
        MySensorManager.init(context)
        MySensorManager.registerAccelerometerSensor{
            xAxis, yAxis, zAxis ->
            mainViewModel.setAccelerometerData(xAxis,yAxis,zAxis)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val globeModifier = Modifier
                .aspectRatio(1f)
                .fillMaxSize()
                .padding(16.dp)
//                .graphicsLayer {
//                    rotationX = mainViewModel.accelerometerData.xAxis
//                    rotationY = mainViewModel.accelerometerData.yAxis
//                    rotationZ = mainViewModel.accelerometerData.zAxis
//                    //translationX = mainViewModel.accelerometerData.yAxis *-10
//                    //translationY = mainViewModel.accelerometerData.xAxis *-10
//                }

            RotatingGlobe(mainViewModel,globeModifier)

//            Box(
//                modifier =
//                Modifier.size(100.dp)
//                    .graphicsLayer {
//                        rotationX = mainViewModel.accelerometerData.xAxis
//                        rotationY = mainViewModel.accelerometerData.yAxis
//                        rotationZ = mainViewModel.accelerometerData.zAxis
//                        //translationX = mainViewModel.accelerometerData.yAxis *-10
//                        //translationY = mainViewModel.accelerometerData.xAxis *-10
//                    }.background(Color.White)
//            )


            Row(Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "X = ${mainViewModel.accelerometerData.xAxis}")
                Text(text = "Y = ${mainViewModel.accelerometerData.yAxis}")
                Text(text = "Z = ${mainViewModel.accelerometerData.zAxis}")
            }
        }

    }

}

private const val NUM_DOTS = 1500
private const val GLOBE_RADIUS_FACTOR = 0.7f
private const val DOT_RADIUS_FACTOR = 0.005f
private const val FIELD_OF_VIEW_FACTOR = 0.8f
const val TWO_PI = 2 * PI

@Composable
fun RotatingGlobe(
    mainViewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val animatedProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

//    LaunchedEffect(mainViewModel.accelerometerData.yAxis) {
//        coroutineScope.launch {
//            val lerpY = lerp(0f,1f,mainViewModel.accelerometerData.yAxis)
//            animatedProgress.animateTo(
//                targetValue = lerpY,
//                animationSpec = tween(500),
//            )
//        }
//    }

    val dotColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val dotInfos = remember {
        (0 until NUM_DOTS).map {
            val azimuthAngle = acos((Math.random().toFloat() * 2) - 1)
            val polarAngle = Math.random().toFloat() * TWO_PI
            DotInfo(azimuthAngle, polarAngle)
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {

        coroutineScope.launch {
            val lerpY = lerp(0f,10f,mainViewModel.accelerometerData.yAxis)
            animatedProgress.animateTo(
                targetValue = lerpY,
                animationSpec = tween(100),
            )
        }

        // Compute the animated rotation about the y-axis in radians.
        val rotationY = animatedProgress.value

        dotInfos.forEach {
            val minSize = size.minDimension
            val globeRadius = minSize * GLOBE_RADIUS_FACTOR

            // Calculate the dot's coordinates in 3D space.
            val x = globeRadius * sin(it.azimuthAngle) * cos(it.polarAngle)
            val y = globeRadius * sin(it.azimuthAngle) * sin(it.polarAngle)
            val z = globeRadius * cos(it.azimuthAngle) - globeRadius

            // Rotate the dot's 3D coordinates about the y-axis.
            val rotatedX = cos(rotationY) * x + sin(rotationY) * (z + globeRadius)
            val rotatedZ = -sin(rotationY) * x + cos(rotationY) * (z + globeRadius) - globeRadius

            // Project the rotated 3D coordinates onto the 2D plane.
            val fieldOfView = minSize * FIELD_OF_VIEW_FACTOR
            val projectedScale = fieldOfView / (fieldOfView - rotatedZ)
            val projectedX = (rotatedX * projectedScale) + minSize / 2f
            val projectedY = (y * projectedScale) + minSize / 2f

            // Scale the dot such that dots further away from the camera appear smaller.
            val dotRadius = minSize * DOT_RADIUS_FACTOR
            val scaledDotRadius = dotRadius * projectedScale

            drawCircle(
                color = dotColor,
                radius = scaledDotRadius.toFloat(),
                center = Offset(projectedX.toFloat(), projectedY.toFloat()),
            )
        }
    }
}

private data class DotInfo(val azimuthAngle: Float, val polarAngle: Double)


