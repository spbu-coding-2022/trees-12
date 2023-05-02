package app.controller

import androidx.compose.ui.unit.dp
import app.view.Position
import app.view.ScrollDelta
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun serializePosition(pos: Position): String {
    return Json.encodeToString(Pair(pos.x.value, pos.x.value))
}

fun deserializePosition(image: String): Position {
    val floatImage = Json.decodeFromString<Pair<Float, Float>>(image)
    return Position(floatImage.first.dp, floatImage.first.dp)
}

fun serializeScrollDelta(scrollDelta: ScrollDelta): String {
    return Json.encodeToString(Pair(scrollDelta.x.value, scrollDelta.x.value))
}

fun deserializeScrollDelta(image: String): ScrollDelta {
    val floatImage = Json.decodeFromString<Pair<Float, Float>>(image)
    return ScrollDelta(floatImage.first.dp, floatImage.first.dp)
}