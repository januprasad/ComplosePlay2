import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.composeplay.R

val images = listOf(
    R.drawable.image_1,
    R.drawable.image_2,
    R.drawable.image_3,
    R.drawable.image_4,
//    R.drawable.image_5,
//    R.drawable.image_6,
//    R.drawable.image_7,
//    R.drawable.image_9,
//    R.drawable.image_10,
//    R.drawable.image_11,
)

@Composable
internal fun PagerUiScreen(
    page: Int,
    modifier: Modifier = Modifier,
    changePage: (Int) -> Unit,
) {
    Box(
        modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
    ) {
        Image(
            painter = painterResource(id = images[page]),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    changePage(page)
                }
        )
    }
}

@Composable
fun PageIndicator(modifier: Modifier, pagerState: PagerState) {
    val numberOfPages = pagerState.pageCount
    val (selectedPage, setSelectedPage) = remember {
        mutableStateOf(0)
    }

    // NEVER use this, this is just for example
    LaunchedEffect(
        key1 = pagerState.currentPage,
    ) {
        setSelectedPage(pagerState.currentPage)
    }

    PageIndicatorContent(
        modifier = modifier,
        numberOfPages = numberOfPages,
        selectedPage = selectedPage,
        defaultRadius = 8.dp,
        selectedLength = 16.dp,
        space = 4.dp,
        animationDurationInMillis = 500,
    )
}

@Composable
fun PageIndicatorContent(
    numberOfPages: Int,
    modifier: Modifier = Modifier,
    selectedPage: Int = 0,
    selectedColor: Color = Color.White,
    defaultColor: Color = Color.White,
    defaultRadius: Dp = 8.dp,
    selectedLength: Dp = 16.dp,
    space: Dp = 10.dp,
    animationDurationInMillis: Int = 100,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space),
        modifier = modifier
            .background(Color.Black.copy(0.16f), RoundedCornerShape(12.dp))
            .padding(vertical = 4.dp, horizontal = 8.dp),
    ) {
        for (i in 0 until numberOfPages) {
            val isSelected = i == selectedPage
            val isSelecting = i == selectedPage - 1
            PageIndicatorView(
                isSelected = isSelected,
                selectedColor = selectedColor,
                defaultColor = defaultColor,
                defaultRadius = defaultRadius,
                selectedLength = selectedLength,
                animationDurationInMillis = animationDurationInMillis,
            )
        }
    }
}

@Composable
fun PageIndicatorView(
    isSelected: Boolean,
    selectedColor: Color,
    defaultColor: Color,
    defaultRadius: Dp,
    selectedLength: Dp,
    animationDurationInMillis: Int,
    modifier: Modifier = Modifier,
) {

    val color: Color by animateColorAsState(
        targetValue = if (isSelected) {
            selectedColor
        } else {
            defaultColor
        },
        animationSpec = tween(
            durationMillis = animationDurationInMillis,
        )
    )
    val width: Dp by animateDpAsState(
        targetValue = if (isSelected) {
            selectedLength
        } else {
            defaultRadius
        },
        animationSpec = tween(
            durationMillis = animationDurationInMillis,
        )
    )

    Canvas(
        modifier = modifier
            .size(
                width = width,
                height = defaultRadius,
            ),
    ) {
        drawRoundRect(
            color = color,
            topLeft = Offset.Zero,
            size = Size(
                width = width.toPx(),
                height = defaultRadius.toPx(),
            ),
            cornerRadius = CornerRadius(
                x = defaultRadius.toPx(),
                y = defaultRadius.toPx(),
            ),
        )
    }
}