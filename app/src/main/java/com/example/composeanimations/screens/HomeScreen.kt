package com.example.composeanimations.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composeanimations.Routes

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navigateTo:(route:String)->Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Button(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
            , onClick = {navigateTo(Routes.ANIMATED_DRAWER_SCREEN)}
        ) {
            Text(text = "Animated Drawer")
        }

        Button(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
            , onClick = {navigateTo(Routes.ANIMATED_DRAWER_WITH_ANCHOR_SCREEN)}
        ) {
            Text(text = "Animated Drawer With Anchor")
        }

        Button(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
            , onClick = {navigateTo(Routes.SIMPLE_ANIMATIONS_SCREEN)}
        ) {
            Text(text = "Basic Animations")
        }
    }
}