package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable

typealias StandingKey  = Pair<Int, LeagueSeason>



@Serializable
data class Standing(val teamId: Int, val leagueSeason: LeagueSeason, val rank: Int, val points: Int, val form: Form,val group:String?=null)