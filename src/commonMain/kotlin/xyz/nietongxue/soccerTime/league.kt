package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable


@Serializable
data class League(val id: Int, val name: String, val logo: String)

@Serializable
data class Season(val id: Int)


typealias LeagueId = Int
typealias SeasonId = Int
typealias LeagueSeason = Pair<LeagueId, SeasonId>

val leagueIds = listOf(
    2, // 欧冠
//    3, // 欧联杯
//    39, // 英超
    45, // 英足总杯
)
val seasons = listOf(
    2022,
//    2023,
)

val currents: List<LeagueSeason> = leagueIds.flatMap { leagueId ->
    seasons.map { seasonId ->
        leagueId to seasonId
    }
}
val currentPE: LeagueSeason = 39 to 2023
val currentCL: LeagueSeason = 2 to 2022

val LeagueSeason.leagueId: Int
    get() = first

fun LeagueSeason.toParams(): Map<String, String> {
    return mapOf(
        "league" to first.toString(),
        "season" to second.toString()
    )
}

fun LeagueSeason.isCup(): Boolean {
    return first in listOf(45, 2, 3)
}

val leagueLogos:Map<Int,String> = leagueIds.associateWith { "https://media.api-sports.io/football/leagues/$it.png" }

//NOTE https://rapidapi.com/api-sports/api/api-football/
//NOTE api: v3/fixtures/rounds
val roundsOf45 = listOf(
    "Extra Preliminary Round",
    "Extra Preliminary Round Replays",
    "Preliminary Round",
    "Preliminary Round Replays",
    "1st Round Qualifying",
    "1st Round Qualifying Replays",
    "2nd Round Qualifying",
    "2nd Round Qualifying Replays",
    "3rd Round Qualifying",
    "3rd Round Qualifying Replays",
    "4th Round Qualifying",
    "4th Round Qualifying Replays",
    "1st Round",
    "1st Round Replays",
    "2nd Round",
    "2nd Round Replays",
    "3rd Round",
    "3rd Round Replays",
    "4th Round",
    "4th Round Replays",
    "5th Round",
    "Quarter-finals",
    "Semi-finals",
    "Final"
)

fun roundsFilter(season: LeagueSeason): RoundsFilter {
    return RoundsFilter(
        //drop until 3rd round
        when (season.leagueId) {
            45 -> roundsOf45.dropWhile { it != "3rd Round" }
            2 -> roundsOf2.dropWhile { it != "Play-offs" }
            3 -> roundsOf3.dropWhile { it != "Play-offs" }
            39 -> roundsOf39
            else -> error("not supported")
        },
        season
    )
}


val roundsOf2 = listOf(
    "Preliminary Round",
    "1st Qualifying Round",
    "2nd Qualifying Round",
    "3rd Qualifying Round",
    "Play-offs",
    "Group E - 1",
    "Group G - 1",
    "Group F - 1",
    "Group H - 1",
    "Group A - 1",
    "Group D - 1",
    "Group B - 1",
    "Group C - 1",
    "Group C - 2",
    "Group D - 2",
    "Group A - 2",
    "Group B - 2",
    "Group E - 2",
    "Group F - 2",
    "Group G - 2",
    "Group H - 2",
    "Group C - 3",
    "Group D - 3",
    "Group A - 3",
    "Group B - 3",
    "Group E - 3",
    "Group F - 3",
    "Group G - 3",
    "Group H - 3",
    "Group G - 4",
    "Group H - 4",
    "Group E - 4",
    "Group F - 4",
    "Group A - 4",
    "Group B - 4",
    "Group C - 4",
    "Group D - 4",
    "Group E - 5",
    "Group G - 5",
    "Group F - 5",
    "Group H - 5",
    "Group B - 5",
    "Group C - 5",
    "Group A - 5",
    "Group D - 5",
    "Group B - 6",
    "Group A - 6",
    "Group C - 6",
    "Group D - 6",
    "Group F - 6",
    "Group E - 6",
    "Group G - 6",
    "Group H - 6",
    "Round of 16",
    "Quarter-finals",
    "Semi-finals",
    "Final"
)
val roundsOf3 = listOf(
    "3rd Qualifying Round",
    "Play-offs",
    "Group Stage - 1",
    "Group Stage - 2",
    "Group Stage - 3",
    "Group Stage - 4",
    "Group Stage - 5",
    "Group Stage - 6",
    "Knockout Round Play-offs",
    "Round of 16",
    "Quarter-finals",
    "Semi-finals",
    "Final"
)
val roundsOf39 = listOf(
    "Regular Season - 1",
    "Regular Season - 2",
    "Regular Season - 3",
    "Regular Season - 4",
    "Regular Season - 5",
    "Regular Season - 6",
    "Regular Season - 8",
    "Regular Season - 9",
    "Regular Season - 10",
    "Regular Season - 11",
    "Regular Season - 12",
    "Regular Season - 13",
    "Regular Season - 14",
    "Regular Season - 15",
    "Regular Season - 16",
    "Regular Season - 17",
    "Regular Season - 18",
    "Regular Season - 19",
    "Regular Season - 7",
    "Regular Season - 20",
    "Regular Season - 21",
    "Regular Season - 22",
    "Regular Season - 23",
    "Regular Season - 24",
    "Regular Season - 25",
    "Regular Season - 26",
    "Regular Season - 27",
    "Regular Season - 28",
    "Regular Season - 29",
    "Regular Season - 30",
    "Regular Season - 31",
    "Regular Season - 32",
    "Regular Season - 33",
    "Regular Season - 34",
    "Regular Season - 35",
    "Regular Season - 36",
    "Regular Season - 37",
    "Regular Season - 38"
)

