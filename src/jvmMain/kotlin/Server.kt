package xyz.nietongxue.soccerTime


import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.*
import kotlin.concurrent.schedule
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

object FixtureRepository {

    val fixtures = mutableListOf<Fixture>(
    )


    fun find(): List<Fixture> {
        return fixtures
    }


}

fun main() {

    val timer = Timer("apiCallerTimer")
    1.hours.inWholeMilliseconds
    timer.schedule(3.seconds.inWholeMilliseconds, 1.hours.inWholeMilliseconds) {
        runBlocking {
            getting()
        }
    }
    val port = System.getenv("PORT")?.toInt() ?: 9000

//    FixtureRepository.fixtures.addAll(
//        fromApiResponse(File("./fixturesApiResponse.json").bufferedReader().use { it.readText() }) ?: emptyList()
//    )
    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Post)
            allowMethod(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }
        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }
            route("/fixtures") {
                get {
                    call.respond(FixtureRepository.find())
                }
            }
//            route(ShoppingListItem.path) {
//                get {
//                    call.respond(shoppingList)
//                }
//                post {
//                    shoppingList += call.receive<ShoppingListItem>()
//                    call.respond(HttpStatusCode.OK)
//                }
//                delete("/{id}") {
//                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
//                    shoppingList.removeIf { it.id == id }
//                    call.respond(HttpStatusCode.OK)
//                }
//            }
        }
    }.start(wait = true)
}