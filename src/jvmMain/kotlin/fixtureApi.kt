package xyz.nietongxue.soccerTime

import kotlinx.serialization.json.*

fun oneFixture(je: JsonElement): Fixture? {
    try {
        val id = je.jsonObject.getByPath("fixture.id")?.jsonPrimitive?.int
        val timestamp = je.jsonObject.getByPath("fixture.timestamp")?.jsonPrimitive?.long
        val teamA =
            je.jsonObject.getByPath("teams.home.name")?.jsonPrimitive?.content
        val teamB =
            je.jsonObject.getByPath("teams.away.name")?.jsonPrimitive?.content
        val status =
            je.jsonObject.getByPath("fixture.status.short")?.jsonPrimitive?.content

        val result: String? =
            if (je.jsonObject.getByPath("goals.home")?.jsonPrimitive?.intOrNull == null) null else je.jsonObject.getByPath(
                "goals.home"
            )?.let {
                "${it.jsonPrimitive.int} - ${je.jsonObject.getByPath("goals.away")?.jsonPrimitive?.int}"
            }
        if (id != null && timestamp != null && teamA != null && teamB != null && status != null) {
            return Fixture(id, timestamp, teamA, teamB, status, result)
        }
        return null
    } catch (e: Exception) {
        error("can not parse json to fixture")
    }
}

fun fromApiResponse(responseString: String): List<Fixture>? {
    val element = Json.parseToJsonElement(responseString)
    val re = element.jsonObject.get("response")?.jsonArray
    val fs = re?.map {
        oneFixture(it)
    }
    return fs?.filterNotNull()
}

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