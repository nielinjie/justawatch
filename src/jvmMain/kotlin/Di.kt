package xyz.nietongxue.soccerTime

import org.kodein.di.*

val di = DI{
    bindSingleton<App> { defaultApp}
    bindSet<ApiCaller> {
        add {singleton { FixtureCaller(instance()) }}
        add {singleton { StandingCaller(instance()) }}
        add {singleton { TeamCaller(instance()) }}
    }
}