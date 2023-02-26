package xyz.nietongxue.soccerTime

import kotlinx.serialization.json.*

fun fromTeamResponse(response: String): List<Team> {
    val je = Json.parseToJsonElement(response).jsonObject
    val teams = je["response"]!!.jsonArray.toList()
    return teams.map {
        val jt = it.jsonObject["team"]!!.jsonObject
        val name = jt["name"]!!.jsonPrimitive.content
        val code = jt["code"]!!.jsonPrimitive.content
        val logo = jt["logo"]!!.jsonPrimitive.content
        val id = jt["id"]!!.jsonPrimitive.int
        Team(id, name, code, logo)
    }
}