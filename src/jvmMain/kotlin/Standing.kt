package xyz.nietongxue.soccerTime

import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object StandingRepository {
    val standings = mutableListOf<Standing>()
    fun findById(id: Int?) = id?.let { idIt -> standings.find { it.teamId == idIt } } ?: standings
}

object StandingCaller : ApiCaller() {
    override val apiId: String = "standings"
    override val scheduler: Scheduler = Scheduler.default
    override val user: ApiUser = object : ApiUser(apiId) {
        override val url: String = "https://api-football-v1.p.rapidapi.com/v3/standings"
        override val params: Map<String, String> = mapOf(
            "league" to "39", "season" to "2022"
        )

        override fun withResponse(stringBody: String) {
            val standings = fromStandingResponse(stringBody)
            StandingRepository.standings.apply {
                clear()
                addAll(standings)
            }
        }
    }

}