package xyz.nietongxue.soccerTime


class  TaggerFiltering(val app:App) {
    fun filter(fixtures: List<Fixture>, tagger: Tagger): List<Fixture> {
        return when (tagger) {
            is League -> {
                fixtures
            }

            is NamedTeam -> {
                val names = tagger.names
                val ids = (app.teamRepository.teams.filter { it.name in names || it.code in names }).map { it.id }
                fixtures.filter { it.teamAId in ids || it.teamBId in ids }
            }

            is PositionInStanding -> {
                val (league, position) = tagger
                TODO()
            }

            else -> error("unsupported filter")
        }
    }
}
