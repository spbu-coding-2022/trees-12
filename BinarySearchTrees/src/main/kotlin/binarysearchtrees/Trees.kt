package binarysearchtrees

// initializing functions

fun <K : Comparable<K>, V> binarySearchTreeOf(): BinarySearchTree<K, V> {
    return object : AbstractBinarySearchTree<K, V>() {}
}

fun <K : Comparable<K>, V> binarySearchTreeOf(
    vararg args: Pair<K, V>
): BinarySearchTree<K, V> {
    val tree = object : AbstractBinarySearchTree<K, V>() {}
    for (it in args) {
        tree.put(it.first, it.second)
    }
    return tree
}