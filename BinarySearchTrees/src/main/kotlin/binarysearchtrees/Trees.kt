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

fun <K : Comparable<K>, V> redBlackTreeOf(): RedBlackTree<K, V> {
    return object : AbstractRedBlackTree<K, V>() {}
}

fun <K : Comparable<K>, V> redBlackTreeOf(
    vararg args: Pair<K, V>
): RedBlackTree<K, V> {
    val tree = object : AbstractRedBlackTree<K, V>() {}
    for (it in args) {
        tree.put(it.first, it.second)
    }
    return tree
}