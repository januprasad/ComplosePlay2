package com.github.composeplay

import PageIndicator
import PagerUiScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.composeplay.ui.theme.ComposePlayTheme
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
                            modifier = Modifier,
                        ) {
//                            App()
//                            CustomLayout()
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
            PageIndicator(modifier = Modifier.padding(4.dp).align(Alignment.BottomCenter), pagerState =pagerState)
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomLayout() {
    Box {
        FlowRow1 {
            repeat(600) {
                Box(
                    modifier =
                    Modifier
                        .width(10.dp)
                        .height(50.dp)
                        .background(Color(Random.nextLong(0xFFFFFFFF))),
                )
            }
        }
    }
}


@Composable
fun FlowRow1(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {

    Layout(content = content, measurePolicy = { measurables, constraints ->
        //define all placeables
        val placeables = measurables.map {
            it.measure(constraints)
        }
        //

        val groupedPlaceables = mutableListOf<List<Placeable>>()
        var currentGroup = mutableListOf<Placeable>()
        var currentGroupWidth = 0
        placeables.forEach { placeable ->
            if (currentGroupWidth + placeable.width <= constraints.maxWidth) {
                currentGroup.add(placeable)
                currentGroupWidth += placeable.width
            } else {
                groupedPlaceables.add(currentGroup)
                currentGroup = mutableListOf(placeable)
                currentGroupWidth = placeable.width
            }
        }

        if (currentGroup.isNotEmpty()) {
            groupedPlaceables.add(currentGroup)
        }
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
        ) {
            var yPosition = 0
            groupedPlaceables.forEach { row ->
                var xPosition = 0
                row.forEach { placeable ->
                    placeable.place(
                        x = xPosition,
                        y = yPosition,
                    )
                    xPosition += placeable.width
                }
                yPosition += row.maxOfOrNull { it.height } ?: 0
            }
        }
    })
}

@Composable
fun FlowRow2(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        measurePolicy = { measurables, constraints ->

            //define all placebles
            val placeables =
                measurables.map {
                    it.measure(constraints)
                }

            val groupedPlaceables = mutableListOf<List<Placeable>>()
            var currentGroup = mutableListOf<Placeable>()
            var currentGroupWidth = 0

            placeables.forEach { placeable ->
                if (currentGroupWidth + placeable.width <= constraints.maxWidth) {
                    currentGroup.add(placeable)
                    currentGroupWidth += placeable.width
                } else {
                    groupedPlaceables.add(currentGroup)
                    currentGroup = mutableListOf(placeable)
                    currentGroupWidth = placeable.width
                }
            }

            if (currentGroup.isNotEmpty()) {
                groupedPlaceables.add(currentGroup)
            }

            layout(
                width = constraints.maxWidth,
                height = constraints.maxHeight,
            ) {
                var yPosition = 0
                groupedPlaceables.forEach { row ->
                    var xPosition = 0
                    row.forEach { placeable ->
                        placeable.place(
                            x = xPosition,
                            y = yPosition,
                        )
                        xPosition += placeable.width
                    }
                    yPosition += row.maxOfOrNull { it.height } ?: 0
                }
            }
        },
        content = content,
    )
}


@Composable
internal fun TryNewapp() {
//    Box(
//        modifier =
//            Modifier.size(100.dp)
//                .drawBehind {
//                    drawRoundRect(
//                        topLeft = Offset.Zero,
//                        cornerRadius = CornerRadius(50f, 50f),
//                        brush = Brush.horizontalGradient(listOf(Color.Red, Color.Green)),
//                    )
//                },
//    )
    val paint = Paint()
    val frameworkPaint = paint.asFrameworkPaint()
    frameworkPaint.color = Color.Black.copy(alpha = 0.5f).toArgb()
    Box(
        modifier =
        Modifier
            .width(300.dp)
            .height(50.dp)
            .background(Color.Gray)
            .drawBehind {
                val radius = size.width / 1.5f
                val extra = size.width / 7
                this.drawIntoCanvas {
                    it.drawRoundRect(
                        left = -extra,
                        top = -extra,
                        right = size.width + extra,
                        bottom = size.height + extra,
                        radiusX = 40f,
                        radiusY = 50f,
                        paint,
                    )
                }
            },
    )
}

@Composable
internal fun App() {
    val numbers = remember {
        mutableStateListOf<String>()
    }
    Column {
        Button(onClick = {
            numbers += "1234343"
        }) {
            Text(text = "Add")
        }
        PhoneNumbers(numbers)
    }
}

@Composable
internal fun PhoneNumbers(numbers: SnapshotStateList<String>) {
    LazyColumn {
        items(numbers.toList()) {
            Text(text = it)
        }
    }
}