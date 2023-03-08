package xyz.nietongxue.soccerTime

abstract class App {
    abstract val fixtureRepository: FixtureRepository
    abstract val standingRepository: StandingRepository
    abstract val sessionRepository: SessionRepository
    abstract val teamRepository: TeamRepository
}

val defaultApp = object : App() {
    override val fixtureRepository: FixtureRepository =
        FixtureRepository()

    override val standingRepository: StandingRepository = StandingRepository()
    override val sessionRepository: SessionRepository = SessionRepository()
    override val teamRepository: TeamRepository = TeamRepository()
}