package xyz.nietongxue.soccerTime

import kotlinx.serialization.json.*

import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class FixtureRepository {

    private val fixturesMap: MutableMap<Int, Fixture> = mutableMapOf()
    val fixtures: List<Fixture>
        get() {
            return fixturesMap.values.toList().sortedBy { it.date }
        }




    fun update(fixtures: List<Fixture>) {
        fixtures.forEach {
            fixturesMap[it.id] = it
        }
    }
}


fun oneFixture(je: JsonElement): Fixture? {
    try {
        val id = je.jsonObject.getByPath("fixture.id")?.jsonPrimitive?.int
        val timestamp = je.jsonObject.getByPath("fixture.timestamp")?.jsonPrimitive?.long
        val teamA =
            je.jsonObject.getByPath("teams.home.id")?.jsonPrimitive?.int
        val teamB =
            je.jsonObject.getByPath("teams.away.id")?.jsonPrimitive?.int
        val status =
            je.jsonObject.getByPath("fixture.status.short")?.jsonPrimitive?.content
        val leagueId =
            je.jsonObject.getByPath("league.id")!!.jsonPrimitive.int
        val season = je.jsonObject.getByPath("league.season")!!.jsonPrimitive.int
        val result: String? =
            if (je.jsonObject.getByPath("goals.home")?.jsonPrimitive?.intOrNull == null) null else je.jsonObject.getByPath(
                "goals.home"
            )?.let {
                "${it.jsonPrimitive.int} - ${je.jsonObject.getByPath("goals.away")?.jsonPrimitive?.int}"
            }
        if (id != null && timestamp != null && teamA != null && teamB != null && status != null) {
            return Fixture(id, leagueId to season, timestamp, teamA, teamB, status, result)
        }
        return null
    } catch (e: Exception) {
        error("can not parse json to fixture")
    }
}

fun fromApiResponse(responseString: String): List<Fixture>? {
    val element = Json.parseToJsonElement(responseString)
    val re = element.jsonObject["response"]?.jsonArray
    val fs = re?.map {
        oneFixture(it)
    }
    return fs?.filterNotNull()
}




class FixtureCaller(val app: App) : ApiCaller() {
    override val apiId: String = "fixtures"
    override val scheduler: Scheduler = Scheduler.default
    override val users: List<ApiUser> = currents.map {
        (object : ApiUser("fixtures") {
            override val url: String = "https://api-football-v1.p.rapidapi.com/v3/fixtures"
            override val params: Map<String, String> = it.toParams()
            override fun withResponse(stringBody: String) {
                val fixtures = fromApiResponse(stringBody)
                app.fixtureRepository.update(fixtures ?: emptyList())
            }
        })
    }
}