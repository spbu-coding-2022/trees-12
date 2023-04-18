package binarysearchtrees

abstract class AbstractBinarySearchTree<K : Comparable<K>, V> : BinarySearchTree<K, V> {
    override var size: Int = 0
    protected var root: Vertex<K, V>? = null
    protected var modCount: Int = 0

    override fun isEmpty(): Boolean = (root == null)

    override fun clear() {
        size = 0
        root = null
        ++modCount
    }

    override fun getRoot(): BinarySearchTree.MutableVertex<K, V>? = root

    override fun iterator(): Iterator<BinarySearchTree.MutableVertex<K, V>> {
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
        var f = false
        var vertex: Vertex<K, V> = root ?: Vertex(key, value).also {
            root = it
            f = true
        }
        while (vertex.key != key) {
            if (vertex.key > key) {
                vertex = vertex.left ?: Vertex(key, value).also {
                    vertex.left = it
                    f = true
                }
            } else {
                vertex = vertex.right ?: Vertex(key, value).also {
                    vertex.right = it
                    f = true
                }
            }
        }
        return if (f) {
            ++size
            ++modCount
            null
        } else vertex.setValue(value)
    }

    override fun set(key: K, value: V) {
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
                root = vertex.let { removeVertex(it) }
            } else {
                if (parent.left == vertex) {
                    parent.left = vertex.let { removeVertex(it) }
                } else {
                    parent.right = vertex.let { removeVertex(it) }
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
                while (next.left != null) {
                    next = next.left?.let {
                        nextParent = next
                        it
                    } ?: next
                }
                if (nextParent == vertex) {
                    next.left = left // vertex.left
                } else {
                    nextParent.left = null
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
    ) : BinarySearchTree.MutableVertex<K, V> {
        override fun setValue(newValue: V): V = value.also { value = newValue }
    }

    protected class BinarySearchTreeIterator<K, V>(
        root: BinarySearchTree.MutableVertex<K, V>?,
        private val getModCount: () -> Int
    ) : Iterator<BinarySearchTree.MutableVertex<K, V>> {
        private val stack: MutableList<BinarySearchTree.MutableVertex<K, V>> = mutableListOf()
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

        override fun next(): BinarySearchTree.MutableVertex<K, V> {
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