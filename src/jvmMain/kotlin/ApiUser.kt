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


abstract class ApiUser<R>(val apiId:String) {
    init {
        loggingConfiguration { DEFAULT_CONSOLE() }
    }

    val logger = logger("api user")

    abstract val url: String
    abstract val params: Map<String, String>
    abstract fun withResponse(stringBody: String):R
    suspend fun getting():R {
        val api = System.getenv("API")
        logger.info("begin to calling api - $apiId")
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(url) {
            url {
                params.forEach {
                    parameter(it.key, it.value)
                }
                header("X-RapidAPI-Key", api)
                header("X-RapidAPI-Host", "api-football-v1.p.rapidapi.com")
            }
        }
        if (response.status == HttpStatusCode.OK) {
            val stringBody: String = response.body()
            logger.info("calling api succeeded - $apiId")
            val r = withResponse(stringBody)
            logger.info("proceeded - $apiId")
            return r
        } else {
            logger.error("calling api failed - $apiId")
            logger.info(response.body<String>())
            error("response error - ${response.status}")
        }
    }
}


