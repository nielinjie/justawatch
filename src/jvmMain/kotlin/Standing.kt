package xyz.nietongxue.soccerTime


class StandingRepository {
    val standingsMap: MutableMap<StandingKey, Standing> = mutableMapOf()
    fun find(teamId: Int, leagueSeason: LeagueSeason): Standing? {
        return standingsMap[teamId to leagueSeason]
    }

    fun forLeagueSeason(leagueSeason: LeagueSeason): List<Standing> {
        return standingsMap.filter { it.key.second == leagueSeason }.map { it.value }
    }

    fun update(standings: List<Standing>) {
        standings.forEach {
            standingsMap[it.teamId to it.leagueSeason] = it
        }
    }


}

class StandingCaller(val app: App, val leagueSeason: LeagueSeason) : ApiCaller<List<Standing>>() {
    override val apiId: String = "standings - $leagueSeason"
    override val scheduler: Scheduler = Scheduler.default
    override val user =
        (object : ApiUser<List<Standing>>(apiId) {
            override val url: String = "https://api-football-v1.p.rapidapi.com/v3/standings"
            override val params: Map<String, String> = leagueSeason.toParams()
            override fun withResponse(stringBody: String):List<Standing> {
                val standings = fromStandingResponse(stringBody)
                app.standingRepository.update(standings)
                return standings
            }
        })
}