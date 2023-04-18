package binarysearchtrees

interface RedBlackTree<K : Comparable<K>, V> : BinarySearchTree<K, V> {
    override fun getRoot(): MutableVertex<K, V>?

    override operator fun iterator(): Iterator<MutableVertex<K, V>>

    //for extension functions like all, any, ...
    interface Vertex<K, V> : BinarySearchTree.Vertex<K, V> {
        val color: Color
        override val left: Vertex<K, V>?
        override val right: Vertex<K, V>?

        enum class Color { RED, BLACK }
    }

    interface MutableVertex<K, V> : BinarySearchTree.MutableVertex<K, V>, Vertex<K, V> {
        override val left: MutableVertex<K, V>?
        override val right: MutableVertex<K, V>?
    }
}