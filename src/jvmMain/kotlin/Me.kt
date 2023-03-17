package xyz.nietongxue.soccerTime

import org.kodein.di.*

val diMe = DI {
    bindSingleton<App> { defaultAppMe }
    bindSet<ApiCaller> {
        add { singleton { FixtureCaller(instance()) } }
        add { singleton { StandingCaller(instance()) } }
        add { singleton { TeamCaller(instance()) } }
    }
}

val defaultAppMe = object : App() {
    override val fixtureRepository: FixtureRepository =
        FixtureRepository()

    override val standingRepository: StandingRepository = StandingRepository()
    override val sessionRepository: SessionRepository = SessionRepository().apply {
        put(
            Session(
                "_me", Customize(
                    emptyMap(), listOf(
                        PinedTeam("MUN"), PinedTeam("TOT"),
                        Rank(onlySupportedLeague, 4),
                        Rank(onlySupportedLeague, -3),
                        //RankDiff(onlySupportedLeague,1), //  这个在这里没必要，因为下一个。
                        PointsDiff(onlySupportedLeague),
                        PointsBelowFromRank(onlySupportedLeague, 3, 4), //离欧冠区3分
                        PointsBelowFromRank(onlySupportedLeague, 3, -4), //离摆脱降级区3分
                    )
                )
            )
        )
    }
    override val teamRepository: TeamRepository = TeamRepository()
}