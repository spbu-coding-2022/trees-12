package app.controller

import app.model.repos.BSTRepository
import app.model.repos.RBTRepository
import app.model.repos.AVLRepository
import app.view.Position
import app.view.ScrollDelta
import binarysearchtrees.BinarySearchTree
import binarysearchtrees.binarysearchtree.SimpleBinarySearchTree
import binarysearchtrees.redblacktree.RedBlackTree
import binarysearchtrees.avltree.AVLTree
import org.neo4j.ogm.config.Configuration

class Repository() {
    private var bstRepo: BSTRepository<Position>
    private var rbtRepo: RBTRepository<Position>
    private var avlRepo: AVLRepository<Position>

    init {
        try {
            val dirPath = "./BSTRepo"
            bstRepo = BSTRepository(
                dirPath,
                { serializePosition(it) },
                { deserializePosition(it) }
            )
        } catch (e: Exception) {
            throw Exception("BSTRepo Init Exception: " + e.message)
        }

        try {
            val configuration = Configuration.Builder()
                .uri("bolt://localhost")
                .credentials("neo4j", "qwerty")
                .build()
            rbtRepo = RBTRepository(
                configuration,
                { serializePosition(it) },
                { deserializePosition(it) }
            )
        } catch (e: Exception) {
            throw Exception("RBTRepo Init Exception: " + e.message)
        }

        try {
            val dirPath = "./AVLRepo"
            avlRepo = AVLRepository(
                    dirPath,
                    { serializePosition(it) },
                    { deserializePosition(it) }
            )
        } catch (e: Exception) {
            throw Exception("AVLRepo Init Exception: " + e.message)
        }
    }

    fun getNames(treeType: TreeType): List<String> {
        return when(treeType) {
            TreeType.BST -> {
                try {
                    bstRepo.getNames()
                } catch (e: Exception) {
                    throw Exception("BSTRepo GetNames Exception: " + e.message)
                }
            }

            TreeType.RBT -> {
                try {
                    rbtRepo.getNames()
                } catch (e: Exception) {
                    throw Exception("RBTRepo GetNames Exception: " + e.message)
                }
            }

            else -> {
                try {
                    avlRepo.getNames()
                } catch (e: Exception) {
                    throw Exception("AVLRepo GetNames Exception: " + e.message)
                }
            }
        }
    }

    fun save(name: String, tree: BinarySearchTree<String, Position>, scrollDelta: ScrollDelta) {
        val settingsData = serializeScrollDelta(scrollDelta)

        when (tree) {
            is SimpleBinarySearchTree<String, Position> -> {
                try {
                    bstRepo.set(name, tree, settingsData)
                } catch (e: Exception) {
                    throw Exception("BSTRepo Save Exception: " + e.message)
                }
            }

            is RedBlackTree<String, Position> -> {
                try {
                    rbtRepo.set(name, tree, settingsData)
                } catch (e: Exception) {
                    throw Exception("RBTRepo Save Exception: " + e.message)
                }
            }

            is AVLTree<String, Position> -> {
                try {
                    avlRepo.set(name, tree, settingsData)
                } catch (e: Exception) {
                    throw Exception("AVLRepo Save Exception: " + e.message)
                }
            }
        }
    }

    fun get(name: String, treeType: TreeType): Pair<BinarySearchTree<String, Position>, ScrollDelta> {
        val (tree: BinarySearchTree<String, Position>, settingsData: String) = when (treeType) {
            TreeType.BST -> {
                try {
                    bstRepo.get(name)
                } catch (e: Exception) {
                    throw Exception("BSTRepo Get Exception: " + e.message)
                }
            }

            TreeType.RBT -> {
                try {
                    rbtRepo.get(name)
                } catch (e: Exception) {
                    throw Exception("RBTRepo Get Exception: " + e.message)
                }
            }

            TreeType.AVL -> {
                try {
                    avlRepo.get(name)
                } catch (e: Exception) {
                    throw Exception("AVLRepo Get Exception: " + e.message)
                }
            }
        }
        return Pair(tree, deserializeScrollDelta(settingsData))
    }

    fun delete(name: String, tree: BinarySearchTree<String, Position>): Boolean {
        return when (tree) {
            is SimpleBinarySearchTree<String, Position> -> {
                try {
                    bstRepo.remove(name)
                } catch (e: Exception) {
                    throw Exception("BSTRepo Delete Exception: " + e.message)
                }
            }

            is RedBlackTree<String, Position> -> {
                try {
                    rbtRepo.remove(name)
                } catch (e: Exception) {
                    throw Exception("RBTRepo Delete Exception: " + e.message)
                }
            }

            is AVLTree<String, Position> -> {
                try {
                    avlRepo.remove(name)
                } catch (e: Exception) {
                    throw Exception("AVLRepo Delete Exception: " + e.message)
                }
            }

            else -> { throw Exception("Undefined tree type.") }
        }
    }

    enum class TreeType { BST, RBT, AVL }
}