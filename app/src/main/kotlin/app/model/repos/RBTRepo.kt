package app.model.repos

import binarysearchtrees.redblacktree.RedBlackTree
import binarysearchtrees.redblacktree.Vertex
import binarysearchtrees.redblacktree.Vertex.Color
import org.neo4j.ogm.annotation.*
import org.neo4j.ogm.config.Configuration
import org.neo4j.ogm.session.SessionFactory

class RBTRepository<ValueType>(
    configuration: Configuration,
    private val serializeValue: (ValueType) -> String,
    private val deserializeValue: (String) -> ValueType
) {
    private val sessionFactory = SessionFactory(configuration, "app.model.repos")
    private val session = sessionFactory.openSession()

    fun getNames(): List<String> {
        return session.loadAll(
            TreeEntity::class.java,
            0
        ).map { it.name }
    }

    fun get(name: String): Pair<RedBlackTree<String, ValueType>, String> {
        val treeEntity = session.load(
            TreeEntity::class.java,
            name,
            -1
        )
        return RBT<ValueType>().apply {
            treeEntity.root?.let { buildTree(it, deserializeValue) }
        } to treeEntity.settingsData
    }

    fun set(name: String, tree: RedBlackTree<String, ValueType>, settingsData: String) {
        remove(name)
        session.save(
            TreeEntity(
                name,
                settingsData,
                tree.getRoot()?.toVertexEntity(serializeValue)
            )
        )
    }

    fun remove(name: String): Boolean {
        return session.query(
            "MATCH r = (t:Tree{name : \$NAME})-[*]->() DETACH DELETE r",
            mapOf("NAME" to name)
        ).queryStatistics().containsUpdates()
    }

    private fun Vertex<String, ValueType>.toVertexEntity(serializeValue: (ValueType) -> String): VertexEntity {
        return VertexEntity(
            key,
            serializeValue(value),
            color.toString()[0],
            left?.toVertexEntity(serializeValue),
            right?.toVertexEntity(serializeValue)
        )
    }
}

@NodeEntity("Tree")
data class TreeEntity(
    @Id
    val name: String,

    @Property
    val settingsData: String,

    @Relationship(type = "ROOT")
    val root: VertexEntity?
)

@NodeEntity("Vertex")
data class VertexEntity(
    @Property
    val key: String,

    @Property
    val value: String,

    @Property
    val color: Char,

    @Relationship(type = "LEFT")
    val left: VertexEntity?,

    @Relationship(type = "RIGHT")
    val right: VertexEntity?,
) {
    @Id
    @GeneratedValue
    var id: Long? = null
}

private class RBT<ValueType> : RedBlackTree<String, ValueType>() {
    fun buildTree(vertexEntity: VertexEntity, deserializeValue: (String) -> ValueType) {
        root = vertexEntity.toVertex(deserializeValue)
    }

    private fun VertexEntity.toVertex(deserializeValue: (String) -> ValueType): Vertex<String, ValueType> {
        val vertex = Vertex(
            key,
            deserializeValue(value),
            if (color == 'R') Color.RED else Color.BLACK
        )
        ++size
        ++modCount
        vertex.left = left?.toVertex(deserializeValue)
        vertex.right = right?.toVertex(deserializeValue)
        return vertex
    }
}