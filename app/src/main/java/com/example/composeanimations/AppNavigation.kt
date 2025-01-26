package com.example.composeanimations

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeanimations.screens.AccelerometerRotateScreen
import com.example.composeanimations.screens.AnimatedDrawerScreen
import com.example.composeanimations.screens.AnimatedDrawerUsingAnchorScreen
import com.example.composeanimations.screens.SimpleAnimationsScreen
import com.example.composeanimations.screens.CardsAnimationScreen
import com.example.composeanimations.screens.HomeScreen
import com.example.composeanimations.screens.SimpleAnimationsScreenTwo
import com.example.composeanimations.ui.theme.ComposeAnimationsTheme

object Routes{
    const val HOME_SCREEN = "home_screen"
    const val ANIMATED_DRAWER_SCREEN = "animated_drawer_screen"
    const val ANIMATED_DRAWER_WITH_ANCHOR_SCREEN = "animated_drawer_with_anchor_screen"
    const val SIMPLE_ANIMATIONS_SCREEN = "animation_spec_screen"
    const val SIMPLE_ANIMATIONS_SCREEN_2 = "animation_spec_screen_2"
    const val CARD_ANIMATIONS_SCREEN = "card_animation_screen"
    const val ROTATION_USING_ACCELEROMETER = "ROTATION_USING_ACCELEROMETER"
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainViewModel,
    context:Context?,
    modifier: Modifier
    ) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME_SCREEN
    ){

        composable(route = Routes.HOME_SCREEN){
            HomeScreen(
                navigateTo = { route -> navController.navigate(route) },
                modifier = modifier
            )
        }

        composable(route = Routes.ANIMATED_DRAWER_SCREEN){
            AnimatedDrawerScreen (
                modifier = modifier
            )
        }

        composable(route = Routes.ANIMATED_DRAWER_WITH_ANCHOR_SCREEN){
            AnimatedDrawerUsingAnchorScreen (
                modifier = modifier
            )
        }

        composable(route = Routes.SIMPLE_ANIMATIONS_SCREEN){
            SimpleAnimationsScreen (
                modifier = modifier
            )
        }

        composable(route = Routes.SIMPLE_ANIMATIONS_SCREEN_2){
            SimpleAnimationsScreenTwo (
                modifier = modifier
            )
        }

        composable(route = Routes.CARD_ANIMATIONS_SCREEN){
            CardsAnimationScreen (
                modifier = modifier
            )
        }

        composable(route = Routes.ROTATION_USING_ACCELEROMETER){
            AccelerometerRotateScreen (
                mainViewModel = mainViewModel,
                context = context,
                modifier = modifier
            )
        }

    }
}