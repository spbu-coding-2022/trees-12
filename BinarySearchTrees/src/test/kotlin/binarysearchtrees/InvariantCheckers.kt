package binarysearchtrees

fun <K : Comparable<K>, V> isBinarySearchTree(tree: BinarySearchTree<K, V>): Boolean {
    fun checkBSTInvariant(vertex: BinarySearchTree.Vertex<K, V>): Boolean {
        return vertex.left?.let { vertex.key > it.key && checkBSTInvariant(it) } ?: true
                && vertex.right?.let { vertex.key < it.key && checkBSTInvariant(it) } ?: true
    }
    return tree.getRoot()?.let { checkBSTInvariant(it) } ?: true
}