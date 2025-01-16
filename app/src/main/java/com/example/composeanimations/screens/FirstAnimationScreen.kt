package com.example.composeanimations.screens

import android.util.Log
import androidx.compose.animation.Animatable
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
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.ui.util.lerp

const val TAG = "FirstAnimationScreen"

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

        val drawerWidth = with(LocalDensity.current) {
            DrawerWidth.toPx()
        }

        val translationX = remember {
            Animatable(0f)
        }

        fun toggleDrawerState() {
            coroutineScope.launch {
                Log.d("First Screen", "toggleDrawer")
                drawerState = if (drawerState == DrawerState.Open) {
                    translationX.animateTo(
                        0f,
                        animationSpec = spring(
                            dampingRatio = DampingRatioLowBouncy,
                            stiffness = StiffnessMediumLow,
                        )
                    )
                    DrawerState.Closed
                } else {
                    translationX.animateTo(drawerWidth
                    ,spring(
                            dampingRatio =  DampingRatioMediumBouncy,
                            stiffness = StiffnessMediumLow
                    )
                    )
                    DrawerState.Open
                }
            }
        }

        val draggableState = rememberDraggableState {
            dragAmmount ->
            Log.d(TAG, "delta value : $dragAmmount")
            coroutineScope.launch{
                translationX.snapTo(translationX.value+dragAmmount)
            }
        }

        CustomDrawer(
            selectedScreen = screenState,
            onScreenSelected = { screen ->
                screenState = screen
            }
        )

        /**
        * Will help us to calculate trasnaltionX value in case of Flinch Gesture.
        * */
        val decay = rememberSplineBasedDecay<Float>()

        MainContent(
            toggleDrawer = { toggleDrawerState() },
            /**
            * "graphics layer" modifier runs in the draw phase of compose
             * life cycle. So animating with this modifier doesn't cause recompositions.
            * */
            modifier = modifier.graphicsLayer {
                this.translationX = translationX.value
                /**
                 * lerp = linearly interpolate between two values 1 - 0.8
                * */
                val scale = lerp(1f,0.8f,translationX.value/drawerWidth)
                this.scaleX = scale
                this.scaleY = scale
                /**
                 * As we have added DampingRatioMediumBouncy to the spring animation,
                 * because of the bouncyness it is possible that translationX values
                 * sometimes goes to negative. By added this condition checking we make
                 * sure it doesn't causes an crash as Corner radius can never be in negative.
                * */
                val transXToPositive = if(translationX.value<0f) 0f else translationX.value

                /**
                 * Linearly interpolate the rounded corer size. from 0 -> 32 and 32 -> 0.
                * */
                val cornerShapeInterpolator = lerp( 0.dp.toPx(),32.dp.toPx(),transXToPositive/drawerWidth)
                this.shape = RoundedCornerShape(cornerShapeInterpolator)
                this.clip = true
            }
                /**
                * Linearly interpolate the rounded corer size. from 0 -> 32 and 32 -> 0.
                * */
                .draggable(draggableState,Orientation.Horizontal, onDragStopped = {
                    /**
                     * This Callback gives us velocity of the gesture, we can use this value to
                     * calculate where the gusture would have ended given if it had continued with
                     * this velocity.
                    * */
                    velocity: Float ->
                    // This is where the gesture will have naturally stopped if it had
                    // continued with current velocity
                    val decayX = decay.calculateTargetValue(
                        translationX.value,
                        velocity
                    )

                    coroutineScope.launch {
                        val targetX = if (decayX>drawerWidth*0.5) drawerWidth else 0f
                        val canReachTargetWithDecay = (decayX>targetX && targetX == drawerWidth) ||
                                (decayX<targetX && targetX==0f)
                        if (canReachTargetWithDecay) {
                            translationX.animateDecay(
                                initialVelocity = velocity,
                                animationSpec = decay
                            )
                        }else{
                            translationX.animateTo(targetX, initialVelocity = velocity)
                        }
                        drawerState = if (targetX==drawerWidth) DrawerState.Open else DrawerState.Closed
                    }

                })
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