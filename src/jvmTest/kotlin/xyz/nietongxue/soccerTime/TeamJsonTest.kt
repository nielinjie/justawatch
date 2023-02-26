package xyz.nietongxue.soccerTime

import org.junit.Test

class TeamJsonTest {
    val teamJson = javaClass.getResource("/teamInfoApiResponse.json")!!.readText()
    @Test
    fun parse() {
        val re = fromTeamResponse(teamJson)
        assert(re.size == 20)
    }
}