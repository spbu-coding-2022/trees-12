package binarysearchtrees

import binarysearchtrees.redblacktree.RedBlackTree

// initializing functions

fun <K : Comparable<K>, V> binarySearchTreeOf(): BinarySearchTree<K, V> {
    return RedBlackTree<K, V>()
}

fun <K : Comparable<K>, V> binarySearchTreeOf(
    vararg args: Pair<K, V>
): BinarySearchTree<K, V> {
    val tree = RedBlackTree<K, V>()
    for (it in args) {
        tree.put(it.first, it.second)
    }
    return tree
}