package app

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import app.view.MainWindow
import app.view.Position
import app.view.defaultVertexSize
import app.view.setTreePositions
import binarysearchtrees.binarysearchtree.SimpleBinarySearchTree
import binarysearchtrees.redblacktree.RedBlackTree

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
            //
            val tree = RedBlackTree<String, Position>()
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
            MainWindow(tree, "Tree", ::exitApplication)
        }
    }
}