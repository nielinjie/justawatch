package xyz.nietongxue.soccerTime

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import org.kodein.di.instance


class TaggerTest : StringSpec({
    val filters: TaggerFiltering by diFake.instance()
    val app: App by diFake.instance()
    "league" {
        val league = League("EP")
        val re = filters.filter(app.fixtureRepository.fixtures, league)
        re.shouldHaveSize(380)
    }
    "team name" {
        val team = NamedTeam(listOf("MUN"))
        val re = filters.filter(app.fixtureRepository.fixtures, team)
        re.shouldHaveSize(38)
    }
})