package xyz.nietongxue.soccerTime

import io.kotest.core.spec.style.StringSpec
import org.kodein.di.instance

class FilteringTest:StringSpec({
    val filter by diFake.instance<Filtering>()
    val app by di.instance<App>()
    "filter" {
        val fixtures = app.fixtureRepository.fixtures
        TODO("fixtureDetail 比较难构建？")
    }
})
