package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable

@Serializable
data class Fixture(val id: Int, val date: Long, val teamA: String, val teamB: String, val status: String,val result:String?) {
}



