package app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

@Composable
fun MainWindow(
    onCloseRequest: () -> Unit,
    title: String = "Trees-12",
    icon: Painter? = painterResource("treeIcon.png"),
    state: WindowState = rememberWindowState(
        position = WindowPosition(alignment = Alignment.Center),
        size = DpSize(800.dp, 600.dp),
    )
) {
    Window(
        onCloseRequest = onCloseRequest,
        title = title,
        icon = icon,
        state = state
    ) {
        window.minimumSize = Dimension(800, 600)
        Box(Modifier.padding(5.dp)) {
            val brush = Brush.linearGradient(listOf(Color(162, 32, 240), Color.Magenta))
            val reversedBrush = Brush.linearGradient(listOf(Color.Magenta, Color(162, 32, 240)))
            Row(Modifier.fillMaxSize()) {
                Box(Modifier.width(300.dp).fillMaxHeight()) {
                    Column(Modifier.fillMaxSize()) {
                        Box(
                            Modifier.height(290.dp).fillMaxWidth().padding(5.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(
                                    2.dp,
                                    brush,
                                    RoundedCornerShape(10.dp)
                                )
                        ) {
                            // place for a logo
                        }

                        Box(
                            Modifier.fillMaxSize().padding(5.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(
                                    2.dp,
                                    brush,
                                    RoundedCornerShape(10.dp)
                                )
                        ) {
                            // place for operation buttons
                        }
                    }

                }
                Box(Modifier.fillMaxSize()) {
                    Box(
                        Modifier.fillMaxSize().padding(5.dp)
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                2.dp,
                                reversedBrush,
                                RoundedCornerShape(10.dp)
                            )
                    ) {
                        // place for drawing a tree
                    }
                }
            }
        }
    }
}