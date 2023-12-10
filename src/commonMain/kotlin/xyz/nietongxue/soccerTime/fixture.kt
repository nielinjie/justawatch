package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable

@Serializable
data class Fixture(
    val id: Int,
    val leagueSeason: LeagueSeason,
    val round:String,
    //EpochSeconds, not ms!!
    val date: Long,
    val teamAId: Int,
    val teamBId: Int,
    val status: String,
    val result: String?
)

@Serializable
data class Team(val id: Int, val name: String, val code: String, val logo: String)


@Serializable
data class Form(val formChars: List<String>) {
    companion object {
        fun fromString(value: String): Form {
            return Form(value.toCharArray().map { it.toString() })
        }
    }
}


