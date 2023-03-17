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
data class PinedTeam(val name: String) : Tagger {
    override val description: String = "pined team - $name"
    override val objectType = ObjectType.TEAM
}

//比如欧冠区争夺
@Serializable
data class Rank(val league: String, val rank: Int) : Tagger {
    override val description: String ="position - $rank"
    override val objectType = ObjectType.TEAM
}


//比如榜首争霸赛
@Serializable
data class RankDiff(val league: String, val rankDiff: Int) : Tagger {
    override val description: String = "rankDiff - $rankDiff"
    override val objectType = ObjectType.FIXTURE
}
@Serializable
data class PointsDiff(val league: String, val pointDiff: Int=3) : Tagger {
    override val description: String = "pointsDiff - $pointDiff"
    override val objectType = ObjectType.FIXTURE
}

@Serializable
data class PointsBelowFromRank(val league: String, val pointsDiff: Int, val rank:Int) : Tagger {
    override val objectType: ObjectType = ObjectType.TEAM
    override val description: String = "pointsDiffFromRank - $pointsDiff points from $rank"
}

//比如欧冠淘汰赛
//比如足总杯半决赛
@Serializable
data class LeaguePhase(val league: String) : Tagger {
    override val description: String
        get() = TODO("Not yet implemented")
    override val objectType = ObjectType.FIXTURE
}
