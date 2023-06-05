package xyz.nietongxue.soccerTime

import kotlinx.serialization.json.*

import kotlinx.datetime.Instant
import kotlin.math.max
import kotlin.time.Duration.Companion.days
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

fun findNextFixture(fixtures: List<Fixture>): Instant? {
    val now = now()
    return fixtures.filter { it.date > now.epochSeconds }.minByOrNull { it.date }?.date?.let {
        Instant.fromEpochSeconds(it)
    }
}

val footballMatchTime = 1.8.hours
val refreshWhileMatching = 10.minutes
fun findCurrentFixture(fixtures: List<Fixture>): List<Fixture> {
    val now = now()
    return fixtures.filter {
        Instant.fromEpochSeconds(it.date) < now && now < Instant.fromEpochSeconds(it.date).plus(footballMatchTime)
    }
}

val fixtureScheduler: Scheduler = object : Scheduler {
    override fun next(preResult: TaskResult): Instant {
        return when (preResult) {
            is TaskResult.SUCCESS<*> -> {
                val fixtures = preResult.result as? List<Fixture> ?: error("no supported result")
                val nextFixture = findNextFixture(fixtures)
                when {
                    findCurrentFixture(fixtures).isNotEmpty() -> now().plus(refreshWhileMatching)
                    nextFixture != null -> listOf(
                        nextFixture.minus(refreshWhileMatching),
                        now().plus(refreshWhileMatching)
                    ).max()
                    else -> now().plus(1.days)
                }
            }

            TaskResult.INIT -> now().plus(3.seconds)
            is TaskResult.ERROR -> now().plus(1.minutes)
            else -> error("not supported")
        }
    }
}

class FixtureCaller(val app: App, val leagueSeason: LeagueSeason) : ApiCaller<List<Fixture>>() {
    override val apiId: String = "fixtures - $leagueSeason"
    override val scheduler: Scheduler = fixtureScheduler
    override val user: ApiUser<List<Fixture>> =
        object : ApiUser<List<Fixture>>(apiId) {
            override val url: String = "https://api-football-v1.p.rapidapi.com/v3/fixtures"
            override val params: Map<String, String> = leagueSeason.toParams()
            override fun withResponse(stringBody: String): List<Fixture> {
                val fixtures = fromApiResponse(stringBody)
                app.fixtureRepository.update(fixtures ?: emptyList())
                return fixtures ?: emptyList()
            }
        }

}