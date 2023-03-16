package xyz.nietongxue.soccerTime

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

    private fun tagForTeam(team: Team, tagger: Tagger): Tag? {
        assert(tagger.objectType == ObjectType.TEAM)
        return when (tagger) {
            is NamedTeam -> {
                val names = tagger.names
                if (team.name in names || team.code in names) Tag("named") else null
            }

            is PositionInStanding -> {
                val (league, position) = tagger
                if (position > 0) {
                    val standing = app.standingRepository.findById(team.id) ?: error("no standing find")
                    if (standing.rank <= position) {
                        Tag("position - ${standing.rank}")
                    } else null
                } else if (position < 0) {
                    val pPosition = app.teamRepository.teams.size - position + 1
                    val standing = app.standingRepository.findById(team.id) ?: error("no standing find")
                    if (standing.rank >= pPosition) {
                        Tag("position - ${standing.rank}")
                    } else null
                } else error("position can not be 0")
            }

            else -> null
        }
    }

    private fun tagForFixture(fixture: Fixture, tagger: Tagger, teamWithTags: Map<Int, List<Tag>>): Tag? {
        assert(tagger.objectType == ObjectType.FIXTURE)
        return when (tagger) {
            is League ->{
                Tag("league")
            }
            else -> error("not supported tagger")
        }
    }
}

