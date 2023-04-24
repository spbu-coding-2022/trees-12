package binarysearchtrees.redblacktree

import binarysearchtrees.MutableVertex

interface Vertex<K, V> : MutableVertex<K, V> {
    val color: Color
    override val left: Vertex<K, V>?
    override val right: Vertex<K, V>?

    enum class Color { RED, BLACK }
}