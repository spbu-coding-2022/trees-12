package binarysearchtrees.avltree

import binarysearchtrees.MutableVertex

interface Vertex<K, V> : MutableVertex<K, V> {
    override val left: Vertex<K, V>?
    override val right: Vertex<K, V>?
}