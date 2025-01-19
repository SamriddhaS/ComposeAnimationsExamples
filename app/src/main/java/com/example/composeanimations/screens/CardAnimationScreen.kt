package com.example.composeanimations.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch

val list = listOf(1, 2, 3, 4, 5)
val size = list.size

@Composable
fun CardsAnimationScreen(modifier: Modifier = Modifier) {

    /*
    * Using single Animatable transformation to animate all the animations
    * ie : offset , rotation
    * */
    val translation = remember {
        Animatable(0f)
    }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.pointerInput(Unit){
                awaitPointerEventScope {
                    /*
                    * Detect : Press/Release gestures.
                    * */
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.type == PointerEventType.Press){
                            coroutineScope.launch {
                                translation.animateTo(targetValue = 1f, animationSpec = tween(600, easing = LinearOutSlowInEasing))
                            }
                        }else if(event.type == PointerEventType.Release){
                            coroutineScope.launch {
                                translation.animateTo(targetValue = 0f, animationSpec = spring(dampingRatio = 0.600f, stiffness = 600f))
                            }
                        }
                    }
                }
            }
        ) {
            list.forEachIndexed { index, i ->
                Card(index, translation)
            }
        }
    }
}

@Composable
fun Card(
    index: Int,
    transition: Animatable<Float, AnimationVector1D>,
    modifier: Modifier = Modifier
) {

    // X Axis translation : index * -40 (start) --> index * 50 (end)
    val xAxisLerp = lerp(index * -40, index * 50, transition.value)

    // Y Axis translation : 0 (no Y translation - start) --> index * 50 (end)
    val yAxisLerp = lerp(0, index * -10, transition.value)

    val offset = IntOffset(xAxisLerp, yAxisLerp)

    // Rotate translation : index * -9f (start) --> index * 5f (end)
    val rotationValue = lerp(index * -9f, index * 5f, transition.value)

    // Alpha : Fade the cards little based on index.
    val alphaValue = if (index == 0) 1f else (size - index) * 0.7f

//    val offset by animateIntOffsetAsState(
//        targetValue = if (isExpanded){
//            IntOffset(index*50, index*-10)
//        }else{
//            IntOffset(index*-40, 0)
//        },
//        label = "CardsOffset"
//    )

//    val rotationValue by animateFloatAsState(
//        targetValue = if (isExpanded) index*5f else index*-9f,
//        label = "RotationValue"
//    )

    Box(
        modifier = modifier
            .offset { offset }
            .size(width = 150.dp, height = 200.dp)
            .rotate(rotationValue)
            .alpha(alphaValue)
            .zIndex(-index * 1f)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, color = Color.White, shape = RoundedCornerShape(18.dp))
            .background(Color.Gray)
    ) {
        Text("$index", modifier = Modifier.padding(6.dp))
    }
}

@Preview
@Composable
private fun CardsScreenPreview() {
    CardsAnimationScreen(modifier = Modifier.padding(18.dp))
}