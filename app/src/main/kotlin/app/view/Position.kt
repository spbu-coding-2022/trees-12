package app.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import binarysearchtrees.BinarySearchTree
import binarysearchtrees.Vertex

class Position(
    x: Dp,
    y: Dp
) {
    var x by mutableStateOf(x)
    var y by mutableStateOf(y)
}

fun setTreePositions(tree: BinarySearchTree<String, Position>, vertexSize: Dp, offset: DpOffset) {
    tree.getRoot()?.let { setVertexPosition(it, vertexSize, offset.y, offset.x) }
}

private fun setVertexPosition(
    vertex: Vertex<String, Position>,
    vertexSize: Dp,
    y: Dp,
    xOffset: Dp
): Dp {
    val leftWidth = vertex.left?.let {
        setVertexPosition(it, vertexSize, y + vertexSize, xOffset)
    } ?: 0.dp

    val rightWidth = vertex.right?.let {
        setVertexPosition(it, vertexSize, y + vertexSize, xOffset + leftWidth + vertexSize)
    } ?: 0.dp

    vertex.value.y = y
    vertex.value.x = xOffset + leftWidth
    return leftWidth + rightWidth + vertexSize
}