package app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import binarysearchtrees.BinarySearchTree
import binarysearchtrees.Vertex

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TreeView(
    tree: BinarySearchTree<String, Position>,
    indicator: State<Int>,
    vertexSize: Dp,
    scrollDelta: ScrollDelta
) {
    val scrollCf = defaultScrollCf

    Box(Modifier.fillMaxSize().background(defaultBackground)
        .onPointerEvent(PointerEventType.Scroll) {
            scrollDelta.x -= it.changes.first().scrollDelta.x.dp * scrollCf
            scrollDelta.y -= it.changes.first().scrollDelta.y.dp * scrollCf
        }
    ) {
        tree.getRoot()?.let {
            val rootState = remember(indicator.value) { mutableStateOf(it as Vertex<String, Position>) }
            VertexView(rootState, vertexSize, scrollDelta)
        }
    }
}