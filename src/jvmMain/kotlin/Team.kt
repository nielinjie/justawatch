package xyz.nietongxue.soccerTime

object TeamRepository {
    val teams = mutableListOf<Team>()
    fun findById(id: Int) = teams.find { it.id == id }
}



object TeamCaller : ApiCaller() {
    override val apiId: String = "teams"
    override val scheduler: Scheduler = Scheduler.longTerm
    override val user: ApiUser = object : ApiUser(apiId) {
        override val url: String = "https://api-football-v1.p.rapidapi.com/v3/teams"
        override val params: Map<String, String> = mapOf(
            "league" to "39",
            "season" to "2022"
        )

        override fun withResponse(stringBody: String) {
            val teams = fromTeamResponse(stringBody)
            TeamRepository.teams.apply {
                clear()
                addAll(teams)
            }
        }
    }

}