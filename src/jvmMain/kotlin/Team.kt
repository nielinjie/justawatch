package xyz.nietongxue.soccerTime

class TeamRepository {
    private val teamsMap = mutableMapOf<Int, Team>()
    fun findById(id: Int) = teamsMap.get(id)
    fun update(teams: List<Team>) {
        teams.forEach {
            teamsMap[it.id] = it
        }
    }

    fun teams(leagueSeason: LeagueSeason, app: App): List<Int> {
        //TODO 需要优化不？
        return app.fixtureRepository.fixtures.filter { it.leagueSeason == leagueSeason }.map {
            listOf(it.teamAId, it.teamBId)
        }.flatten().distinct()
    }
}


class TeamCaller(val app: App, val leagueSeason: LeagueSeason) : ApiCaller<List<Team>>() {
    override val apiId: String = "teams  - $leagueSeason"
    override val scheduler: Scheduler = Scheduler.longTerm
    override val user = (object : ApiUser<List<Team>>(apiId) {
        override val url: String = "https://api-football-v1.p.rapidapi.com/v3/teams"
        override val params: Map<String, String> = leagueSeason.toParams()
        override fun withResponse(stringBody: String): List<Team> {
            val teams = fromTeamResponse(stringBody)
            app.teamRepository.update(teams)
            return teams
        }
    })
}