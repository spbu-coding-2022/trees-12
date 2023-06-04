package binarysearchtrees.redblacktree

fun <K : Comparable<K>, V> isRedBlackTree(tree: RedBlackTree<K, V>): Boolean {
    var f = true

    fun checkRBTInvariant(vertex: Vertex<K, V>): Int {
        if (vertex.color == Vertex.Color.RED) {
            f = f && (vertex.left?.color != Vertex.Color.RED)
            f = f && (vertex.right?.color != Vertex.Color.RED)
        }
        val leftBlackHeight = vertex.left?.let {
            f = f && (vertex.key > it.key)
            checkRBTInvariant(it)
        } ?: 1
        val rightBlackHeight = vertex.right?.let {
            f = f && (vertex.key < it.key)
            checkRBTInvariant(it)
        } ?: 1
        f = f && (leftBlackHeight == rightBlackHeight)
        return if (vertex.color == Vertex.Color.BLACK)
            leftBlackHeight + 1
        else leftBlackHeight
    }

    return tree.getRoot()?.let {
        f = (it.color == Vertex.Color.BLACK)
        checkRBTInvariant(it)
        f
    } ?: true
}