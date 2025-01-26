package com.example.composeanimations

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composeanimations.ui.theme.ComposeAnimationsTheme
import java.io.Console

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeAnimationsTheme {
                val welcomeViewModel:MainViewModel = viewModel(factory = MainViewModelFactory())
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        mainViewModel = welcomeViewModel,
                        context = this@MainActivity,
                        modifier=Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeAnimationsTheme {
        AppNavHost(
            mainViewModel = MainViewModel(),
            context = null,
            modifier = Modifier.padding(16.dp)
        )
    }
}