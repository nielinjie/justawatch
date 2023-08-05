package xyz.nietongxue.soccerTime

import kotlinx.datetime.LocalTime
import org.kodein.di.*

val diMe = DI {
    bindSingleton<App> { defaultAppMe }
    bindSingleton<List<ApiCaller<*>>> {
        (currents.map {
            TeamCaller(instance(), it)
        } + currents.map {
            StandingCaller(instance(), it)
        } + currents.map {
            FixtureCaller(instance(), it)
        })
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
                    mapOf(
                        UnderLine.COLLAPSED to listOf(
                            NegativeTagsFilter, NoTagFilter
                        ),
                        //NOTE 不在这里面的将标为hidden。
                        //UnderLine.HIDDEN to listOf(LeagueFilter(listOf(currentPE, currentCL)))
                    ),

                    listOf(
                        PinedTeam("MUN"), PinedTeam("TOT"),
                        Rank(currentPE.leagueId, 4),
                        Rank(currentPE.leagueId, -3),
                        //RankDiff(onlySupportedLeague,1), //  这个在这里没必要，因为下一个。
                        PointsDiff(currentPE.leagueId),
                        PointsBelowFromRank(currentPE.leagueId, 3, 4), //离欧冠区3分
                        PointsBelowFromRank(currentPE.leagueId, 3, -4), //离摆脱降级区3分
                        AtNight(Night(LocalTime(23, 0, 0), LocalTime(18, 0, 0)))
                    )
                )
            )
        )
    }
    override val teamRepository: TeamRepository = TeamRepository()
}