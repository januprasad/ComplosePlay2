package com.github.composeplay

import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Carousel(
    list: MutableList<Int>,
    autoScrollDuration: Long = 1500L,
    autoScroll: Boolean = true
) {
    val pageCount = Int.MAX_VALUE
    val pagerState: PagerState = rememberPagerState(initialPage = pageCount/2, pageCount = { pageCount })
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    var currentPageKey by remember { mutableIntStateOf(0) }
    if (isDragged.not() && autoScroll) {
        with(pagerState) {
            LaunchedEffect(key1 = settledPage) {
                launch {
                    delay(timeMillis = autoScrollDuration)
                    val nextPage = (currentPage + 1).mod(pageCount)
                    currentPageKey = nextPage
                    Log.v("LaunchedEffect", "$nextPage")
                    animateScrollToPage(
                        page = nextPage,
                        animationSpec = tween(
                            durationMillis = 800
                        )
                    )
                }
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val scope = rememberCoroutineScope()
        Box {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(
                    horizontal = 32.dp
                ),
                pageSpacing = 20.dp,
                userScrollEnabled = false
            ) { page: Int ->
                val item = list[page % list.size]
                Card(
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                page,
                                animationSpec = tween(
                                    durationMillis = 800,
                                ),
                            )
                        }
                    },
                    modifier = Modifier
                ) {
                    PagerImages(item)
                }
            }
        }
    }
}

@Composable
fun PagerImages(item: Int) {
    Box {
        Image(
            painter = painterResource(id = item),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
        )
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
