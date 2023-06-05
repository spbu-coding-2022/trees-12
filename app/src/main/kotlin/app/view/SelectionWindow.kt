package app.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import app.controller.Repository

@Composable
fun SelectionWindow(
    getNames: (Repository.TreeType) -> List<String>,
    onOpenRequest: (String, Repository.TreeType) -> Unit,
    onCloseRequest: () -> Unit,
    title: String = "Trees-12",
    icon: Painter? = painterResource("treeIcon.png")
) {
    Window(
        onCloseRequest = onCloseRequest,
        title = title,
        icon = icon,
        state = rememberWindowState(
            position = WindowPosition(alignment = Alignment.Center),
            size = DpSize(800.dp, 600.dp),
        ),
        undecorated = true,
        resizable = false
    ) {
        Box(Modifier.fillMaxSize().background(defaultBackground).padding(defaultPadding)) {
            Row(Modifier.fillMaxSize()) {
                var indicator by remember { mutableStateOf(0) }
                Box(Modifier.width(300.dp).fillMaxHeight()) {
                    Column(Modifier.fillMaxSize()) {
                        Box(
                            Modifier.height(290.dp).fillMaxWidth().padding(defaultPadding)
                                .background(
                                    color = defaultBackground,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(
                                    2.dp,
                                    defaultBrush,
                                    RoundedCornerShape(10.dp)
                                )
                        ) {
                            Image(
                                painter = painterResource("treeIcon.png"),
                                contentDescription = "Logotype",
                                modifier = Modifier.height(200.dp)
                                    .width(400.dp)
                                    .padding(50.dp)
                            )

                            Text(
                                "Trees-12", fontSize = 36.sp,
                                modifier = Modifier.padding(top = 175.dp).width(300.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                "Arsene & Artem", fontSize = 19.sp,
                                modifier = Modifier.padding(top = 215.dp).width(300.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }

                        Box(
                            Modifier.fillMaxSize().padding(defaultPadding)
                                .background(
                                    color = defaultBackground,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(
                                    2.dp,
                                    defaultBrush,
                                    RoundedCornerShape(10.dp)
                                )
                        ) {
                            Column(Modifier.selectableGroup()) {
                                Row() {
                                    RadioButton(
                                        selected = (indicator == 1),
                                        onClick = { indicator = 1 }
                                    )
                                    Box(
                                        modifier = Modifier.height(45.dp).width(245.dp)
                                            .padding(top = defaultPadding * 2, bottom = defaultPadding * 2)
                                    ) {
                                        Text(
                                            text = "Binary Search Tree",
                                            style = defaultLargeTextStyle
                                        )
                                    }
                                }
                                Row() {
                                    RadioButton(
                                        selected = (indicator == 2),
                                        onClick = { indicator = 2 }
                                    )
                                    Box(
                                        modifier = Modifier.height(45.dp).width(245.dp)
                                            .padding(top = defaultPadding * 2, bottom = defaultPadding * 2)
                                    ) {
                                        Text(
                                            text = "Red Black Tree",
                                            style = defaultLargeTextStyle
                                        )
                                    }
                                }
                                Row() {
                                    RadioButton(
                                            selected = (indicator == 3),
                                            onClick = { indicator = 3 }
                                    )
                                    Box(
                                            modifier = Modifier.height(45.dp).width(245.dp)
                                                    .padding(top = defaultPadding * 2, bottom = defaultPadding * 2)
                                    ) {
                                        Text(
                                                text = "AVL Tree",
                                                style = defaultLargeTextStyle
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Box(Modifier.fillMaxSize()) {
                    Box(
                        Modifier.fillMaxSize().padding(defaultPadding)
                            .background(
                                color = defaultBackground,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                2.dp,
                                defaultBrush,
                                RoundedCornerShape(10.dp)
                            )
                    ) {
                        Box(Modifier.padding(top = 60.dp).fillMaxWidth().height(550.dp)) {
                            when (indicator) {
                                1 -> Selection({getNames(Repository.TreeType.BST)}, {onOpenRequest(it, Repository.TreeType.BST)})
                                2 -> Selection({getNames(Repository.TreeType.RBT)}, {onOpenRequest(it, Repository.TreeType.RBT)})
                                3 -> Selection({getNames(Repository.TreeType.AVL)}, {onOpenRequest(it, Repository.TreeType.AVL)})
                            }
                        }
                    }
                }
            }
        }

        IconButton(
            onClick = onCloseRequest,
            modifier = Modifier.size(40.dp)
                .offset(740.dp, 20.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        ) {
            Icon(
                Icons.Filled.Close,
                modifier = Modifier.size(50.dp),
                contentDescription = "Close application",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun Selection(
    getNames: () -> List<String>,
    onOpenRequest: (String) -> Unit
) {
    val names = getNames()
    Column(Modifier.fillMaxSize()) {
        var indicator by remember { mutableStateOf(-1) }
        var newName by remember { mutableStateOf("") }


        Box(Modifier.fillMaxWidth().height(80.dp).align(Alignment.CenterHorizontally)) {
            Button(
                onClick = { onOpenRequest(if (indicator == -1) newName else names[indicator]) },
                modifier = Modifier.width(115.dp).height(45.dp).align(Alignment.Center),
                shape = RoundedCornerShape(5.dp),
            ) {
                Text(text = "Open", style = defaultOnPrimaryLargeTextStyle)
            }

        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val stateVertical = rememberScrollState(0)
            val stateHorizontal = rememberScrollState(0)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(stateVertical)
                    .padding(end = 12.dp, bottom = 12.dp)
                    .horizontalScroll(stateHorizontal)
            ) {
                Column(Modifier.selectableGroup()) {
                    Row() {
                        RadioButton(
                            selected = (indicator == -1),
                            onClick = { indicator = -1 }
                        )
                        BasicTextField(
                            value = newName,
                            onValueChange = { if (it.length < 20) newName = it },
                            modifier = Modifier.width(345.dp).height(45.dp)
                                .border(
                                    1.dp,
                                    defaultVVBrush,
                                    RoundedCornerShape(5.dp)
                                )
                                .padding(defaultPadding * 2),
                            textStyle = defaultLargeTextStyle,
                            singleLine = true
                        )
                    }
                    for (i in names.indices) {
                        Row() {
                            RadioButton(
                                selected = (indicator == i),
                                onClick = { indicator = i }
                            )
                            Box(
                                modifier = Modifier.height(45.dp).width(345.dp)
                                    .padding(top = defaultPadding * 2, bottom = defaultPadding * 2)
                            ) {
                                Text(
                                    text = names[i],
                                    style = defaultLargeTextStyle
                                )
                            }
                        }
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd)
                    .fillMaxHeight(),
                adapter = rememberScrollbarAdapter(stateVertical)
            )
            HorizontalScrollbar(
                modifier = Modifier.align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(end = 12.dp),
                adapter = rememberScrollbarAdapter(stateHorizontal)
            )
        }
    }
}