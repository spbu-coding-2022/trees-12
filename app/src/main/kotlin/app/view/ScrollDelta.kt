package app.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp

class ScrollDelta(
    x: Dp,
    y: Dp
) {
    var x by mutableStateOf(x)
    var y by mutableStateOf(y)
}