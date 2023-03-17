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
import org.kodein.di.instance


fun main() {
    val productMode: Boolean = System.getenv("productMode") != null

    val port = System.getenv("PORT")?.toInt() ?: 9000
    val callers by diMe.instance<Set<ApiCaller>>()
    val app by diMe.instance<App>()

    callers.forEach { it.init() }
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
            route("/api/fixtures") {
                get {
                    call.respond(app.fixtureRepository.find())
                }
            }
            route("/api/fixtures/detailed") {
                get {
                    //TODO use session id from client
                    //https://ktor.io/docs/sessions.html#custom_storage
                    val sessionId = "_me"
                    val session = app.sessionRepository.get(sessionId) ?: error("no session find")
                    val fixtures = app.fixtureRepository.find()
                    val standings = app.standingRepository.standings.associateBy { it.teamId }
                    val teams = app.teamRepository.teams.associateBy { it.id }
                    val tagging = Tagging(session, app)
                    val fixtureWithTags = tagging.tagging(fixtures)
                    val re: List<FixtureDetailed> = fixtureWithTags.map {
                        FixtureDetailed(
                            it.fixture,
                            standings[it.fixture.teamAId]!! to standings[it.fixture.teamBId]!!,
                            teams[it.fixture.teamAId]!! to teams[it.fixture.teamBId]!!,
                            it.tags + it.aTeamTags + it.bTeamTags
                        )
                    }
                    call.respond(re)
                }
            }
            route("/api/standings/{teamId?}") {
                get {
                    call.respond(

                        call.parameters["teamId"]?.let {
                            app.standingRepository.findById(it.toInt())
                        } ?: emptyList<Standing>())
                }
            }
            route("/api/status/{api?}") {
                get {
                    call.respond(ServiceStatusRepository.calling(call.parameters["api"]))
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