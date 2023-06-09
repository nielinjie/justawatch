package xyz.nietongxue.soccerTime


import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.conditionalheaders.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance


fun main() {
    val productMode: Boolean = System.getenv("productMode") != null

    val port = System.getenv("PORT")?.toInt() ?: 9000
    val callers by diMe.instance<List<ApiCaller<*>>>()
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
        //TODO add cache control
        //https://ktor.io/docs/conditional-headers.html#configure
        install(ConditionalHeaders)
        //static content 自动version
        install(CachingHeaders) {
            options { call, content ->
                CachingOptions(CacheControl.NoCache(CacheControl.Visibility.Public))
            }
        }


        install(Compression) {
            gzip()
        }
        routing {
            get("/") {
                //这个是支持index.html省略的。
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
                    call.respond(app.fixtureRepository.fixtures)
                }
            }
            route("/api/fixtures/detailed") {
                get {
                    //TODO use session id from client
                    //https://ktor.io/docs/sessions.html#custom_storage
                    val sessionId = "_me"
                    val session = app.sessionRepository.get(sessionId) ?: error("no session find")
                    val fixtures = app.fixtureRepository.fixtures
//                    val standings = app.standingRepository.standings.associateBy { it.teamId }
                    val tagging = Tagging(session, app)
                    val fixtureWithTags = tagging.tagging(fixtures)
                    val re: List<FixtureDetailed> = fixtureWithTags.map {
                        FixtureDetailed(
                            it.fixture,
                            app.standingRepository.find(
                                it.fixture.teamAId,
                                it.fixture.leagueSeason
                            ) to
                                    app.standingRepository.find(
                                        it.fixture.teamBId, it.fixture.leagueSeason
                                    ),
                            app.teamRepository.findById(it.fixture.teamAId)!!
                                    to app.teamRepository.findById(it.fixture.teamBId)!!,
                            it.tags + it.aTeamTags + it.bTeamTags
                        )
                    }
                    val filtered = Filtering(session,app).filter(re)
                    call.respond(filtered)
                }
            }
            post("/api/callers/restart"){
                callers.forEach{
                    it.restart()
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