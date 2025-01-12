package com.example.composeanimations

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeanimations.ui.theme.ComposeAnimationsTheme

object Routes{
    const val HOME_SCREEN = "home_screen"
    const val FIRST_SCREEN = "first_screen"
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    ) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME_SCREEN
    ){
        composable(route = Routes.HOME_SCREEN){
            HomeScreen(
                navigateTo = { route -> navController.navigate(route) }
            )
        }

    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier,navigateTo:(route:String)->Unit) {
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
            , onClick = {navigateTo("")}
        ) {
            Text(text = "First Animation")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NavHostPreview() {
    ComposeAnimationsTheme {
        AppNavHost()
    }
}