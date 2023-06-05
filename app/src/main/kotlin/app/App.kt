package app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import app.controller.Repository
import app.view.*
import binarysearchtrees.BinarySearchTree
import binarysearchtrees.binarysearchtree.SimpleBinarySearchTree
import binarysearchtrees.redblacktree.RedBlackTree
import binarysearchtrees.avltree.AVLTree

fun main() {
    application {
        MaterialTheme(
                colorScheme = MaterialTheme.colorScheme.copy(
                        primary = Color(162, 32, 240),
                        secondary = Color.Magenta,
                        tertiary = Color(164, 0, 178),
                        background = Color(240, 240, 240)
                )
        ) {
            val repo = Repository()

            var isEditorMode by remember { mutableStateOf(false) }

            var tree: BinarySearchTree<String, Position>

            if (!isEditorMode) {
                SelectionWindow(
                        { repo.getNames(it) },
                        { name, type ->
                            if (repo.getNames(type).contains(name)) {
                                repo.get(name, type)
                            } else {
                                tree = when(type) {
                                    Repository.TreeType.BST -> SimpleBinarySearchTree()
                                    Repository.TreeType.RBT -> RedBlackTree()
                                    else -> AVLTree()
                                }
                            }
                            isEditorMode = true
                        },
                        ::exitApplication
                )
            } else {
                //
                tree = AVLTree<String, Position>()
                tree["1"] = Position(0.dp, 0.dp)
                tree["2"] = Position(0.dp, 0.dp)
                tree["3"] = Position(0.dp, 0.dp)
                tree["4"] = Position(0.dp, 0.dp)
                tree["5"] = Position(0.dp, 0.dp)
                tree["6"] = Position(0.dp, 0.dp)
                tree["7"] = Position(0.dp, 0.dp)
                tree["8"] = Position(0.dp, 0.dp)
                tree["9"] = Position(0.dp, 0.dp)
                tree["10"] = Position(0.dp, 0.dp)
                //
                setTreePositions(tree, defaultVertexSize, DpOffset(10.dp, 10.dp))
                //
                MainWindow(tree, "Tree", { isEditorMode = false })
            }
        }
    }
}