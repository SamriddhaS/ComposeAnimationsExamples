package com.example.composeanimations.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun AnimationSpecScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            VisibilityAnimationUsingAlpha(modifier = Modifier.weight(1f))
            VisibilityAnimation(modifier = Modifier.weight(1f))
        }

        AnimateComposableSize(Modifier.fillMaxWidth())

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
        {
            AnimateOffSet(modifier = Modifier.weight(1f))
            AnimateMultipleBoxes(modifier = Modifier.weight(1f))
        }
    }
}

/**
* One way to animate visibility of a compose is to use change the alpha value along with animateFloatAsState.
 * However, changing the alpha comes with the caveat that the composable remains in the composition and continues
 * to occupy the space it's laid out in.
 * This could cause screen readers and other accessibility mechanisms to still consider the item on screen.
* */
@Composable
fun VisibilityAnimationUsingAlpha(modifier: Modifier = Modifier) {

    var visible by remember {
        mutableStateOf(true)
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = if (visible) 1.0f else 0f,
        label = "alpha",
        finishedListener = { currentValue ->
            /*
            * This callback will be called once the animation is done.
            * */
            Log.d(TAG,"onFinised => animatedFloatAsState - $currentValue")
        },
        animationSpec = TweenSpec(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .graphicsLayer {
                    alpha = animatedAlpha
                }
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Green)
        )
        Button(onClick = {
            visible = !visible
        },modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
            Text("Animate visibility")
        }
    }
}

/**
* We can also animate visibility of a composable using "AnimatedVisibility" modifier. AnimatedVisibility eventually
 * removes the item from the composition.
* */
@Composable
fun VisibilityAnimation(modifier: Modifier = Modifier) {

    var visible by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        AnimatedVisibility(visible) {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Green)
            )
        }
        Button(onClick = {
            visible = !visible
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Text("Animate visibility")
        }
    }
}

@Composable
fun AnimateComposableSize(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    /*
    * We can animate any dp value like this. Here we are animating the padding value
    * */
    val animatedPadding by animateDpAsState(
        if (expanded) {
            20.dp
        } else {
            0.dp
        },
        label = "padding"
    )

    Box(
        modifier = modifier
            .padding(animatedPadding)
            .background(Color.Blue)
            .fillMaxWidth()
            .animateContentSize()
            .height(if (expanded) 200.dp else 100.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                expanded = !expanded
            }

    ){
        Text(
            text = "Animate Composable Size",
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

/**
* Animate offset and rotation
* */
@Composable
fun AnimateOffSet(modifier: Modifier = Modifier) {
    var moved by remember { mutableStateOf(false) }
    val pxToMove = with(LocalDensity.current) {
        70.dp.toPx().roundToInt()
    }
    val offset by animateIntOffsetAsState(
        targetValue = if (moved) {
            IntOffset(pxToMove, pxToMove)
        } else {
            IntOffset.Zero
        },
        label = "offset"
    )

    // Add some rotation animation
    val rotation by animateFloatAsState(
        targetValue = if (moved) 90f else 0f
    )

    Box(
        modifier = modifier
            .padding(18.dp)
            .offset {
                offset
            }
            .rotate(rotation)
            .background(Color.Magenta)
            .size(100.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                moved = !moved
            }
    ){
        Text(text = "Offset And Rotation")
    }
}

/**
* Animate multiple boxes : without overlapping
 * If you want to ensure that composables are not drawn over or under other composables when
 * animating position or size, use Modifier.layout{ }.
 * This modifier propagates size and position changes to the parent, which then affects other children.
 * For example, if you are moving a Box within a Column and the other children need to move when the Box
 * moves, include the offset information with Modifier.layout{ }.
* */
@Composable
fun AnimateMultipleBoxes(modifier: Modifier = Modifier) {

    var toggled by remember {
        mutableStateOf(false)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .clickable(indication = null, interactionSource = interactionSource) {
                toggled = !toggled
            }
    ) {
        val offsetTarget = if (toggled) {
            IntOffset(150, 150)
        } else {
            IntOffset.Zero
        }
        val offset = animateIntOffsetAsState(
            targetValue = offsetTarget, label = "offset"
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.Yellow)
        )
        Box(
            modifier = Modifier
                .layout { measurable, constraints ->
                    val offsetValue = if (isLookingAhead) offsetTarget else offset.value
                    val placeable = measurable.measure(constraints)
                    layout(placeable.width + offsetValue.x, placeable.height + offsetValue.y) {
                        placeable.placeRelative(offsetValue)
                    }
                }
                .size(50.dp)
                .background(Color.Red)
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.Green)
        )

        Text("Animate Multiple Boxes")
    }
}