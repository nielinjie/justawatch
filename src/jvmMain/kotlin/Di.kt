package xyz.nietongxue.soccerTime

import org.kodein.di.*

val di = DI{
    bindSingleton<App> { defaultApp}
    bindSingleton<List<ApiCaller<*>>> {
        (currents.map {
            TeamCaller(instance(), it)
        } + currents.map {
            StandingCaller(instance(), it)
        } + currents.map {
            FixtureCaller(instance(), it)
        })
    }
}

