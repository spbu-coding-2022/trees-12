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

// extension functions

fun <K : Comparable<K>, V> BinarySearchTree<K, V>.forEach(action: (BinarySearchTree.MutableVertex<K, V>) -> Unit) {
    fun traversalInOrder(vertex: BinarySearchTree.MutableVertex<K, V>?) {
        if (vertex == null) {
            return
        } else {
            traversalInOrder(vertex.left)
            action(vertex)
            traversalInOrder(vertex.right)
        }
    }
    traversalInOrder(this.getRoot())
}

fun <K : Comparable<K>, V> BinarySearchTree<K, V>.toList(): List<Pair<K, V>> {
    val list = mutableListOf<Pair<K, V>>()
    this.forEach { list.add(Pair(it.key, it.value)) }
    return list
}