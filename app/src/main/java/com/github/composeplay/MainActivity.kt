package com.github.composeplay

import PageIndicator
import PagerUiScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateIntSizeAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.github.composeplay.ui.theme.ComposePlayTheme
import images
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
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
                                .background(Color.Black),
                        ) {
                            CarousalDemo()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun CarousalDemo() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        }, modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box(
            Modifier
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {

            val pagerState = rememberPagerState() {
                images.size
            }
            val scope = rememberCoroutineScope()

            var i = 0
            var state = true
//            LaunchedEffect(key1 = true) {
//                while (state) {
//                    delay(2000L)
//                    pagerState.animateScrollToPage(i)
//                    if (i == images.size)
//                        state = false
//                    else i += 1
//                }
//
//            }
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                pageSpacing = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) { pageCount ->
                PagerUiScreen(
                    page = pageCount,
                    modifier = Modifier
                        .aspectRatio(2.2f),
                ) {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
            }
            PageIndicator(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.BottomCenter),
                pagerState = pagerState
            )
        }
    }
}


@Composable
fun Indicator(
    selected: Boolean,
    selecting: Boolean,
    index: Int,
    selectedColor: Color,
    defaultColor: Color,
    lastColor: Color,
    spacing: Dp,
    width: Dp,
    height: Dp
) {
    val capsule: Dp by animateDpAsState(
        targetValue = if (selected) {
            width.times(2)
        } else {
            width
        },
        animationSpec = tween(
            durationMillis = 500,
        )
    )
//    val offset by animateIntOffsetAsState(
//        targetValue = if (selected) {
//            IntOffset(60, 0)
//        } else {
//            IntOffset.Zero
//        },
//        label = "offset"
//    )

    val size by animateIntSizeAsState(
        targetValue = if (selected) {
            IntSize(width.times(2).value.toInt(), height.value.toInt())
        } else {
            IntSize(width.times(1).value.toInt(), height.value.toInt())
        },
        label = "offset"
    )
    val bg: Color by animateColorAsState(
        targetValue = if (selected) {
            Color.Red
        } else {
            Color.White
        },
        animationSpec = tween(
            durationMillis = 100,
        )
    )

    Box(
        modifier = Modifier
            .padding(horizontal = spacing)
            .size(width = capsule, height = height)
            .background(
                bg, CircleShape
            )
    )
}

