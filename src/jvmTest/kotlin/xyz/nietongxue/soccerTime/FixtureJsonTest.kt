package xyz.nietongxue.soccerTime


import kotlinx.serialization.json.*
import org.junit.Test
import java.io.File
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class FixtureJsonTest {

    private val jsonString = File("./fixturesApiResponse.json").bufferedReader().use { it.readText() }
    private val fixtureString = """
        {
      "fixture": {
        "id": 867947,
        "referee": "Andy Madley, England",
        "timezone": "UTC",
        "date": "2022-08-06T11:30:00+00:00",
        "timestamp": 1659785400,
        "periods": {
          "first": 1659785400,
          "second": 1659789000
        },
        "venue": {
          "id": 535,
          "name": "Craven Cottage",
          "city": "London"
        },
        "status": {
          "long": "Match Finished",
          "short": "FT",
          "elapsed": 90
        }
      },
      "league": {
        "id": 39,
        "name": "Premier League",
        "country": "England",
        "logo": "https://media-3.api-sports.io/football/leagues/39.png",
        "flag": "https://media-3.api-sports.io/flags/gb.svg",
        "season": 2022,
        "round": "Regular Season - 1"
      },
      "teams": {
        "home": {
          "id": 36,
          "name": "Fulham",
          "logo": "https://media.api-sports.io/football/teams/36.png",
          "winner": null
        },
        "away": {
          "id": 40,
          "name": "Liverpool",
          "logo": "https://media.api-sports.io/football/teams/40.png",
          "winner": null
        }
      },
      "goals": {
        "home": 2,
        "away": 2
      },
      "score": {
        "halftime": {
          "home": 1,
          "away": 0
        },
        "fulltime": {
          "home": 2,
          "away": 2
        },
        "extratime": {
          "home": null,
          "away": null
        },
        "penalty": {
          "home": null,
          "away": null
        }
      }
    }
    """.trimIndent()

    @Test
    fun test() {

        val je = Json.parseToJsonElement(fixtureString)
        val fixture = oneFixture(je)
        assertNotNull(fixture)
    }

    @Test
    fun test2() {
        val fs = fromApiResponse(jsonString)
        assertNotNull(fs)
        assertEquals(380, fs.size)
    }



    @Test
    fun time() {
        val time = 1659785400L * 1000
        val now = Date().time
        val d = (now - time).toDuration(DurationUnit.MILLISECONDS)
        assert(d.inWholeDays >= 188)
    }

    @Test
    fun testTimePresentation() {
//        val duration = 188.toDuration(DurationUnit.MINUTES)
//        assertEquals(toNaturelString(duration,Date().time), "3 hours ")
        val duration2 = 188.toDuration(DurationUnit.HOURS)
        assertEquals(toNaturelString(duration2,Date().time), "7 days")
    }

}