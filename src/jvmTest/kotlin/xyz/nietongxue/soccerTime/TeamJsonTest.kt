package xyz.nietongxue.soccerTime

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize

class TeamJsonTest: StringSpec({
    val teamJson = javaClass.getResource("/teamInfoApiResponse.json")!!.readText()
    "parse" {
        val re = fromTeamResponse(teamJson)
        re.shouldHaveSize(20)
    }
})