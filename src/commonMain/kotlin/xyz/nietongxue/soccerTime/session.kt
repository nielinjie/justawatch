package xyz.nietongxue.soccerTime

enum class UnderLine {
    NEXT, WATCHED, HOT, APPEARED,TAG
}



data class Session(val filters: Map<UnderLine, List<Filter>>,
val taggers:List<Tagger>) {

}