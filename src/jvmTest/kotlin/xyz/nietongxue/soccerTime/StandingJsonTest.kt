package xyz.nietongxue.soccerTime

import org.junit.Test

class StandingJsonTest {
    val standingJson = javaClass.getResource("/standingApiResponse.json")!!.readText()
    @Test
    fun parse() {
        val re = fromStandingResponse(standingJson)
        assert(re.size == 20)
    }
}