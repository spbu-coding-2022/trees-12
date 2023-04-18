package binarysearchtrees

fun <K : Comparable<K>, V> isBinarySearchTree(tree: BinarySearchTree<K, V>): Boolean {
    fun checkBSTInvariant(vertex: BinarySearchTree.Vertex<K, V>): Boolean {
        return vertex.left?.let { vertex.key > it.key && checkBSTInvariant(it) } ?: true
                && vertex.right?.let { vertex.key < it.key && checkBSTInvariant(it) } ?: true
    }
    return tree.getRoot()?.let { checkBSTInvariant(it) } ?: true
}

fun <K : Comparable<K>, V> isRedBlackTree(tree: RedBlackTree<K, V>): Boolean {
    var f = true

    fun checkRBTInvariant(vertex: RedBlackTree.Vertex<K, V>): Int {
        if (vertex.color == RedBlackTree.Vertex.Color.RED) {
            f = f && (vertex.left?.color != RedBlackTree.Vertex.Color.RED)
            f = f && (vertex.right?.color != RedBlackTree.Vertex.Color.RED)
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
        return if (vertex.color == RedBlackTree.Vertex.Color.BLACK)
            leftBlackHeight + 1
        else leftBlackHeight
    }

    return tree.getRoot()?.let {
        f = (it.color == RedBlackTree.Vertex.Color.BLACK)
        checkRBTInvariant(it)
        f
    } ?: true
}