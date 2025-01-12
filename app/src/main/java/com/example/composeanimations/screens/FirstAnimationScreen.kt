package com.example.composeanimations.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun FirstScreen(modifier: Modifier = Modifier) {
    Box {
        var drawerState by remember {
            mutableStateOf(DrawerState.Closed)
        }
        var screenState by remember {
            mutableStateOf(Screen.Home)
        }

        val coroutineScope = rememberCoroutineScope()

        fun toggleDrawerState() {
            coroutineScope.launch {
                Log.d("First Screen", "toggleDrawer")
                drawerState = if (drawerState == DrawerState.Open) {
                    DrawerState.Closed
                } else {
                    DrawerState.Open
                }
            }
        }

        CustomDrawer(
            selectedScreen = screenState,
            onScreenSelected = { screen ->
                screenState = screen
            }
        )

        val drawerWidth = with(LocalDensity.current) {
            DrawerWidth.toPx()
        }

        MainContent(
            toggleDrawer = { toggleDrawerState() },
            modifier = modifier.graphicsLayer {
                this.translationX = if (drawerState==DrawerState.Open) drawerWidth else 0f
                this.scaleX = if (drawerState==DrawerState.Open) 0.8f else 1f
                this.scaleY = if (drawerState==DrawerState.Open) 0.8f else 1f
                val cornerShape = if(drawerState==DrawerState.Open) 32.dp.toPx() else 0.dp.toPx()
                this.shape = RoundedCornerShape(cornerShape)
                this.clip = true
            }
        )
    }
}

@Composable
fun MainContent(
    toggleDrawer: () -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Cyan)
                .padding(18.dp)
        ) {
            Button(
                onClick = { toggleDrawer() }
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Open Drawer")
            }
        }
    }
}

private enum class DrawerState {
    Open,
    Closed
}

@Composable
private fun CustomDrawer(
    selectedScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Screen.entries.forEach {
            NavigationDrawerItem(
                label = {
                    Text(it.text)
                },
                icon = {
                    Icon(imageVector = it.icon, contentDescription = it.text)
                },
                selected = selectedScreen == it,
                onClick = {
                    onScreenSelected(it)
                },
            )
        }
    }
}

private enum class Screen(val text: String, val icon: ImageVector) {
    Home("Home", Icons.Default.Home),
    SleepDetails("Account", Icons.Default.AccountCircle),
    Leaderboard("Calender", Icons.Default.DateRange),
    Settings("Settings", Icons.Default.Settings),
}

private val DrawerWidth = 250.dp