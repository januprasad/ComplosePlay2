package com.github.composeplay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.dp
import kotlin.random.Random


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
