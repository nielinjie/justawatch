package xyz.nietongxue.soccerTime

import io.ktor.http.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*


val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}


suspend fun getFixturesDetailed(): List<FixtureDetailed> {
    return jsonClient.get("/api/fixtures/detailed").body()
}
