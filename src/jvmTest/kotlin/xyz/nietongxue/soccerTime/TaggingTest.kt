package xyz.nietongxue.soccerTime

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import org.kodein.di.instance

class TaggingTest : StringSpec({
  val tagging by diFake.instance<Tagging>()
    val app by diFake.instance<App>()
    "tagging" {
        val fixtures = app.fixtureRepository.fixtures
        val re  = tagging.tagging(fixtures)
        re.shouldHaveSize(380)
        (re.filter { it.tags.isNotEmpty()} )shouldHaveSize(38)
    }
})