package app.view

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import binarysearchtrees.Vertex
import binarysearchtrees.redblacktree.Vertex as RBVertex
import binarysearchtrees.redblacktree.Vertex.Color as RBColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VertexView(
    vertexState: MutableState<Vertex<String, Position>>,
    vertexSize: Dp,
    scrollDelta: ScrollDelta,
    modifier: Modifier = Modifier
) {
    val vertex by vertexState

    vertex.left?.let {
        Edge(vertex, it, vertexSize, scrollDelta)
        VertexView(mutableStateOf(it), vertexSize, scrollDelta)
    }
    vertex.right?.let {
        Edge(vertex, it, vertexSize, scrollDelta)
        VertexView(mutableStateOf(it), vertexSize, scrollDelta)
    }

    val brush = if (vertex is RBVertex) {
        if ((vertex as RBVertex<String, Position>).color == RBColor.BLACK)
            defaultBlackBrush
        else defaultRedBrush
    } else defaultBrush
    Box(
        modifier.zIndex(4f)
            .offset(vertex.value.x + scrollDelta.x, vertex.value.y + scrollDelta.y)
            .size(vertexSize)
            .background(
                defaultBackground,
                CircleShape
            )
            .border(5.dp, brush, CircleShape)
            .pointerInput(vertex) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    vertex.value.x += dragAmount.x.toDp()
                    vertex.value.y += dragAmount.y.toDp()
                }
            }
    ) {
        TooltipArea(
            modifier = Modifier.zIndex(5f).align(Alignment.Center),
            tooltip = {
                Surface(
                    modifier = Modifier.shadow(10.dp),
                    color = defaultBackground,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = vertex.key,
                        modifier = Modifier.padding(defaultPadding * 2),
                        style = defaultTextStyle
                    )
                }
            },
            delayMillis = 600, // in milliseconds
            tooltipPlacement = TooltipPlacement.CursorPoint(
                alignment = Alignment.BottomEnd
            )
        ) {
            VertexText(
                text = vertex.key,
                modifier = Modifier,
            )
        }
    }
}

@Composable
private fun VertexText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = if (text.length < 6) text else (text.dropLast(text.length - 4) + ".."),
        modifier = modifier,
        style = defaultTextStyle
    )
}
