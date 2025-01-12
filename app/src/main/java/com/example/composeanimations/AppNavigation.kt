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
import com.example.composeanimations.screens.FirstScreen
import com.example.composeanimations.screens.HomeScreen
import com.example.composeanimations.ui.theme.ComposeAnimationsTheme

object Routes{
    const val HOME_SCREEN = "home_screen"
    const val FIRST_ANIMATION = "first_animation_screen"
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

        composable(route = Routes.FIRST_ANIMATION){
            FirstScreen (
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