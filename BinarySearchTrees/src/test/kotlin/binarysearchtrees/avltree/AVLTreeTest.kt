package binarysearchtrees.avltree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.random.Random

class AVLTreeTest {
    private val randomizer = Random(100)
    private val elementsCount = 1000
    private val values = Array(elementsCount) { Pair(randomizer.nextInt(), randomizer.nextInt()) }
    private lateinit var tree: AVLTree<Int, Int>

    @BeforeEach
    fun init() {
        tree = AVLTree()
    }

    @Test
    fun `Function put doesn't violate the invariant`() {
        values.forEach {
            tree.insert(it.first, it.second)
            assertTrue(isAVLTree(tree))
        }
    }

    @Test
    fun `Function put adds all elements with unique keys`() {
        values.forEach { tree.insert(it.first, 245) }
        values.forEach { tree.insert(it.first, it.second) }
        val listOfPairKeyValue = mutableListOf<Pair<Int, Int>>()
        assertEquals(
            values.reversed().distinctBy { it.first }.sortedBy { it.first },
            listOfPairKeyValue
        )
        assertEquals(values.distinctBy { it.first }.size, tree.size)
    }

    @ParameterizedTest(name = "Function get returns correct value for key {0}")
    @ValueSource(ints = [9, 20, 32, 81, 77, 94, -10, -15])
    fun `Function get returns correct value`(key: Int) {
        values.forEach { tree.insert(it.first, it.second) }

        val expected = key * 198
        tree[key] = expected
        assertEquals(expected, tree.search(key))

        tree.delete(key)
        assertEquals(null, tree.search(key))
    }

    @ParameterizedTest
    @ValueSource(ints = [9, 20, 32, 81, 77, 94, -10, -15])
    fun `Function remove deletes the some element correctly`(key: Int) {
        values.forEach { tree.insert(it.first, it.second) }

        var value = key * 198
        tree[key] = value
        var size = tree.size - 1
        assertEquals(value, tree.delete(key))
        assertEquals(null, tree.delete(key))
        assertEquals(null, tree[key])
        assertEquals(size, tree.size)
        assertTrue(isAVLTree(tree))

        value = key * 95
        tree[key] = value
        size = tree.size - 1
        assertEquals(null, tree[key])
        assertEquals(size, tree.size)
        assertTrue(isAVLTree(tree))
    }

    @Test
    fun `Function remove deletes the existing element correctly`() {
        values.forEach { tree.insert(it.first, it.second) }

        val elements = mutableListOf<Pair<Int, Int>>()
        values.reversed().distinctBy { it.first }.forEach { elements.add(it) }
        elements.shuffle()
        for (i in 0 until elements.size step 20) {
            val key = elements[i].first
            val value = elements[i].second
            val size = tree.size - 1
            assertEquals(value, tree.delete(key))
            assertEquals(null, tree[key])
            assertEquals(size, tree.size)
            assertTrue(isAVLTree(tree))
        }
    }

    @Test
    fun `Function remove deletes the root element correctly`() {
        values.forEach { tree.insert(it.first, it.second) }

        val value = 45
        var oldKey = tree.getRoot()?.let {
            it.value
            it.key
        } ?: -25
        var size = tree.size - 1
        assertEquals(value, tree.delete(oldKey))
        assertNotEquals(oldKey, tree.getRoot()?.key)
        assertEquals(size, tree.size)
        assertTrue(isAVLTree(tree))

        oldKey = tree.getRoot()?.let {
            it.value
            it.key
        } ?: -25
        size = tree.size - 1
        assertNotEquals(oldKey, tree.getRoot()?.key)
        assertEquals(size, tree.size)
        assertTrue(isAVLTree(tree))
    }

    @Test
    fun `Function clear makes tree empty`() {
        values.forEach { tree.insert(it.first, it.second) }

        tree.clear()
        assertTrue(tree.isEmpty())
        assertNull(tree.getRoot())
        assertEquals(0, tree.size)
    }
}