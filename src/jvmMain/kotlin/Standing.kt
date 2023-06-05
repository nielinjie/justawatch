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

class StandingCaller(val app: App) : ApiCaller() {
    override val apiId: String = "standings"
    override val scheduler: Scheduler = Scheduler.default
    override val users: List<ApiUser> = currents.map {
        (object : ApiUser(apiId) {
            override val url: String = "https://api-football-v1.p.rapidapi.com/v3/standings"
            override val params: Map<String, String> = it.toParams()
            override fun withResponse(stringBody: String) {
                val standings = fromStandingResponse(stringBody)
                app.standingRepository.update(standings)
            }
        })
    }
}