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
        val teams: Map<Int, Team> = teamsId.associateWith { app.teamRepository.findById(it)!! }

        val fixtureWithTags: List<Pair<Fixture, List<Tag>>> = list.map { fixture ->
            fixture to fixtureTaggers.mapNotNull {
                tagForFixture(fixture, it)
            }
        }

        return fixtureWithTags.map {
            val fixture = it.first
            val teamATags = tagForTeam(teams[fixture.teamAId]!!, teamTaggers, fixture)
            val teamBTags = tagForTeam(teams[fixture.teamBId]!!, teamTaggers, fixture)
            FixtureWithTags(it.first, it.second, teamATags, teamBTags)
        }
    }

    private fun normalizeRank(rank: Int, leagueSeason: LeagueSeason): Int {
        assert(rank != 0)
        return if (rank >= 0) rank
        //TODO, team按照standing的个数来确定，可能会有问题，杯赛的standing不知道是什么格式。
        else app.standingRepository.forLeagueSeason(leagueSeason).size + rank + 1
    }

    private fun getPointsOfRank(rank: Int, leagueSeason: LeagueSeason): Int {
        val nr = normalizeRank(rank, leagueSeason)
        val standing =
            app.standingRepository.forLeagueSeason(leagueSeason).find { it.rank == nr }
                ?: error("not find standing for rank $nr")
        return standing.points
    }

    private fun tagForTeam(team: Team, taggers: List<Tagger>, fixture: Fixture): List<Tag> {
        val leagueSeason = fixture.leagueSeason
        return taggers.map { tagger ->
            assert(tagger.objectType == ObjectType.TEAM)
            when (tagger) {
                is PinedTeam -> {
                    val name = tagger.name
                    if (team.name == name || team.code == name) Tag(
                        "${team.code} - pined team",
                        power = Power.HIGH,
                        data = mapOf("name" to team.name)
                    ) else null
                }

                is Rank -> {
                    if (leagueSeason.isCup()) return@map null
                    val (league, rank) = tagger
                    val standing =
                        app.standingRepository.find(team.id, fixture.leagueSeason) ?: error("no standing find")

                    if (rank > 0) {
                        if (standing.rank <= rank) {
                            Tag("${team.code} - @${standing.rank}")
                        } else null
                    } else if (rank < 0) {
                        val pPosition = normalizeRank(rank, fixture.leagueSeason)
                        if (standing.rank >= pPosition) {
                            Tag("${team.code} - @${standing.rank}")
                        } else null
                    } else error("rank can not be 0")
                }

                is PointsBelowFromRank -> {
                    if (leagueSeason.isCup()) return@map null
                    val (league, points, rank) = tagger
                    assert(rank != 0)
                    val nr = normalizeRank(rank, fixture.leagueSeason)
                    val pointsOfRank = getPointsOfRank(rank, fixture.leagueSeason)
                    val thisPoints =
                        (app.standingRepository.find(team.id, fixture.leagueSeason) ?: error("no standing find")).points
                    if (thisPoints < pointsOfRank && thisPoints + points >= pointsOfRank) {
                        Tag("${team.code} - p${pointsOfRank - thisPoints} below @${nr}")
                    } else
                        null

                }

                else -> error("not supported tagger")
            }
        }.filterNotNull()
    }

    private fun tagForFixture(fixture: Fixture, tagger: Tagger): Tag? {
        assert(tagger.objectType == ObjectType.FIXTURE)
        val leagueSeason = fixture.leagueSeason

        return when (tagger) {
            is RankDiff -> {
                if (leagueSeason.isCup()) return null
                val rankDiff = tagger.rankDiff
                val aStanding = app.standingRepository.find(fixture.teamAId, fixture.leagueSeason)
                    ?: error("no standing find for team - $fixture.teamAId")
                val bStanding = app.standingRepository.find(fixture.teamBId, fixture.leagueSeason)
                    ?: error("no standing find for team - $fixture.teamBId")
                val diff = abs(aStanding.rank - bStanding.rank)
                if (diff <= abs(rankDiff)) {
                    Tag("rank diff - $diff")
                } else {
                    null
                }
            }

            is PointsDiff -> {
                if (leagueSeason.isCup()) return null

                val pointsDiff = tagger.pointDiff
                val aStanding = app.standingRepository.find(fixture.teamAId, fixture.leagueSeason)
                    ?: error("no standing find for team - $fixture.teamAId")
                val bStanding = app.standingRepository.find(fixture.teamBId, fixture.leagueSeason)
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
                //TODO server timezone, if client is different, need modify
                val tz = TimeZone.of("Asia/Shanghai")
                val local = fixtureTimeIn.toLocalDateTime(tz)
                return if (night.timeIn(local.time))
                    Tag("awkward time - ${local.time}", power = Power.NEGATIVE)
                else null
            }

            else -> error("not supported tagger")
        }
    }
}

