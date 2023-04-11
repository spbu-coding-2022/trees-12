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
        return if (f) null else vertex.setValue(value)
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
        if (parent == null) {
            root = vertex?.let { removeVertex(it) }
        } else {
            if (parent.left == vertex) {
                parent.left = vertex?.let { removeVertex(it) }
            } else {
                parent.right = vertex?.let { removeVertex(it) }
            }
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
}