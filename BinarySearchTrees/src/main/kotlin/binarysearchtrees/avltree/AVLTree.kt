package binarysearchtrees.avltree

import binarysearchtrees.BinarySearchTree
import binarysearchtrees.MutableVertex
import binarysearchtrees.avltree.AVLTree
import binarysearchtrees.avltree.Vertex
import binarysearchtrees.avltree.Vertex as PublicVertex

open class AVLTree<K : Comparable<K>, V> : BinarySearchTree<K, V> {
    final override var size: Int = 0
        protected set
    protected var root: AVLVertex<K, V>? = null
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
        var new = false
        var vertex: AVLVertex<K, V> = root ?: AVLVertex(key, value).also {
            root = it
            new = true
        }
        while (vertex.key != key) {
            if (vertex.key > key) {
                vertex = vertex.left ?: AVLVertex(key, value).also {
                    vertex.left = it
                    new = true
                }
            } else {
                vertex = vertex.right ?: AVLVertex(key, value).also {
                    vertex.right = it
                    new = true
                }
            }
        }
        return if (new) {
            ++size
            ++modCount
            balanceUp(vertex)
            null
        } else vertex.setValue(value)
    }

    override operator fun set(key: K, value: V) {
        put(key, value)
    }

    override fun remove(key: K): V? {
        var parent: AVLVertex<K, V>? = null
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
        var parent: AVLVertex<K, V>? = null
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

    private fun removeVertex(vertex: AVLVertex<K, V>): AVLVertex<K, V>? {
        if (vertex.left == null && vertex.right == null) {
            return null
        } else if (vertex.left == null) {
            return vertex.right
        } else if (vertex.right == null) {
            return vertex.left
        }

        val successor = findMin(vertex.right!!)
        vertex.key = successor.key
        vertex.value = successor.value
        vertex.right = removeVertex(successor)

        balanceUp(vertex)

        return vertex
    }

    private fun findMin(vertex: AVLVertex<K, V>): AVLVertex<K, V> {
        var current = vertex
        while (current.left != null) {
            current = current.left!!
        }
        return current
    }

    private fun balanceUp(vertex: AVLVertex<K, V>) {
        var current: AVLVertex<K, V>? = vertex

        while (current != null) {
            val balanceFactor = getBalanceFactor(current)
            if (balanceFactor > 1) {
                if (getBalanceFactor(current.left!!) >= 0) {
                    current = rotateRight(current)
                } else {
                    current.left = rotateLeft(current.left!!)
                    current = rotateRight(current)
                }
            } else if (balanceFactor < -1) {
                if (getBalanceFactor(current.right!!) <= 0) {
                    current = rotateLeft(current)
                } else {
                    current.right = rotateRight(current.right!!)
                    current = rotateLeft(current)
                }
            }
            current = current.parent
        }
    }

    private fun rotateLeft(vertex: AVLVertex<K, V>): AVLVertex<K, V> {
        val rightChild = vertex.right!!
        vertex.right = rightChild.left
        rightChild.left?.parent = vertex
        rightChild.left = vertex
        rightChild.parent = vertex.parent
        vertex.parent = rightChild

        updateHeight(vertex)
        updateHeight(rightChild)

        return rightChild
    }

    private fun rotateRight(vertex: AVLVertex<K, V>): AVLVertex<K, V> {
        val leftChild = vertex.left!!
        vertex.left = leftChild.right
        leftChild.right?.parent = vertex
        leftChild.right = vertex
        leftChild.parent = vertex.parent
        vertex.parent = leftChild

        updateHeight(vertex)
        updateHeight(leftChild)

        return leftChild
    }

    private fun updateHeight(vertex: AVLVertex<K, V>) {
        val leftHeight = getHeight(vertex.left)
        val rightHeight = getHeight(vertex.right)
        vertex.height = 1 + maxOf(leftHeight, rightHeight)
    }

    private fun getBalanceFactor(vertex: AVLVertex<K, V>): Int {
        val leftHeight = getHeight(vertex.left)
        val rightHeight = getHeight(vertex.right)
        return leftHeight - rightHeight
    }

    private fun getHeight(vertex: AVLVertex<K, V>?): Int {
        return vertex?.height ?: 0
    }

    protected class AVLVertex<K, V>(
            override var key: K,
            override var value: V,
            override var left: AVLVertex<K, V>? = null,
            override var right: AVLVertex<K, V>? = null,
            var parent: AVLVertex<K, V>? = null,
            var height: Int = 1
    ) : PublicVertex<K, V> {
        override fun setValue(newValue: V): V = value.also { value = newValue }
    }

    protected class AVLTreeIterator<K, V>(
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
