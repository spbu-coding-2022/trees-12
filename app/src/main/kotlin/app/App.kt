package app

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.application
import app.view.MainWindow

fun main() {
    application {
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                primary = Color.Magenta,
                secondary = Color(162, 32, 240)
            )
        ) {
            MainWindow(::exitApplication)
        }
    }
}