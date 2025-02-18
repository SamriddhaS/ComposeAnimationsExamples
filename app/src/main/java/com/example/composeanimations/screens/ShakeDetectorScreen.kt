package com.example.composeanimations.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.composeanimations.MainViewModel
import com.example.composeanimations.utils.FlashLightManager
import com.example.composeanimations.utils.MySensorManager

@Composable
fun ShakeDetectorScreen(
    mainViewModel: MainViewModel,
    context: Context?,
    modifier: Modifier = Modifier) {

    val shakeEvent by mainViewModel.shakeEvent.collectAsState()

    LaunchedEffect(Unit) {
        MySensorManager.init(context)
        MySensorManager.registerAccelerometerSensor{
                xAxis, yAxis, zAxis ->
            mainViewModel.setAccelerometerDataForShakeDetection(xAxis,yAxis,zAxis)
        }
        FlashLightManager.init(context)
    }

    LaunchedEffect(shakeEvent) {
        FlashLightManager.toggleFlashLight(shakeEvent)
    }

    Column(modifier = modifier
        .fillMaxSize()
        .background(if (shakeEvent) Color.Yellow else Color.Black)
        .padding(18.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            text = "Flash Light is On : $shakeEvent",
            color = Color.Red
        )
        Button(onClick = {
            mainViewModel.setShakeEvent(!shakeEvent)
        }) {
            Text(
                text = "Toggle Flash",
                color = Color.White
            )
        }
    }
}