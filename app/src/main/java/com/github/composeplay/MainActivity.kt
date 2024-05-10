package com.github.composeplay

import PageIndicator
import PageIndicatorContent
import PagerUiScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRowDefaults.Indicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.composeplay.ui.theme.ComposePlayTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

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
//                            TryNewApp()
                            SwapShapes()
//                            CarousalDemo()
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
                10
            }
            val scope = rememberCoroutineScope()

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
                    .align(Alignment.BottomCenter), pagerState = pagerState
            )
        }
    }
}


@Composable
fun SwapShapes() {
    var swapped by remember { mutableStateOf(false) }

    val size = 20.dp
    val initialOffset = 0.dp
    val swappedOffset = 40.dp

    val offsetLeft = animateDpAsState(targetValue = if (swapped) swappedOffset else initialOffset)
    val offsetRight = animateDpAsState(targetValue = if (swapped) initialOffset else swappedOffset)

    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .offset(x = offsetLeft.value.times(1.5f))
                .size(size)
                .background(Color.Red, CircleShape)
                .clickable { swapped = !swapped }
        )
        Box(
            modifier = Modifier
                .offset(x = offsetRight.value)
                .size(width = size.times(2), height = size)
                .background(Color.White, CircleShape)
                .clickable { swapped = !swapped }
        )
    }
    PageIndicatorContent(
        modifier = Modifier,
        numberOfPages = 4,
        selectedPage = 0,
        defaultRadius = 20.dp,
        selectedLength = 40.dp,
        space = 4.dp,
        animationDurationInMillis = 500,
    )
    Row {
        LaunchedEffect(key1 = true) {

        }
        for (i in 0 until 4) {
            Indicator(
                selected = false,
                shift = false,
                shiftPosition = 50.dp,
                spacing = 10.dp,
                width = 30.dp,
                height = 30.dp
            )
        }
    }
}

@Composable
fun Indicator(
    selected: Boolean, shift: Boolean, shiftPosition: Dp,
    spacing: Dp,
    width: Dp,
    height: Dp
) {
    Box(
        modifier = Modifier
            .padding(horizontal = spacing)
            .size(width = if (selected) width.times(2) else width, height = height)
            .background(Color.White, CircleShape)
            .then(if (shift) Modifier.offset(x = shiftPosition) else Modifier)
    )
}

@Composable
internal fun TryNewApp() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.25f))
            .padding(40.dp)
    ) {

        val isSelected = remember {
            mutableStateOf(false)
        }
        val right: Dp by animateDpAsState(
            targetValue = if (isSelected.value) {
                100.dp
            } else {
                0.dp
            },
            animationSpec = tween(
                durationMillis = 600,
            )
        )
        val left: Dp by animateDpAsState(
            targetValue = if (isSelected.value) {
                0.dp
            } else {
                100.dp
            },
            animationSpec = tween(
                durationMillis = 601,
            )
        )

        LaunchedEffect(key1 = true) {
            delay(2000L)
            isSelected.value = true
        }

        Box(
            modifier = Modifier
                .size(30.dp)
                .offset(right, 0.dp)
                .background(Color.Green, shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .size(width = 60.dp, height = 30.dp)
                .offset(left, 0.dp)
                .background(Color.Green, shape = CircleShape)
        )
    }
}
