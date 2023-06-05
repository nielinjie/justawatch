package xyz.nietongxue.soccerTime

class TeamRepository {
    val teamsMap = mutableMapOf<Int,Team>()
    fun findById(id: Int) = teamsMap.get(id)
    fun update(teams:List<Team>){
        teams.forEach {
            teamsMap[it.id] = it
        }
    }
    fun teams(leagueSeason: LeagueSeason,app: App):List<Int>{
        //TODO 需要优化不？
        return app.fixtureRepository.fixtures.filter{it.leagueSeason == leagueSeason}.map {
            listOf(it.teamAId,it.teamBId)
        }.flatten().distinct()
    }
}


class TeamCaller(val app: App) : ApiCaller() {
    override val apiId: String = "teams"
    override val scheduler: Scheduler = Scheduler.longTerm
    override val users: List<ApiUser> = currents.map{ (object : ApiUser(apiId) {
        override val url: String = "https://api-football-v1.p.rapidapi.com/v3/teams"
        override val params: Map<String, String> = it.toParams()
        override fun withResponse(stringBody: String) {
            val teams = fromTeamResponse(stringBody)
            app.teamRepository.update(teams)
        }
    })}
}