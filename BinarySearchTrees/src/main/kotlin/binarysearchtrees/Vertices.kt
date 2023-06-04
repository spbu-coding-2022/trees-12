package binarysearchtrees

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