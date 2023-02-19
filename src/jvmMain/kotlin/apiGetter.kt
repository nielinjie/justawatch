package xyz.nietongxue.soccerTime

import io.klogging.config.DEFAULT_CONSOLE
import io.klogging.config.loggingConfiguration
import io.klogging.logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.io.File

suspend fun getting() {
    loggingConfiguration { DEFAULT_CONSOLE() }
    val logger = logger("main")
    logger.info("begin to calling api")
    val client = HttpClient(CIO)
    val response: HttpResponse = client.get(" https://api-football-v1.p.rapidapi.com/v3/fixtures") {
        url {
            parameter("league", "39")
            parameter("season", "2022")
            header("X-RapidAPI-Key", "8800dd04ddmshd5c8c314ca6cf6ep1e4b0djsnf51fbed06dfb")
            header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
        }
    }
    if (response.status == HttpStatusCode.OK) {

        val stringBody: String = response.body()
        logger.info("calling api succeeded")

        val fixtures = fromApiResponse(stringBody)
        FixtureRepository.fixtures.apply {
            clear()
            addAll(fixtures ?: emptyList())
        }
        logger.info("dumped fixtures")
    } else {
        logger.error("calling api failed")
        logger.info(response.body<String>())
    }
}

suspend fun gettingFromMockFile() {
    loggingConfiguration { DEFAULT_CONSOLE() }
    val logger = logger("main")
    logger.info("loading mock from file")

    val stringBody: String = File("./fixturesApiResponse.json").bufferedReader().use { it.readText() }
    val fixtures = fromApiResponse(stringBody)
    FixtureRepository.fixtures.apply {
        clear()
        addAll(fixtures ?: emptyList())
    }
    logger.info("dumped fixtures")
}