package binarysearchtrees.binarysearchtree

fun <K : Comparable<K>, V> isBinarySearchTree(tree: SimpleBinarySearchTree<K, V>): Boolean {
    fun checkBSTInvariant(vertex: Vertex<K, V>): Boolean {
        return vertex.left?.let { vertex.key > it.key && checkBSTInvariant(it) } ?: true
                && vertex.right?.let { vertex.key < it.key && checkBSTInvariant(it) } ?: true
    }
    return tree.getRoot()?.let { checkBSTInvariant(it) } ?: true
}