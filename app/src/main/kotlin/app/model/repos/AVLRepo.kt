package app.model.repos

import binarysearchtrees.avltree.AVLTree
import binarysearchtrees.avltree.Vertex
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class AVLRepository<ValueType>(
        private val dirPath: String,
        private val serializeValue: (ValueType) -> String,
        private val deserializeValue: (String) -> ValueType
) {
    init {
        File(dirPath).mkdirs()
    }

    fun getNames(): List<String> {
        return File(dirPath).listFiles { it ->
            it.name.endsWith(".json")
        }?.map { it.name.dropLast(5) } ?: listOf()
    }

    fun get(name: String): Pair<AVLTree<String, ValueType>, String> {
        val jsonTree = Json.decodeFromString<AVLJsonTree>(File(dirPath, "$name.json").readText())
        return AVL<ValueType>().apply {
            jsonTree.root?.let { buildTree(it, deserializeValue) }
        } to jsonTree.settingsData
    }

    fun set(name: String, tree: AVLTree<String, ValueType>, settingsData: String) {
        val file = File(dirPath, "$name.json")
        file.createNewFile()
        file.writeText(Json.encodeToString(AVLJsonTree(settingsData, tree.getRoot()?.toJsonNode(serializeValue))))
    }

    fun remove(name: String): Boolean = File(dirPath, "$name.json").delete()

    private fun Vertex<String, ValueType>.toJsonNode(serializeValue: (ValueType) -> String): AVLJsonVertex {
        return AVLJsonVertex(
                key,
                serializeValue(value),
                left?.toJsonNode(serializeValue),
                right?.toJsonNode(serializeValue)
        )
    }
}

@Serializable
data class AVLJsonTree(
        val settingsData: String,
        val root: AVLJsonVertex?
)

@Serializable
data class AVLJsonVertex(
        val key: String,
        val value: String,
        val left: AVLJsonVertex?,
        val right: AVLJsonVertex?,
)

private class AVL<ValueType> : AVLTree<String, ValueType>() {
    fun buildTree(jsonVertex: AVLJsonVertex, deserializeValue: (String) -> ValueType) {
        root = jsonVertex.toVertex(deserializeValue)
    }

    private fun AVLJsonVertex.toVertex(deserializeValue: (String) -> ValueType): Vertex<String, ValueType> {
        val vertex = Vertex(key, deserializeValue(value))
        ++size
        ++modCount
        vertex.left = left?.toVertex(deserializeValue)
        vertex.right = right?.toVertex(deserializeValue)
        return vertex
    }
}