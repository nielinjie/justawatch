package xyz.nietongxue.soccerTime

import kotlinx.serialization.json.*

fun fromStandingResponse(response: String): List<Standing> {
    val je = Json.parseToJsonElement(response).jsonObject
    if( je["response"]!!.jsonArray.isEmpty()) return emptyList()
    val sts = je["response"]!!.jsonArray[0]
        .jsonObject
        .getByPath("league.standings")!!.jsonArray[0].jsonArray
    //TODO 可能一个赛事有多个standing？杯赛？
    val leagueId = je["response"]!!.jsonArray[0]
        .jsonObject
        .getByPath("league.id")!!.jsonPrimitive.int
    val season = je["response"]!!.jsonArray[0]
        .jsonObject
        .getByPath("league.season")!!.jsonPrimitive.int
    return sts.map {
        val js = it.jsonObject
        val rank = js["rank"]!!.jsonPrimitive.int
        val teamId = js.getByPath("team.id")!!.jsonPrimitive.int
        val score = js["points"]!!.jsonPrimitive.int
        val form = js["form"]!!.jsonPrimitive.content
        val group = js["group"]?.jsonPrimitive?.content
        Standing(teamId, leagueId to season, rank, score, Form.fromString(form),group)
    }
}


