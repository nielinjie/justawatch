package xyz.nietongxue.soccerTime


import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.conditionalheaders.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.reflect.*
import org.kodein.di.instance
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientCN


val applicationHttpClient = HttpClient(CIO) {
    install(ClientCN) {
        json()
    }
}


fun main() {
    val devMode: Boolean = System.getenv("devMode") != null

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
        install(Sessions) {
            cookie<UserSession>("user_session")
        }


        val redirects = mutableMapOf<String, String>()
        install(Authentication) {
            oauth("auth-oauth-github") {
                // Configure oauth authentication
                urlProvider = {
                    if (devMode) "https://127.0.0.1:9000/oa/github/callback"
                    else "https://justawatch.qingyangyunyun.cloud/oa/github/callback"
                }
                providerLookup = {
                    OAuthServerSettings.OAuth2ServerSettings(
                        name = "github",
                        authorizeUrl = "https://github.com/login/oauth/authorize",
                        accessTokenUrl = "https://github.com/login/oauth/access_token",
                        requestMethod = HttpMethod.Post,
                        clientId = System.getenv("GITHUB_CLIENT_ID"),
                        clientSecret = System.getenv("GITHUB_CLIENT_SECRET"),
                        defaultScopes = emptyList(),
                        onStateCreated = { call, state ->
                            //saves new state with redirect url value
                            //如果url中显式具有?redirectUrl=xxx，那么就用这个redirectUrl
                            //但为啥需要在这里保存呢？
                            //这个state会被github原样返回，可以用于区别不同的call实例。
                            call.request.queryParameters["redirectUrl"]?.let {
                                redirects[state] = it
                            }
                        }
                    )
                }
                client = applicationHttpClient
            }
        }

        routing {

//            static("/") {
//                resources("")
//            }
            staticResources("/", "", index = "index.html")

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
                    val filtered = Filtering(session, app).filter(re)
                    call.respond(filtered)
                }
            }
            post("/api/callers/restart") {
                callers.forEach {
                    it.restart()
                }
            }
            route("/api/status/{api?}") {
                get {
                    call.respond(ServiceStatusRepository.calling(call.parameters["api"]))
                }
            }
            route("/api/user") {
                get {
                    val session = call.sessions.get<UserSession>()
                    if (session == null) {
                        call.respond(HttpStatusCode.Forbidden)
                    } else {
                        val user = app.userRepository.get(session)
                        call.respond(user)
                    }
                }
            }
            route("/api/user/session") {
                get {
                    val session = call.sessions.get<UserSession>()
                    if (session == null)
                        call.respond(HttpStatusCode.NotFound)
                    else
                        call.respond(session)
                }
            }

            authenticate("auth-oauth-github") {
                get("/loginGithub") {
                    // Redirects to 'authorizeUrl' automatically
                }

                get("/oa/github/callback") {
                    val currentPrincipal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                    // redirects home if the url is not found before authorization
                    currentPrincipal?.let { principal ->
                        principal.state?.let { state ->
                            call.sessions.set(UserSession("github", principal.accessToken))
                            redirects[state]?.let { redirect ->
                                call.respondRedirect(redirect)
                                return@get
                            }
                        }
                    }
                    call.respondRedirect("/")
                }
            }

//            get("/") {
//                //这个是支持index.html省略的。
//                call.respondText(
//                    this::class.java.classLoader.getResource("index.html")!!.readText(),
//                    ContentType.Text.Html
//                )
//            }
        }
    }.start(wait = true)
}