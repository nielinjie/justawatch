package xyz.nietongxue.soccerTime

import kotlinx.serialization.json.*

fun fromStandingResponse(response: String): List<Standing> {
    val je = Json.parseToJsonElement(response).jsonObject
    val sts = je["response"]!!.jsonArray[0]
        .jsonObject
        .getByPath("league.standings")!!.jsonArray[0].jsonArray
    //TODO 可能一个赛事有多个standing？杯赛？
    return sts.map {
        val js = it.jsonObject
        val rank = js["rank"]!!.jsonPrimitive.int
        val teamId = js.getByPath("team.id")!!.jsonPrimitive.int
        val score = js["points"]!!.jsonPrimitive.int
        val form = js["form"]!!.jsonPrimitive.content
        Standing(teamId, rank, score, Form.fromString(form))
    }
}


