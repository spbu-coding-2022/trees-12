package binarysearchtrees.avltree

fun <K : Comparable<K>, V> isAVLTree(tree: AVLTree<K, V>): Boolean {
    fun checkAVLInvariant(vertex: AVLTree.Vertex<K, V>): Boolean {
        return vertex.left?.let { vertex.key > it.key && checkAVLInvariant(it) } ?: true
                && vertex.right?.let { vertex.key < it.key && checkAVLInvariant(it) } ?: true
    }
    return tree.getRoot()?.let { checkAVLInvariant(it) } ?: true
}
