package com.github.composeplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.composeplay.ui.theme.ComposePlayTheme
import images
import kotlinx.coroutines.delay

class MoveCirclesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White),
                        ) {
                            MoveCircles()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoveCircles() {
    var selectedIndicator by remember { mutableStateOf(1) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000) // Change the delay to adjust the sliding speed
            selectedIndicator = (selectedIndicator % 7) + 1
        }
    }
    Row(
        modifier = Modifier
            .background(color = Color.Black.copy(alpha = 0.25f))
            .fillMaxWidth()
            .height(100.dp)
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..7) {
            IndicatorCustom(selected = i == selectedIndicator)
        }
    }
}

@Composable
fun IndicatorCustom(selected: Boolean) {
    val defaultWidth = 18.dp
    val capsuleWidth = defaultWidth.times(2)
    val padding = defaultWidth.div(2)
    val resource = remember {
        MutableInteractionSource()
    }

    Box(
        modifier = Modifier
            .padding(horizontal = padding)
            .background(Color.White, CircleShape)
            .size(
                width = if (selected) {
                    capsuleWidth
                } else {
                    defaultWidth
                }, height = defaultWidth
            )
            .clickable(
                interactionSource = resource,
                indication = rememberRipple(bounded = true, color = Color.Black),
                enabled = true,
                onClickLabel = null,
                role = Role.Button,
                onClick = {

                },
            )
    )
}

//fun Modifier.scaleOnPress(
//    interactionSource: InteractionSource,
//) = composed {
//    val isPressed by interactionSource.collectIsPressedAsState()
//    val scale by animateFloatAsState(
//        if (isPressed) {
//            0.98f
//        } else {
//            1f
//        },
//        label = "scale on press",
//    )
//    this.graphicsLayer {
//        scaleX = scale
//        scaleY = scale
//    }
//}
enum class Direction {
    Left, Right

}

fun Modifier.movePosition(
    enabled: Boolean,
    direction: Direction
) = composed {
    val offsetInt: IntOffset by animateIntOffsetAsState(
        targetValue =
        if (enabled) {
            IntOffset(calculateOffsetPosition(direction), 0)
        } else IntOffset.Zero, label = ""
    )
    this.offset {
        offsetInt
    }
}

fun calculateOffsetPosition(direction: Direction): Int {
    return when (direction) {
        Direction.Left -> -100
        Direction.Right -> 100
    }
}
