package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable


@Serializable
data class FixtureDetailed(
    val fixture: Fixture,
    val standings: Pair<Standing?, Standing?>,
    val teams: Pair<Team, Team>,
    val tags: List<Tag>,
    val underLines: Set<UnderLine> = emptySet(),
)

fun List<FixtureDetailed>.defaultSort():List<FixtureDetailed>{
    return this.sortedWith(
        compareBy(
            { it.fixture.date },
            {-(it.tags.sccore())}
        )
    )
}