package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable


const val onlySupportedLeague = "EP"


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

val LeagueSeason.leagueId: Int
    get() = first

fun LeagueSeason.toParams(): Map<String, String> {
    return mapOf(
        "league" to first.toString(),
        "season" to second.toString()
    )
}

fun LeagueSeason.isCup(): Boolean {
    return first in listOf(45,2,3)
}