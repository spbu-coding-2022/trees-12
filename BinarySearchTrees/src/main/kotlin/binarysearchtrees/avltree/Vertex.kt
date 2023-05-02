package binarysearchtrees.avltree

import binarysearchtrees.MutableVertex

interface Vertex<K, V> : MutableVertex<K, V> {
    var balance: Int

    override val left: Vertex<K, V>?
    override val right: Vertex<K, V>?
}