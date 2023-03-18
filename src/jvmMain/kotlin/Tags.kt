package xyz.nietongxue.soccerTime

import kotlinx.datetime.*
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs


class Tagging(private val session: Session, val app: App) {

    private val taggers = session.customize.taggers
    private val teamTaggers = taggers.filter { it.objectType == ObjectType.TEAM }
    private val fixtureTaggers = taggers.filter { it.objectType == ObjectType.FIXTURE }

    fun tagging(list: List<Fixture>): List<FixtureWithTags> {
        val teamsId = list.map { listOf(it.teamBId, it.teamAId) }.flatten().distinct()
        val teams = teamsId.mapNotNull { app.teamRepository.findById(it) }
        val teamWithTags: Map<Int, List<Tag>> = teams.associate { team ->
            team.id to teamTaggers.mapNotNull {
                tagForTeam(team, it)
            }
        }
        val fixtureWithTags: List<Pair<Fixture, List<Tag>>> = list.map { fixture ->
            fixture to fixtureTaggers.mapNotNull {
                tagForFixture(fixture, it, teamWithTags.toMap())
            }
        }

        return fixtureWithTags.map {
            val teamATags = teamWithTags[it.first.teamAId] ?: emptyList()
            val teamBTags = teamWithTags[it.first.teamBId] ?: emptyList()
            FixtureWithTags(it.first, it.second, teamATags, teamBTags)
        }
    }

    private fun normalizeRank(rank: Int): Int {
        assert(rank != 0)
        return if (rank >= 0) rank
        //TODO, team的数量需要league限制
        else app.teamRepository.teams.size + rank + 1
    }

    private fun getPointsOfRank(rank: Int): Int {
        val nr = normalizeRank(rank)
        val standing =
            app.standingRepository.standings.find { it.rank == nr } ?: error("not find standing for rank $nr")
        return standing.points
    }

    private fun tagForTeam(team: Team, tagger: Tagger): Tag? {
        assert(tagger.objectType == ObjectType.TEAM)
        return when (tagger) {
            is PinedTeam -> {
                val name = tagger.name
                if (team.name == name || team.code == name) Tag(
                    "${team.code} - pined team",
                    power = Power.HIGH,
                    data = mapOf("name" to team.name)
                ) else null
            }

            is Rank -> {
                val (league, rank) = tagger
                assert(league == onlySupportedLeague) { "not supported league" }
                val standing = app.standingRepository.findById(team.id) ?: error("no standing find")

                if (rank > 0) {
                    if (standing.rank <= rank) {
                        Tag("${team.code} - @${standing.rank}")
                    } else null
                } else if (rank < 0) {
                    val pPosition = normalizeRank(rank)
                    if (standing.rank >= pPosition) {
                        Tag("${team.code} - @${standing.rank}")
                    } else null
                } else error("rank can not be 0")
            }

            is PointsBelowFromRank -> {
                val (league, points, rank) = tagger
                assert(league == onlySupportedLeague) { "not supported league" }
                assert(rank != 0)
                val nr = normalizeRank(rank)
                val pointsOfRank = getPointsOfRank(rank)
                val thisPoints = (app.standingRepository.findById(team.id) ?: error("no standing find")).points
                if (thisPoints < pointsOfRank && thisPoints + points >= pointsOfRank) {
                    return Tag("${team.code} - p${pointsOfRank - thisPoints} below @${nr}")
                } else
                    return null

            }

            else -> error("not supported tagger")
        }
    }

    private fun tagForFixture(fixture: Fixture, tagger: Tagger, teamWithTags: Map<Int, List<Tag>>): Tag? {
        assert(tagger.objectType == ObjectType.FIXTURE)
        return when (tagger) {
            is RankDiff -> {
                val (league, rankDiff) = tagger
                assert(league == onlySupportedLeague) { "not supported league" }
                val aStanding = app.standingRepository.findById(fixture.teamAId)
                    ?: error("no standing find for team - $fixture.teamAId")
                val bStanding = app.standingRepository.findById(fixture.teamBId)
                    ?: error("no standing find for team - $fixture.teamBId")
                val diff = abs(aStanding.rank - bStanding.rank)
                if (diff <= abs(rankDiff)) {
                    Tag("rank diff - $diff")
                } else {
                    null
                }
            }

            is PointsDiff -> {
                val (league, pointsDiff) = tagger
                assert(league == onlySupportedLeague) { "not supported league" }
                val aStanding = app.standingRepository.findById(fixture.teamAId)
                    ?: error("no standing find for team - $fixture.teamAId")
                val bStanding = app.standingRepository.findById(fixture.teamBId)
                    ?: error("no standing find for team - $fixture.teamBId")
                val diff = abs(aStanding.points - bStanding.points)
                if (diff <= abs(pointsDiff)) {
                    Tag("points diff - $diff", power = Power.LOW)
                } else {
                    null
                }
            }

            is AtNight -> {
                val (night) = tagger
                val fixtureTimeIn =
                    Instant.fromEpochSeconds(fixture.date)
                val local = fixtureTimeIn.toLocalDateTime(TimeZone.currentSystemDefault())
                return if (night.timeIn(local.time))
                    Tag("bad time - ${local.time}", power = Power.NEGATIVE)
                else null
            }

            else -> error("not supported tagger")
        }
    }
}

