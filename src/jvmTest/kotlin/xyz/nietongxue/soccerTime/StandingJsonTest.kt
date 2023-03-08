package xyz.nietongxue.soccerTime

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.Test

class StandingJsonTest : StringSpec({
    val standingJson = javaClass.getResource("/standingApiResponse.json")!!.readText()

    "parse standing json response" {
        val re = fromStandingResponse(standingJson)
        re.shouldHaveSize(20)
    }
})