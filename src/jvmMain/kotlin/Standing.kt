package xyz.nietongxue.soccerTime


class StandingRepository {
    private val standingsMap: MutableMap<StandingKey, Standing> = mutableMapOf()
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
class StandingResult(val leagueSeason: LeagueSeason, val standings: List<Standing>)
class StandingCaller(val app: App, val leagueSeason: LeagueSeason) : ApiCaller<StandingResult>() {
    override val apiId: String = "standings - $leagueSeason"
    override val scheduler: Scheduler = FixtureScheduler(app) //TODO 可能有问题，如果此时fixture call还没完成，就可能造成standing 的预排过晚。
    override val user =
        (object : ApiUser<StandingResult>(apiId) {
            override val url: String = "https://api-football-v1.p.rapidapi.com/v3/standings"
            override val params: Map<String, String> = leagueSeason.toParams()
            override fun withResponse(stringBody: String):StandingResult {
                val standings = fromStandingResponse(stringBody)
                app.standingRepository.update(standings)
                return StandingResult(leagueSeason,standings)
            }
        })
}