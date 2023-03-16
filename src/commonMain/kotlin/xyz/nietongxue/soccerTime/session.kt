package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable

enum class UnderLine {
    NEXT, WATCHED, HOT, APPEARED,TAG
}
@Serializable
data class Session(val id:String,val customize: Customize){

}
@Serializable
data class Customize(val filters: Map<UnderLine, List<Filter>>,
val taggers:List<Tagger>) {

}