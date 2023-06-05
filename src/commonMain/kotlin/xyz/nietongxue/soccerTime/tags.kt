package xyz.nietongxue.soccerTime

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

enum class ObjectType {
    FIXTURE, TEAM
}

interface Tagger {
    val description: String
    val objectType: ObjectType
//    val type: String
}

enum class Power {
    HIGH, MEDIUM, LOW, NEGATIVE
}

@Serializable
data class Tag(
    val text: String,
    val id: String = text,
    val power: Power = Power.MEDIUM,
    val data: Map<String, String> = emptyMap()
) {
}

@Serializable
data class FixtureWithTags(
    val fixture: Fixture,
    val tags: List<Tag>,
    val aTeamTags: List<Tag>,
    val bTeamTags: List<Tag>
) {}


@Serializable
data class OfLeague(val leagueId: Int) : Tagger {
    override val description = "league: $leagueId"
    override val objectType = ObjectType.FIXTURE
}


@Serializable
data class PinedTeam(val name: String) : Tagger {
    override val description: String = "pined team - $name"
    override val objectType = ObjectType.TEAM
}

//比如欧冠区争夺
@Serializable
data class Rank(val leagueId: Int, val rank: Int) : Tagger {
    override val description: String = "position - $rank"
    override val objectType = ObjectType.TEAM
}

@Serializable
data class Night(val start: LocalTime, val end: LocalTime) {
    init {
        require(start <= LocalTime(23, 59, 59) || end > LocalTime(0, 0, 0)) {
            "start must be before middle night and end must be after middle night"
        }
    }

    fun timeIn(time: LocalTime): Boolean {
        if (time == LocalTime(0, 0, 0)) return true
        return (start <= time && time <= LocalTime(23, 59, 59)) ||
                (LocalTime(0, 0, 0) <= time && time <= end)
    }
}

@Serializable
data class AtNight(val night: Night) : Tagger {

    override val description = "too late - $night"
    override val objectType = ObjectType.FIXTURE
}

//比如榜首争霸赛
@Serializable
data class RankDiff( val rankDiff: Int) : Tagger {
    override val description: String = "rankDiff - $rankDiff"
    override val objectType = ObjectType.FIXTURE
}

@Serializable
data class PointsDiff( val pointDiff: Int = 3) : Tagger {
    override val description: String = "pointsDiff - $pointDiff"
    override val objectType = ObjectType.FIXTURE
}

@Serializable
data class PointsBelowFromRank(val leagueId: Int, val pointsDiff: Int, val rank: Int) : Tagger {
    override val objectType: ObjectType = ObjectType.TEAM
    override val description: String = "pointsDiffFromRank - $pointsDiff points from $rank"
}

//比如欧冠淘汰赛
//比如足总杯半决赛
@Serializable
data class LeaguePhase(val leagueId: Int) : Tagger {
    override val description: String
        get() = TODO("Not yet implemented")
    override val objectType = ObjectType.FIXTURE
}
