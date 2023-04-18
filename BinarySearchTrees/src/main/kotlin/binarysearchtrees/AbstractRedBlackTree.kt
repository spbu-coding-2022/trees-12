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
        val stack = mutableListOf<Vertex<K, V>>()
        var vertex = root ?: Vertex(key, value, RedBlackTree.Vertex.Color.BLACK).also {
            root = it
            new = true
        }
        while (vertex.key != key) {
            stack.add(vertex)
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

        // balance and return
        return if (new) {
            stack.add(vertex)
            balanceAfterInsert(stack)
            stack.clear()
            ++size
            ++modCount
            null
        } else {
            stack.clear()
            vertex.setValue(value)
        }
    }

    override fun set(key: K, value: V) {
        put(key, value)
    }

    override fun remove(key: K): V? {
        val stack = mutableListOf<Vertex<K, V>>()
        var parentIndex = -1
        var vertex = root
        while (vertex != null && vertex.key != key) {
            stack.add(vertex)
            ++parentIndex
            if (vertex.key > key) {
                vertex = vertex.left
            } else {
                vertex = vertex.right
            }
        }
        val oldValue = vertex?.value
        if (vertex != null) {
            val goner = getGoner(vertex, parentIndex, stack)

            // leaf removal
            stack.add(goner)
            removeLeaf(stack)
            --size
            ++modCount
        }
        stack.clear()
        return oldValue
    }

    override fun remove(key: K, value: V): Boolean {
        val stack = mutableListOf<Vertex<K, V>>()
        var parentIndex = -1
        var vertex = root
        while (vertex != null && vertex.key != key) {
            stack.add(vertex)
            ++parentIndex
            if (vertex.key > key) {
                vertex = vertex.left
            } else {
                vertex = vertex.right
            }
        }
        return if (vertex != null && vertex.value == value) {
            val goner = getGoner(vertex, parentIndex, stack)

            // leaf removal
            stack.add(goner)
            removeLeaf(stack)
            --size
            ++modCount
            stack.clear()
            true
        } else {
            stack.clear()
            false
        }
    }

    private fun rotateLeft(parent: Vertex<K, V>, child: Vertex<K, V>): Vertex<K, V> {
        parent.color = child.color.also { child.color = parent.color }
        parent.right = child.left
        child.left = parent
        return child
    }

    private fun rotateRight(parent: Vertex<K, V>, child: Vertex<K, V>): Vertex<K, V> {
        parent.color = child.color.also { child.color = parent.color }
        parent.left = child.right
        child.right = parent
        return child
    }

    private fun balanceAfterInsert(stack: MutableList<Vertex<K, V>>) {
        val black = RedBlackTree.Vertex.Color.BLACK
        val red = RedBlackTree.Vertex.Color.RED
        var notEnd = true

        var vertex = stack.removeLast()
        while (notEnd && stack.lastOrNull()?.color == red) {
            var parent = stack.removeLast()
            val grandparent = stack.removeLast()
            if (grandparent.left == parent) {
                if (grandparent.right?.color == red) {
                    grandparent.color = red
                    grandparent.right?.color = black
                    parent.color = black
                    vertex = grandparent
                } else {
                    notEnd = false
                    if (parent.right == vertex) {
                        parent = rotateLeft(parent, vertex)
                        grandparent.left = parent
                    }
                    if (root == grandparent) {
                        root = rotateRight(grandparent, parent)
                    } else {
                        val ggparent = stack.last()
                        if (ggparent.left == grandparent) {
                            ggparent.left = rotateRight(grandparent, parent)
                        } else {
                            ggparent.right = rotateRight(grandparent, parent)
                        }
                    }
                }
            } else {
                if (grandparent.left?.color == red) {
                    grandparent.color = red
                    grandparent.left?.color = black
                    parent.color = black
                    vertex = grandparent
                } else {
                    notEnd = false
                    if (parent.left == vertex) {
                        parent = rotateRight(parent, vertex)
                        grandparent.right = parent
                    }
                    if (root == grandparent) {
                        root = rotateLeft(grandparent, parent)
                    } else {
                        val ggparent = stack.last()
                        if (ggparent.left == grandparent) {
                            ggparent.left = rotateLeft(grandparent, parent)
                        } else {
                            ggparent.right = rotateLeft(grandparent, parent)
                        }
                    }
                }
            }
        }

        root?.color = black
    }

    private fun getGoner(vertex: Vertex<K, V>, parentIndex: Int, stack: MutableList<Vertex<K, V>>): Vertex<K, V> {
        return vertex.left?.let { left ->
            vertex.right?.let { right ->
                // swap with the vertex that is next by key
                var nextParent: Vertex<K, V> = vertex
                stack.add(nextParent)
                var next = right
                while (next.left != null) {
                    next = next.left?.let {
                        nextParent = next
                        stack.add(nextParent)
                        it
                    } ?: next
                }
                vertex.color = next.color.also { next.color = vertex.color }
                if (root == vertex) {
                    root = next
                } else {
                    val parent = stack[parentIndex]
                    if (parent.left == vertex) {
                        parent.left = next
                    } else {
                        parent.right = next
                    }
                }
                stack[parentIndex + 1] = next
                next.left = left
                vertex.left = null
                vertex.right = next.right
                if (nextParent == vertex) {
                    next.right = vertex
                    vertex.right?.let { right ->
                        // swap with the single red leaf
                        vertex.color = right.color.also { right.color = vertex.color }
                        next.right = right
                        right.left = vertex
                        vertex.left = null
                        vertex.right = null
                        stack.add(right)
                    }
                } else {
                    next.right = right
                    nextParent.left = vertex
                    vertex.right?.let { right ->
                        // swap with the single red leaf
                        vertex.color = right.color.also { right.color = vertex.color }
                        nextParent.left = right
                        right.left = vertex
                        vertex.left = null
                        vertex.right = null
                        stack.add(right)
                    }
                }
                vertex
            } ?: left.let { left ->
                // swap with the single red leaf
                vertex.color = left.color.also { left.color = vertex.color }
                if (root == vertex) {
                    root = left
                } else {
                    val parent = stack.last()
                    if (parent.left == vertex) {
                        parent.left = left
                    } else {
                        parent.right = left
                    }
                }
                left.right = vertex
                vertex.left = null
                vertex.right = null
                stack.add(left)
                vertex
            }
        } ?: vertex.right?.let { right ->
            // swap with the single red leaf
            vertex.color = right.color.also { right.color = vertex.color }
            if (root == vertex) {
                root = right
            } else {
                val parent = stack.last()
                if (parent.left == vertex) {
                    parent.left = right
                } else {
                    parent.right = right
                }
            }
            right.left = vertex
            vertex.left = null
            vertex.right = null
            stack.add(right)
            vertex
        } ?: vertex // vertex is already a leaf
    }

    private fun removeLeaf(stack: MutableList<Vertex<K, V>>) {
        val black = RedBlackTree.Vertex.Color.BLACK
        val red = RedBlackTree.Vertex.Color.RED
        var notEnd = true

        fun balanceLeft(parent: Vertex<K, V>): Vertex<K, V> {
            fun balanceBlackRightBrother(parent: Vertex<K, V>): Vertex<K, V> {
                val brother = parent.right
                if (brother != null) {
                    return brother.right?.let { rightCousin ->
                        if (rightCousin.color == red) {
                            notEnd = false
                            rightCousin.color = black
                            rotateLeft(parent, brother)
                        } else {
                            brother.left?.let { leftCousin ->
                                if (leftCousin.color == red) {
                                    notEnd = false
                                    parent.right = rotateRight(brother, leftCousin)
                                    brother.color = black
                                    rotateLeft(parent, leftCousin)
                                } else {
                                    brother.color = red
                                    if (parent.color == red) {
                                        notEnd = false
                                        parent.color = black
                                    }
                                    parent
                                }
                            } ?: parent.let {
                                brother.color = red
                                if (it.color == red) {
                                    notEnd = false
                                    parent.color = black
                                }
                                it
                            }
                        }
                    } ?: brother.left?.let { leftCousin ->
                        if (leftCousin.color == red) {
                            notEnd = false
                            parent.right = rotateRight(brother, leftCousin)
                            brother.color = black
                            rotateLeft(parent, leftCousin)
                        } else {
                            brother.color = red
                            if (parent.color == red) {
                                notEnd = false
                                parent.color = black
                            }
                            parent
                        }
                    } ?: parent.let {
                        brother.color = red
                        if (it.color == red) {
                            notEnd = false
                            parent.color = black
                        }
                        it
                    }
                } else {
                    throw Exception("Deletion error: height of right subtree must be at least 1")
                }
            }

            return parent.right?.let { right ->
                if (right.color == red) {
                    val newParent = rotateLeft(parent, right)
                    newParent.left = balanceBlackRightBrother(parent)
                    if (notEnd) {
                        balanceBlackRightBrother(newParent)
                    } else {
                        newParent
                    }
                } else {
                    balanceBlackRightBrother(parent)
                }
            } ?: balanceBlackRightBrother(parent)
        }

        fun balanceRight(parent: Vertex<K, V>): Vertex<K, V> {
            fun balanceBlackLeftBrother(parent: Vertex<K, V>): Vertex<K, V> {
                val brother = parent.left
                if (brother != null) {
                    return brother.left?.let { leftCousin ->
                        if (leftCousin.color == red) {
                            notEnd = false
                            leftCousin.color = black
                            rotateRight(parent, brother)
                        } else {
                            brother.right?.let { rightCousin ->
                                if (rightCousin.color == red) {
                                    notEnd = false
                                    parent.left = rotateLeft(brother, rightCousin)
                                    brother.color = black
                                    rotateRight(parent, rightCousin)
                                } else {
                                    brother.color = red
                                    if (parent.color == red) {
                                        notEnd = false
                                        parent.color = black
                                    }
                                    parent
                                }
                            } ?: parent.let {
                                brother.color = red
                                if (it.color == red) {
                                    notEnd = false
                                    parent.color = black
                                }
                                it
                            }
                        }
                    } ?: brother.right?.let { rightCousin ->
                        if (rightCousin.color == red) {
                            notEnd = false
                            parent.left = rotateLeft(brother, rightCousin)
                            brother.color = black
                            rotateRight(parent, rightCousin)
                        } else {
                            brother.color = red
                            if (parent.color == red) {
                                notEnd = false
                                parent.color = black
                            }
                            parent
                        }
                    } ?: parent.let {
                        brother.color = red
                        if (it.color == red) {
                            notEnd = false
                            parent.color = black
                        }
                        it
                    }
                } else {
                    throw Exception("Deletion error: height of left subtree must be at least 1")
                }
            }

            return parent.left?.let { left ->
                if (left.color == red) {
                    val newParent = rotateRight(parent, left)
                    newParent.right = balanceBlackLeftBrother(parent)
                    if (notEnd) {
                        balanceBlackLeftBrother(newParent)
                    } else {
                        newParent
                    }
                } else {
                    balanceBlackLeftBrother(parent)
                }
            } ?: balanceBlackLeftBrother(parent)
        }

        val goner = stack.removeLast()
        if (goner.color == red) {
            val parent = stack.last()
            if (parent.left == goner) {
                parent.left = null
            } else {
                parent.right = null
            }
        } else {
            // removal black leaf
            if (root == goner) {
                root = null
            } else {
                var parent = stack.removeLast()
                if (root == parent) {
                    if (parent.left == goner) {
                        parent.left = null
                        parent = balanceLeft(parent)
                    } else {
                        parent.right = null
                        parent = balanceRight(parent)
                    }
                    root = parent
                } else {
                    val grandparent = stack.last()
                    if (grandparent.left == parent) {
                        if (parent.left == goner) {
                            parent.left = null
                            parent = balanceLeft(parent)
                        } else {
                            parent.right = null
                            parent = balanceRight(parent)
                        }
                        grandparent.left = parent
                    } else {
                        if (parent.left == goner) {
                            parent.left = null
                            parent = balanceLeft(parent)
                        } else {
                            parent.right = null
                            parent = balanceRight(parent)
                        }
                        grandparent.right = parent
                    }
                }
                stack.add(parent)

                while (notEnd && stack.size >= 2) {
                    val vertex = stack.removeLast()
                    var parent = stack.removeLast()
                    if (root == parent) {
                        if (parent.left == vertex) {
                            parent = balanceLeft(parent)
                        } else {
                            parent = balanceRight(parent)
                        }
                        root = parent
                    } else {
                        val grandparent = stack.last()
                        if (grandparent.left == parent) {
                            if (parent.left == vertex) {
                                parent = balanceLeft(parent)
                            } else {
                                parent = balanceRight(parent)
                            }
                            grandparent.left = parent
                        } else {
                            if (parent.left == vertex) {
                                parent = balanceLeft(parent)
                            } else {
                                parent = balanceRight(parent)
                            }
                            grandparent.right = parent
                        }
                    }
                    stack.add(parent)
                }
                root?.color = black
            }
        }
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