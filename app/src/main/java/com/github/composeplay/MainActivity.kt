package com.github.composeplay

import PageIndicator
import PagerUiScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.composeplay.ui.theme.ComposePlayTheme
import images
import kotlinx.coroutines.delay
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
                                .background(Color.Black)
                                .padding(16.dp),
                        ) {
//                            MediaCarousal()
//                            Carousel(
//                                list = images.toMutableList(),
//                                autoScroll = true
//                            )
//                            App()
//                            SwapBox()
                            Carousal2(

                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun Carousal2(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(2.2f),
    autoStart: Boolean = true,
    autoScrollDuration: Long = 1800L
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {

        val pagerState = rememberPagerState() {
            images.size
        }
        val scope = rememberCoroutineScope()

        if (autoStart) {
            var currentPageKey by remember { mutableIntStateOf(0) }
            with(pagerState) {
                LaunchedEffect(key1 = currentPageKey) {
                    launch {
                        delay(timeMillis = autoScrollDuration)
                        val nextPage = (currentPage + 1).mod(pageCount)
                        animateScrollToPage(
                            nextPage, animationSpec = tween(
                                durationMillis = 1200
                            )
                        )
                        currentPageKey = nextPage
                    }
                }
            }
        }
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
                modifier = Modifier,
            ) {
                scope.launch {
                    pagerState.animateScrollToPage(it)
                }
            }
        }
//        PageIndicator(
//            modifier = Modifier
//                .padding(4.dp)
//                .align(Alignment.BottomCenter),
//            pagerState = pagerState
//        )
        SwapDotIndicators(
            modifier = Modifier
                .padding(4.dp)
                .align(Alignment.BottomCenter),
            count = images.size, pagerState = pagerState
        )
    }
}



