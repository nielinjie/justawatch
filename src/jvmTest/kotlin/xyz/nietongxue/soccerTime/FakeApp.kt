import xyz.nietongxue.soccerTime.*

val fakeApp = object : App() {
    override val fixtureRepository: FixtureRepository by lazy {
        val jsonString = javaClass.getResource("/fixturesApiResponse.json")!!.readText()
        val fs = fromApiResponse(jsonString)
        FixtureRepository().apply {
            update(fs ?: emptyList())
        }
    }
    override val standingRepository: StandingRepository by lazy {
        val standingJson = javaClass.getResource("/standingApiResponse.json")!!.readText()
        val fs = fromStandingResponse(standingJson)
        StandingRepository().apply {
            this.update(fs)
        }
    }
    override val sessionRepository: SessionRepository by lazy {
        SessionRepository()
    }
    override val teamRepository: TeamRepository by lazy {
        val teamJson = javaClass.getResource("/teamInfoApiResponse.json")!!.readText()
        val fs = fromTeamResponse(teamJson)
        TeamRepository().apply {
            this.update(fs)
        }
    }
    override val userRepository: UserRepository
        get() = TODO("Not yet implemented")

}

val fakeSession = Session(
    "_fake", Customize(

        filters = emptyMap(),
        taggers = listOf(
            PinedTeam("MUN")
        )
    )
)