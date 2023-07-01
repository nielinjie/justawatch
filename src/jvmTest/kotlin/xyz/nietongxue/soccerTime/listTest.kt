package xyz.nietongxue.soccerTime

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class listTest:StringSpec( {
    "drop" {
        val list = listOf(1, 2, 3, 4, 5)
        val re = list.dropWhile { it !=3 }
        re.shouldBe(listOf(3,4,5))
    }
})