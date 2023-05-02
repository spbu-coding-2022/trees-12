package binarysearchtrees.avltree

import kotlin.math.max
import kotlin.math.min

open class AVLTree<K : Comparable<K>, V>(vararg init: Pair<K, V>) {

    var size: Int = 0

    private var root: Vertex<K, V>? = null

    init {
        init.forEach {
            insert(it.first, it.second)
        }
    }

    fun getRoot(): Vertex<K, V>? = root

    fun isEmpty(): Boolean = (root == null)

    fun clear() {
        size = 0
        root = null
    }

    operator fun set(key: K, value: V) {
        insert(key, value)
    }

    fun insert(key: K, value: V) {
        root = add(root, Vertex(key, value, 1))
    }

    private fun add(vertex: Vertex<K, V>?, newVertex: Vertex<K, V>): Vertex<K, V>? {
        if (vertex == null) {
            return newVertex
        }

        when {
            newVertex.key < vertex.key -> {
                vertex.left = add(vertex.left, newVertex)
            }
            newVertex.key > vertex.key -> {
                vertex.right = add(vertex.right, newVertex)
            }
        }

        val heightLeft = vertex.leftHeight
        val heightRight = vertex.rightHeight
        val balance = vertex.balance

        val leftGreater = balance > 1
        val rightGreater = balance < -1

        vertex.height = 1 + max(heightLeft, heightRight)

        if (leftGreater && newVertex.key < vertex.left!!.key) {
            return vertex.rightRotate()
        }

        if (rightGreater && newVertex.key > vertex.right!!.key) {
            return vertex.leftRotate()
        }

        if (leftGreater && newVertex.key > vertex.left!!.key) {
            vertex.left = vertex.left!!.leftRotate()

            return vertex.rightRotate()
        }

        if (rightGreater && newVertex.key < vertex.right!!.key) {
            vertex.right = vertex.right!!.rightRotate()

            return vertex.leftRotate()
        }

        ++size
        return vertex
    }

    fun delete(key: K) {
        delete(key, root)
    }

    fun delete(key: K, tree: Vertex<K, V>?) {
        if (tree == null) {
            return
        }

        val visited = mutableListOf<Vertex<K, V>>()
        val vertex = search(key, tree) {
            visited.add(it)
        } ?: return

        val parent = when {
            visited.size >= 2 -> visited.elementAt(visited.size - 2)
            vertex == tree -> tree
            else -> null
        }

        if (vertex.left == null && vertex.right == null) {
            if (vertex == root && tree == root) root = null else parent?.removeChild(vertex)
        } else if (vertex.left == null) {
            if (vertex == root && tree == root) root = vertex.right else parent?.replaceChild(vertex, vertex.right)
        } else if (vertex.right == null) {
            if (vertex == root && tree == root) root = vertex.left else parent?.replaceChild(vertex, vertex.left)
        } else {
            val min = vertex.right!!.min()
            val newNode = Vertex(min.key, min.value, vertex.height)
            newNode.left = vertex.left
            newNode.right = vertex.right
            newNode.height = vertex.height - 1

            if (vertex == root && tree == root) root = newNode else parent?.replaceChild(vertex, newNode)

            delete(min.key, vertex.right)
            --size
        }
    }

    operator fun get(key: K): V? {
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

    fun search(key: K, visited: ((K) -> Unit)? = null): V? {
        return search(key, root) { visited?.invoke(it.key) }?.value
    }

    private fun search(key: K, vertex: Vertex<K, V>?, visited: ((Vertex<K, V>) -> Unit)? = null): Vertex<K, V>? {
        if (vertex == null) {
            return null
        }

        visited?.let { it(vertex) }

        if (key < vertex.key) {
            return search(key, vertex.left, visited)
        } else if (key > vertex.key) {
            return search(key, vertex.right, visited)
        }

        return vertex
    }

    open class Vertex<K : Comparable<K>, V>(val key: K, val value: V, var height: Int) {
        var left: Vertex<K, V>? = null
        var right: Vertex<K, V>? = null

        val leftHeight
            get() = left?.height ?: 0

        val rightHeight
            get() = right?.height ?: 0

        val balance
            get() = leftHeight - rightHeight

        override fun equals(other: Any?) = if (other is Vertex<*, *>) other.key == key else false

        private fun updateHeight() {
            height = 1 + max(leftHeight, rightHeight)
        }

        fun leftRotate(): Vertex<K, V>? {
            val other = right ?: return this

            other.left = this.also { it.right = other.left }

            updateHeight()
            other.updateHeight()

            return other
        }

        fun rightRotate(): Vertex<K, V>? {
            val other = this.left ?: return this

            other.right = this.also { it.left = other.right }

            updateHeight()
            other.updateHeight()

            return other
        }

        fun removeChild(vertex: Vertex<K, V>) {
            if (right == vertex) {
                right = null
            } else if (left == vertex) {
                left = null
            }
        }

        fun replaceChild(vertex: Vertex<K, V>, with: Vertex<K, V>?) {
            if (right == vertex) {
                right = with
            } else if (left == vertex) {
                left = with
            }
        }

        fun min(): Vertex<K, V> {
            return if (left == null) {
                this
            } else {
                left!!.min()
            }
        }

        override fun hashCode() = key.hashCode()
    }
}

fun <K : Comparable<K>, V> AVLTree(items: Map<K, V>): AVLTree<K, V> {
    return AVLTree(*items.map { Pair(it.key, it.value) }.toTypedArray())
}