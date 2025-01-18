package com.example.composeanimations.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

val list = listOf(1,2,3,4,5)
val size = list.size
@Composable
fun CardsAnimationScreen(modifier: Modifier = Modifier) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.Black)
        .clickable {
            isExpanded = !isExpanded
        },
        contentAlignment = Alignment.Center
    ) {
        list.forEachIndexed { index, i ->
            Card(index,isExpanded)
        }
    }
}

@Composable
fun Card(
    index:Int,
    isExpanded:Boolean,
    modifier: Modifier = Modifier
) {

    // Initial State
    val offset by animateIntOffsetAsState(
        targetValue = if (isExpanded){
            IntOffset(index*50, index*-10)
        }else{
            IntOffset(index*-40, 0)
        },
        label = "CardsOffset"
    )
    val rotationValue by animateFloatAsState(
        targetValue = if (isExpanded) index*5f else index*-9f,
        label = "RotationValue"
    )

    val alphaValue = if (index==0) 1f else (size-index)*0.7f

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
    ){
        Text("$index",modifier = Modifier.padding(6.dp))
    }
}

@Preview
@Composable
private fun CardsScreenPreview() {
    CardsAnimationScreen(modifier = Modifier.padding(18.dp))
}