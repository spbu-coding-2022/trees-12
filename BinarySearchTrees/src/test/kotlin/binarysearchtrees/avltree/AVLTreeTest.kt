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
            tree.put(it.first, it.second)
            assertTrue(isAVLTree(tree))
        }
    }

    @Test
    fun `Function put adds all elements with unique keys`() {
        values.forEach { tree.put(it.first, it.second) }
        val listOfPairKeyValue = mutableListOf<Pair<Int, Int>>()
        for (it in tree) {
            listOfPairKeyValue.add(Pair(it.key, it.value))
        }
        assertEquals(
                values.reversed().distinctBy { it.first }.sortedBy { it.first },
                listOfPairKeyValue
        )
        assertEquals(values.distinctBy { it.first }.size, tree.size)
    }

    @ParameterizedTest
    @ValueSource(ints = [10, 21, 32, 43, 54, 65, -10, -15])
    fun `Functions of iterator throws exceptions after change tree`(key: Int) {
        values.forEach { tree.put(it.first, it.second) }
        tree.put(key, 69)

        var iterator = tree.iterator()
        tree.remove(key)
        assertThrows(ConcurrentModificationException::class.java) { iterator.hasNext() }

        iterator = tree.iterator()
        tree.put(key, key * 100)
        assertThrows(ConcurrentModificationException::class.java) { iterator.next() }

        iterator = tree.iterator()
        tree.remove(key, key * 100)
        assertThrows(ConcurrentModificationException::class.java) { iterator.next() }

        iterator = tree.iterator()
        tree[key] = key * 100
        assertThrows(ConcurrentModificationException::class.java) { iterator.hasNext() }

        iterator = tree.iterator()
        tree.clear()
        assertThrows(ConcurrentModificationException::class.java) { iterator.hasNext() }

        iterator = tree.iterator()
        assertThrows(NoSuchElementException::class.java) { iterator.next() }
    }

    @ParameterizedTest(name = "Function get returns correct value for key {0}")
    @ValueSource(ints = [9, 20, 32, 81, 77, 94, -10, -15])
    fun `Function get returns correct value`(key: Int) {
        values.forEach { tree.put(it.first, it.second) }

        val expected = key * 198
        tree[key] = expected
        assertEquals(expected, tree.get(key))

        tree.remove(key)
        assertEquals(null, tree.get(key))
    }

    @ParameterizedTest
    @ValueSource(ints = [9, 20, 32, 81, 77, 94, -10, -15])
    fun `Function remove deletes the some element correctly`(key: Int) {
        values.forEach { tree.put(it.first, it.second) }

        var value = key * 198
        tree[key] = value
        var size = tree.size - 1
        assertEquals(value, tree.remove(key))
        assertEquals(null, tree.remove(key))
        assertEquals(null, tree[key])
        assertEquals(size, tree.size)
        assertTrue(isAVLTree(tree))

        value = key * 95
        tree[key] = value
        size = tree.size - 1
        assertFalse(tree.remove(key, value + 10))
        assertTrue(tree.remove(key, value))
        assertFalse(tree.remove(key, value))
        assertEquals(null, tree[key])
        assertEquals(size, tree.size)
        assertTrue(isAVLTree(tree))
    }

    @Test
    fun `Function remove deletes the root element correctly`() {
        values.forEach { tree.put(it.first, it.second) }

        val value = 45
        var oldKey = tree.getRoot()?.let {
            it.setValue(value)
            it.key
        } ?: -25
        var size = tree.size - 1
        assertEquals(value, tree.remove(oldKey))
        assertNotEquals(oldKey, tree.getRoot()?.key)
        assertEquals(size, tree.size)
        assertTrue(isAVLTree(tree))

        oldKey = tree.getRoot()?.let {
            it.setValue(value)
            it.key
        } ?: -25
        size = tree.size - 1
        assertTrue(tree.remove(oldKey, value))
        assertNotEquals(oldKey, tree.getRoot()?.key)
        assertEquals(size, tree.size)
        assertTrue(isAVLTree(tree))
    }

    @Test
    fun `Function clear makes tree empty`() {
        values.forEach { tree.put(it.first, it.second) }

        tree.clear()
        assertTrue(tree.isEmpty())
        assertNull(tree.getRoot())
        assertEquals(0, tree.size)
    }
}