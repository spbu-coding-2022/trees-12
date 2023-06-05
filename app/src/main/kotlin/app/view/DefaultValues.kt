package app.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val defaultPadding = 5.dp

val defaultScrollDelta = ScrollDelta(0.dp, 0.dp)

const val defaultScrollCf = 75

val defaultVertexSize = 60.dp

val defaultBrush
    @Composable
    get() = Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary))

val defaultVVBrush
    @Composable
    get() = Brush.linearGradient(listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.primary))

val defaultBlackBrush
    @Composable
    get() = Brush.linearGradient(listOf(Color(153, 0, 204), Color(153, 0, 204)))

val defaultRedBrush
    @Composable
    get() = Brush.linearGradient(listOf(Color(255, 51, 255), Color(255, 51, 255)))

val defaultEdgeColor
    @Composable
    get() = MaterialTheme.colorScheme.tertiary

const val defaultEdgeWidht = 2f

val defaultBackground
    @Composable
    get() = MaterialTheme.colorScheme.background

val defaultTextStyle
    @Composable
    get() = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )

val defaultLargeTextStyle
    @Composable
    get() = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.primary,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )

val defaultOnPrimaryLargeTextStyle
    @Composable
    get() = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )