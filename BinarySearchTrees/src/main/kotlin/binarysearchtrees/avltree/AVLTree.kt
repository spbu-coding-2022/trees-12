package binarysearchtrees.avltree

import binarysearchtrees.BinarySearchTree
import binarysearchtrees.binarysearchtree.Vertex
import binarysearchtrees.avltree.Vertex as PublicVertex
import java.util.ConcurrentModificationException

open class AVLTree<K : Comparable<K>, V> : BinarySearchTree<K, V> {
    final override var size: Int = 0
        protected set
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
        return AVLTreeIterator<K, V>(getRoot()) { modCount }
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
        root = put(root, key, value)
        ++modCount
        return null
    }

    private fun put(vertex: Vertex<K, V>?, key: K, value: V): Vertex<K, V> {
        if (vertex == null) {
            size++
            return Vertex(key, value)
        }

        when {
            key < vertex.key -> {
                vertex.left = put(vertex.left, key, value)
            }
            key > vertex.key -> {
                vertex.right = put(vertex.right, key, value)
            }
            else -> {
                vertex.setValue(value)
                return vertex
            }
        }

        vertex.height = 1 + maxOf(height(vertex.left), height(vertex.right))

        val balance = getBalance(vertex)

        return when {
            balance > 1 && key < vertex.left!!.key -> rotateRight(vertex)
            balance < -1 && key > vertex.right!!.key -> rotateLeft(vertex)
            balance > 1 && key > vertex.left!!.key -> {
                vertex.left = rotateLeft(vertex.left!!)
                rotateRight(vertex)
            }
            balance < -1 && key < vertex.right!!.key -> {
                vertex.right = rotateRight(vertex.right!!)
                rotateLeft(vertex)
            }
            else -> vertex
        }
    }

    override operator fun set(key: K, value: V) {
        put(key, value)
    }

    override fun remove(key: K): V? {
        val oldValue = get(key)
        if (oldValue != null) {
            root = remove(root, key)
            ++modCount
            size--
        }
        return oldValue
    }

    private fun remove(vertex: Vertex<K, V>?, key: K): Vertex<K, V>? {
        if (vertex == null) {
            return null
        }

        when {
            key < vertex.key -> {
                vertex.left = remove(vertex.left, key)
            }
            key > vertex.key -> {
                vertex.right = remove(vertex.right, key)
            }
            else -> {
                if (vertex.left == null || vertex.right == null) {
                    return vertex.left ?: vertex.right
                } else {
                    val successor = findMin(vertex.right!!)
                    vertex.key = successor.key
                    vertex.value = successor.value
                    vertex.right = remove(vertex.right, successor.key)
                }
            }
        }

        vertex.height = 1 + maxOf(height(vertex.left), height(vertex.right))

        val balance = getBalance(vertex)

        return when {
            balance > 1 && getBalance(vertex.left) >= 0 -> rotateRight(vertex)
            balance > 1 && getBalance(vertex.left) < 0 -> {
                vertex.left = rotateLeft(vertex.left!!)
                rotateRight(vertex)
            }
            balance < -1 && getBalance(vertex.right) <= 0 -> rotateLeft(vertex)
            balance < -1 && getBalance(vertex.right) > 0 -> {
                vertex.right = rotateRight(vertex.right!!)
                rotateLeft(vertex)
            }
            else -> vertex
        }
    }

    override fun remove(key: K, value: V): Boolean {
        val oldValue = get(key)
        if (oldValue != null && oldValue == value) {
            root = remove(root, key)
            ++modCount
            size--
            return true
        }
        return false
    }

    private fun rotateLeft(z: Vertex<K, V>): Vertex<K, V> {
        val y = z.right!!
        val T2 = y.left

        y.left = z
        z.right = T2

        z.height = 1 + maxOf(height(z.left), height(z.right))
        y.height = 1 + maxOf(height(y.left), height(y.right))

        return y
    }

    private fun rotateRight(z: Vertex<K, V>): Vertex<K, V> {
        val y = z.left!!
        val T3 = y.right

        y.right = z
        z.left = T3

        z.height = 1 + maxOf(height(z.left), height(z.right))
        y.height = 1 + maxOf(height(y.left), height(y.right))

        return y
    }

    private fun height(vertex: Vertex<K, V>?): Int = vertex?.height ?: 0

    private fun getBalance(vertex: Vertex<K, V>?): Int = height(vertex?.left) - height(vertex?.right)

    private fun findMin(vertex: Vertex<K, V>): Vertex<K, V> {
        var current = vertex
        while (current.left != null) {
            current = current.left!!
        }
        return current
    }

    protected class Vertex<K, V>(
            override var key: K,
            override var value: V,
            override var left: Vertex<K, V>? = null,
            override var right: Vertex<K, V>? = null,
            var height: Int = 1
    ) : PublicVertex<K, V> {
        override fun setValue(newValue: V): V = value.also { value = newValue }
    }

    protected class AVLTreeIterator<K, V>(
            root: binarysearchtrees.avltree.Vertex<K, V>?,
            private val getModCount: () -> Int
    ) : Iterator<binarysearchtrees.avltree.Vertex<K, V>> {
        private val stack: MutableList<binarysearchtrees.avltree.Vertex<K, V>> = mutableListOf()
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

        override fun next(): binarysearchtrees.avltree.Vertex<K, V> {
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
