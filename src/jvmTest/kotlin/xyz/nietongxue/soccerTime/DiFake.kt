package xyz.nietongxue.soccerTime

import fakeSession
import fakeApp
import org.kodein.di.*

val diFake = DI {
    bindSingleton<App> { fakeApp }
//    bindSingleton<TaggerFiltering> { TaggerFiltering(instance()) }
//    bindSingleton<SessionRepository> { SessionRepository() }
//    bind<Session> { provider { instance<SessionRepository>().get("_default")!! } }
    bindSingleton<Session> { fakeSession }
    bindSingleton<Tagging> { Tagging(instance(), instance()) }

}