package com.example.composeanimations

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeanimations.screens.AnimatedDrawerScreen
import com.example.composeanimations.screens.AnimatedDrawerUsingAnchorScreen
import com.example.composeanimations.screens.AnimationSpecScreen
import com.example.composeanimations.screens.HomeScreen
import com.example.composeanimations.ui.theme.ComposeAnimationsTheme

object Routes{
    const val HOME_SCREEN = "home_screen"
    const val ANIMATED_DRAWER_SCREEN = "animated_drawer_screen"
    const val ANIMATED_DRAWER_WITH_ANCHOR_SCREEN = "animated_drawer_with_anchor_screen"
    const val SIMPLE_ANIMATIONS_SCREEN = "animation_spec_screen"
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
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
            AnimationSpecScreen (
                modifier = modifier
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun NavHostPreview() {
    ComposeAnimationsTheme {
        AppNavHost(modifier = Modifier.padding(8.dp))
    }
}