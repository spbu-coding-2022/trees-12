package app.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import binarysearchtrees.Vertex

@Composable
fun Edge(
    start: Vertex<String, Position>,
    end: Vertex<String, Position>,
    vertexSize: Dp,
    scrollDelta: ScrollDelta,
    modifier: Modifier = Modifier
) {
    val edgeColor = defaultEdgeColor
    Canvas(modifier = modifier.zIndex(3f).fillMaxSize()) {
        drawLine(
            start = Offset(
                ((start.value.x + vertexSize / 2) + scrollDelta.x).toPx(),
                ((start.value.y + vertexSize / 2) + scrollDelta.y).toPx(),
            ),
            end = Offset(
                ((end.value.x + vertexSize / 2) + scrollDelta.x).toPx(),
                ((end.value.y + vertexSize / 2) + scrollDelta.y).toPx(),
            ),
            strokeWidth = defaultEdgeWidht,
            color = edgeColor
        )
    }
}