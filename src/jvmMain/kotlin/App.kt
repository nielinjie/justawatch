package xyz.nietongxue.soccerTime

import io.klogging.config.DEFAULT_CONSOLE
import io.klogging.config.loggingConfiguration

abstract class App {
    init {
        loggingConfiguration { DEFAULT_CONSOLE() }
    }

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

