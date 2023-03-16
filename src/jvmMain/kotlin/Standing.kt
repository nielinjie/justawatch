package xyz.nietongxue.soccerTime

class StandingRepository {
    val standings = mutableListOf<Standing>()
    fun findById(id: Int) =  standings.find { it.teamId == id }
}

class StandingCaller(val app:App) : ApiCaller() {
    override val apiId: String = "standings"
    override val scheduler: Scheduler = Scheduler.default
    override val user: ApiUser = object : ApiUser(apiId) {
        override val url: String = "https://api-football-v1.p.rapidapi.com/v3/standings"
        override val params: Map<String, String> = mapOf(
            "league" to "39", "season" to "2022"
        )

        override fun withResponse(stringBody: String) {
            val standings = fromStandingResponse(stringBody)
            app.standingRepository.standings.apply {
                clear()
                addAll(standings)
            }
        }
    }

}