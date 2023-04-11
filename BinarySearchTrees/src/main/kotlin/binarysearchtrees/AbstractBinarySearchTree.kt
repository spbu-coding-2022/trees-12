package binarysearchtrees

abstract class AbstractBinarySearchTree<K : Comparable<K>, V> : BinarySearchTree<K, V> {
    override var size: Int = 0
    protected var root: Vertex<K, V>? = null

    override fun isEmpty(): Boolean = (root == null)

    override fun clear() {
        size = 0
        root = null
    }

    override fun getRoot(): BinarySearchTree.MutableVertex<K, V>? = root

    protected class Vertex<K, V>(
        override val key: K,
        override var value: V,
        override var left: Vertex<K, V>? = null,
        override var right: Vertex<K, V>? = null
    ) : BinarySearchTree.MutableVertex<K, V> {
        override fun setValue(newValue: V): V = value.also { value = newValue }
    }
}