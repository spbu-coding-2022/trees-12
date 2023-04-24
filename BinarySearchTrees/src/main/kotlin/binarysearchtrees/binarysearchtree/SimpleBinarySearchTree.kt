package binarysearchtrees.binarysearchtree

import binarysearchtrees.BinarySearchTree
import binarysearchtrees.binarysearchtree.Vertex as PublicVertex

open class SimpleBinarySearchTree<K : Comparable<K>, V> : BinarySearchTree<K, V> {
    final override var size: Int = 0
        private set
    protected var root: Vertex<K, V>? = null
    protected var modCount: Int = 0

    override fun isEmpty(): Boolean = (root == null)

    override fun clear() {
        size = 0
        root = null
        ++modCount
    }

    override fun getRoot(): PublicVertex<K, V>? = root

    override fun iterator(): Iterator<PublicVertex<K, V>> {
        return BinarySearchTreeIterator<K, V>(getRoot()) { modCount }
    }

    override fun get(key: K): V? {
        var vertex = root
        while (vertex != null && vertex.key != key) {
            if (vertex.key > key) {
                vertex = vertex.left
            } else {
                vertex = vertex.right
            }
        }
        return vertex?.value
    }

    override fun put(key: K, value: V): V? {
        var new = false
        var vertex: Vertex<K, V> = root ?: Vertex(key, value).also {
            root = it
            new = true
        }
        while (vertex.key != key) {
            if (vertex.key > key) {
                vertex = vertex.left ?: Vertex(key, value).also {
                    vertex.left = it
                    new = true
                }
            } else {
                vertex = vertex.right ?: Vertex(key, value).also {
                    vertex.right = it
                    new = true
                }
            }
        }
        return if (new) {
            ++size
            ++modCount
            null
        } else vertex.setValue(value)
    }

    override operator fun set(key: K, value: V) {
        put(key, value)
    }

    override fun remove(key: K): V? {
        var parent: Vertex<K, V>? = null
        var vertex = root
        while (vertex != null && vertex.key != key) {
            parent = vertex
            if (vertex.key > key) {
                vertex = vertex.left
            } else {
                vertex = vertex.right
            }
        }
        val oldValue = vertex?.value
        if (vertex != null) {
            if (parent == null) {
                root = removeVertex(vertex)
            } else {
                if (parent.left == vertex) {
                    parent.left = removeVertex(vertex)
                } else {
                    parent.right = removeVertex(vertex)
                }
            }
            --size
            ++modCount
        }
        return oldValue
    }

    override fun remove(key: K, value: V): Boolean {
        var parent: Vertex<K, V>? = null
        var vertex = root
        while (vertex != null && vertex.key != key) {
            parent = vertex
            if (vertex.key > key) {
                vertex = vertex.left
            } else {
                vertex = vertex.right
            }
        }
        return if (vertex?.value == value) {
            if (parent == null) {
                root = vertex?.let { removeVertex(it) }
            } else {
                if (parent.left == vertex) {
                    parent.left = vertex?.let { removeVertex(it) }
                } else {
                    parent.right = vertex?.let { removeVertex(it) }
                }
            }
            --size
            ++modCount
            true
        } else false
    }

    private fun removeVertex(vertex: Vertex<K, V>): Vertex<K, V>? {
        return vertex.left?.let { left ->
            vertex.right?.let { right ->
                //search of parent of Vertex with next key
                var nextParent = vertex
                var next = right // vertex.right
                var nextLeft = next.left // left son of next vertex
                while (nextLeft != null) {
                    nextParent = next
                    next = nextLeft
                    nextLeft = next.left
                }
                if (nextParent == vertex) {
                    next.left = left // vertex.left
                } else {
                    nextParent.left = next.right
                    next.left = left // vertex.left
                    next.right = right // vertex.right
                }

                next
            } ?: left
        } ?: vertex.right
    }

    protected class Vertex<K, V>(
        override val key: K,
        override var value: V,
        override var left: Vertex<K, V>? = null,
        override var right: Vertex<K, V>? = null
    ) : PublicVertex<K, V> {
        override fun setValue(newValue: V): V = value.also { value = newValue }
    }

    protected class BinarySearchTreeIterator<K, V>(
        root: PublicVertex<K, V>?,
        private val getModCount: () -> Int
    ) : Iterator<PublicVertex<K, V>> {
        private val stack: MutableList<PublicVertex<K, V>> = mutableListOf()
        private val expectedModCount: Int = getModCount()

        init {
            var vertex = root
            while (vertex != null) {
                stack.add(vertex)
                vertex = vertex.left
            }
        }

        override fun hasNext(): Boolean {
            if (expectedModCount != getModCount()) {
                throw ConcurrentModificationException()
            } else {
                return stack.isNotEmpty()
            }
        }

        override fun next(): PublicVertex<K, V> {
            if (expectedModCount != getModCount()) {
                throw ConcurrentModificationException()
            } else {
                val vertex = stack.removeLast()
                var nextVertex = vertex.right
                while (nextVertex != null) {
                    stack.add(nextVertex)
                    nextVertex = nextVertex.left
                }
                return vertex
            }
        }
    }
}