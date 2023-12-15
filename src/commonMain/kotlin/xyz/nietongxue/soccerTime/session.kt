package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable

enum class UnderLine {
    NEXT, WATCHED, HOT, APPEARED, TAG, COLLAPSED,HIDDEN
}

@Serializable
data class Session(val id: String, val customize: Customize) {

}

@Serializable
data class Customize(
    val filters: Map<UnderLine, List<Filter>>, //TODO listFilter 指的是是否要显示，underline指的是显示的形式，这两个是否要整合？
    val taggers: List<Tagger>
) {

}



@Serializable
data class UserSession(val provider: String, val token: String)
@Serializable
data class UserInfo(val provider: String, val id: String, val name: String)