package xyz.nietongxue.soccerTime

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.util.Date

fun JsonObject.getByPath(path: String): JsonElement? {
    val parts = path.split(".")
    return getByPath(parts)
}

fun JsonObject.getByPath(parts: List<String>): JsonElement? {
    var re: JsonElement? = this
    for (part in parts) {
        re = re?.jsonObject?.get(part)
    }
    return re
}

fun Instant.toDate():Date {
    return Date.from(this.toJavaInstant())
}


fun now(): Instant = Clock.System.now()
