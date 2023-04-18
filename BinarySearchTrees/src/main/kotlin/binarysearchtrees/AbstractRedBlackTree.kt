package binarysearchtrees

abstract class AbstractRedBlackTree<K : Comparable<K>, V> : RedBlackTree<K, V> {
    override var size: Int = 0
    protected var root: Vertex<K, V>? = null
    protected var modCount: Int = 0

    override fun isEmpty(): Boolean = (root == null)

    override fun clear() {
        size = 0
        root = null
        ++modCount
    }

    override fun getRoot(): RedBlackTree.MutableVertex<K, V>? = root

    override fun iterator(): Iterator<RedBlackTree.MutableVertex<K, V>> {
        return RedBlackTreeIterator(getRoot()) { modCount }
    }

    protected class Vertex<K, V>(
        override val key: K,
        override var value: V,
        override var color: RedBlackTree.Vertex.Color = RedBlackTree.Vertex.Color.RED,
        override var left: Vertex<K, V>? = null,
        override var right: Vertex<K, V>? = null
    ) : RedBlackTree.MutableVertex<K, V> {
        override fun setValue(newValue: V): V = value.also { value = newValue }
    }

    protected class RedBlackTreeIterator<K, V>(
        root: RedBlackTree.MutableVertex<K, V>?,
        private val getModCount: () -> Int
    ) : Iterator<RedBlackTree.MutableVertex<K, V>> {
        private val stack: MutableList<RedBlackTree.MutableVertex<K, V>> = mutableListOf()
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

        override fun next(): RedBlackTree.MutableVertex<K, V> {
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