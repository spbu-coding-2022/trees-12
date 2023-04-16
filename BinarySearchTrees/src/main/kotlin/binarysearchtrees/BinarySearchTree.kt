package binarysearchtrees

interface BinarySearchTree<K : Comparable<K>, V> {
    val size: Int

    fun isEmpty(): Boolean

    fun clear()

    fun getRoot(): MutableVertex<K, V>?

    operator fun get(key: K): V?

    fun put(key: K, value: V): V?

    operator fun set(key: K, value: V)

    fun remove(key: K): V?

    fun remove(key: K, value: V): Boolean

    operator fun iterator(): Iterator<MutableVertex<K, V>>

    //for extension functions like all, any, ...
    interface Vertex<K, V> {
        val key: K
        val value: V
        val left: Vertex<K, V>?
        val right: Vertex<K, V>?
    }

    interface MutableVertex<K, V> : Vertex<K, V> {
        override val left: MutableVertex<K, V>?
        override val right: MutableVertex<K, V>?

        fun setValue(newValue: V): V
    }
}