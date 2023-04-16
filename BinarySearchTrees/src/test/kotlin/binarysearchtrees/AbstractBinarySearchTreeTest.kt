package binarysearchtrees

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.random.Random

class AbstractBinarySearchTreeTest {
    private val randomizer = Random(100)
    private val elementsCount = 1000
    private val values = Array(elementsCount) { Pair(randomizer.nextInt(), randomizer.nextInt()) }
    private lateinit var tree: AbstractBinarySearchTree<Int, Int>

    @BeforeEach
    fun init() {
        tree = object : AbstractBinarySearchTree<Int, Int>() {}
    }

    @Test
    fun `Function put doesn't violate the invariant`() {
        values.forEach {
            tree.put(it.first, it.second)
            assertTrue(isBinarySearchTree(tree))
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
    }

    @ParameterizedTest
    @ValueSource(ints = [10, 21, 32, 43, 54, 65, -10, -15])
    fun `Functions of iterator throws exceptions after change tree`(key: Int) {
        values.forEach { tree.put(it.first, it.second) }

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
}