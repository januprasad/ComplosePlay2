package com.github.composeplay

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import images
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun CarouselApp(
    list: MutableList<Int>,
    carouselLabel: String = "",
    autoScrollDuration: Long = 1500L,
) {
//    val pageCount = list.size
    val pageCount = Int.MAX_VALUE
    val pagerState: PagerState = rememberPagerState(initialPage = pageCount/2, pageCount = { pageCount })
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    var currentPageKey by remember { mutableIntStateOf(0) }
    if (isDragged.not()) {
        with(pagerState) {
            LaunchedEffect(key1 = currentPageKey) {
                launch {
                    delay(timeMillis = autoScrollDuration)
                    val nextPage = (currentPage + 1).mod(pageCount)
                    animateScrollToPage(
                        page = nextPage,
                        animationSpec = tween(
                            durationMillis = 800
                        )
                    )
                    currentPageKey = nextPage
                }
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(
                    horizontal = 10.dp
                ),
                pageSpacing = 10.dp
            ) { page: Int ->
                val item = list[page % list.size]
//                currentPageKey = page
                Card(
                    onClick = { },
                    modifier = Modifier.carouselTransition(
                        page,
                        pagerState
                    )
                ) {
                    CarouselBox(item)
                }
            }
        }

        if (carouselLabel.isNotBlank()) {
            Text(
                text = carouselLabel,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun CarouselBox(item: Int) {
    Box {
        Image(
            painter = painterResource(id = item),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth().aspectRatio(2f)
        )
        val gradient = remember {
            Brush.verticalGradient(
                listOf(
                    Color.Transparent,
                    Color.Black.copy(alpha = 0.23f)
                )
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.carouselTransition(
    page: Int,
    pagerState: PagerState
) = graphicsLayer {
    val pageOffset =
        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

    val transformation = lerp(
        start = 0.8f,
        stop = 1f,
        fraction = 1f - pageOffset.coerceIn(
            0f,
            1f
        )
    )
    alpha = transformation
    scaleY = transformation
}
