package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable
interface Tagger {
    val description: String
}
@Serializable
data class Tag(val name: String, val id: String) {
    constructor(id: String) : this(id, id)
}
@Serializable
data class FixtureWithTags(val fixture: Fixture, val tags: List<Tag>)



@Serializable
data class League(val league: String) : Tagger{
    override val description="league: $league"
}


@Serializable
data class NamedTeam(val names: List<String>) : Tagger {
    override val description: String
         = "for teams - $names"

}

//比如欧冠区争夺
@Serializable
data class PositionInStanding(val league: String, val position: Int) : Tagger{
    override val description: String
        get() = TODO("Not yet implemented")
}


//比如榜首争霸赛
@Serializable
data class PositionDiff(val league: String, val positionDiff: Int) : Tagger{
    override val description: String
        get() = TODO("Not yet implemented")
}


//比如欧冠淘汰赛
//比如足总杯半决赛
@Serializable
data class LeaguePhase(val league: String) : Tagger{
    override val description: String
        get() = TODO("Not yet implemented")
}
