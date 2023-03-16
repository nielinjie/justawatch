package xyz.nietongxue.soccerTime

import kotlinx.serialization.Serializable

enum class ObjectType {
    FIXTURE, TEAM
}

interface Tagger {
    val description: String
    val objectType: ObjectType
//    val type: String
}


@Serializable
data class Tag(val text: String, val id: String, val data: Map<String, String>) {
    constructor(id: String) : this(id, id, emptyMap())
    constructor(id: String, data: Map<String, String>) : this(id, id, data)
}

@Serializable
data class FixtureWithTags(
    val fixture: Fixture,
    val tags: List<Tag>,
    val aTeamTags: List<Tag>,
    val bTeamTags: List<Tag>
) {}


@Serializable
data class League(val league: String) : Tagger {
    override val description = "league: $league"
    override val objectType = ObjectType.FIXTURE
}


@Serializable
data class NamedTeam(val names: List<String>) : Tagger {
    override val description: String = "for teams - $names"
    override val objectType = ObjectType.TEAM
}

//比如欧冠区争夺
@Serializable
data class PositionInStanding(val league: String, val position: Int) : Tagger {
    override val description: String
        get() = TODO("Not yet implemented")
    override val objectType = ObjectType.TEAM
}


//比如榜首争霸赛
@Serializable
data class PositionDiff(val league: String, val positionDiff: Int) : Tagger {
    override val description: String
        get() = TODO("Not yet implemented")
    override val objectType = ObjectType.FIXTURE
}


//比如欧冠淘汰赛
//比如足总杯半决赛
@Serializable
data class LeaguePhase(val league: String) : Tagger {
    override val description: String
        get() = TODO("Not yet implemented")
    override val objectType = ObjectType.FIXTURE
}
