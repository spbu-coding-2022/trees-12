package binarysearchtrees

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
}