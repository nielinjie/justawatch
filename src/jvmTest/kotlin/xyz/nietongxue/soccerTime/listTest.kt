package xyz.nietongxue.soccerTime

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class listTest : StringSpec({
    "drop" {
        val list = listOf(1, 2, 3, 4, 5)
        val re = list.dropWhile { it != 3 }
        re.shouldBe(listOf(3, 4, 5))
    }
    "findNearest" {
        fun <A : Any> findNearest(list: List<A>, f: (A) -> Boolean): List<Int> {
            val re = mutableListOf<Int>()
            for (i in list.indices) {
                if (f(list[i])) {
                    if (i == 0 || re[i - 1] == -1) {
                        re.add(i)
                    } else {
                        re.add(re[i - 1])
                    }
                } else {
                    re.add(-1)
                }
            }
            return re
        }

        val list = listOf(false, true, true, false, true, true, true, false)
        val re = findNearest(list) { it }
        re shouldBe listOf(-1, 1, 1, -1, 4, 4, 4, -1)

        val list2 = listOf(true, true, true, true, true, true, true, true)
        findNearest(list2) { it } shouldBe listOf(0, 0, 0, 0, 0, 0, 0, 0)
        val list3 = listOf(false, false, false, false, false, false, false, false)
        findNearest(list3) { it } shouldBe listOf(-1, -1, -1, -1, -1, -1, -1, -1)
        val list4 = listOf(true, false, false, false, false, false, false, false)
        findNearest(list4) { it } shouldBe listOf(0, -1, -1, -1, -1, -1, -1, -1)
    }
    "merge by value" {


        fun <A : Any> com(list: List<Int>, valueList: List<A>): List<CompositedListItem<A>> {
            require(list.size == valueList.size)
            val re = mutableListOf<CompositedListItem<A>>()
            for (i in list.withIndex()) {
                if (i.value == -1) {
                    re.add(Single(i.index, valueList[i.index]))
                } else {
                    if (re.isEmpty() || re.last() is Single) {
                        re.add(Composited(i.value))
                    }
                    (re.last() as Composited).values.add(valueList[i.index])

                }
            }
            return re
        }

        val vlist = listOf(false, true, true, false, true, true, true, false)
        val list = listOf(-1, 1, 1, -1, 4, 4, 4, -1)
        com(list, vlist) shouldBe listOf(
            Single(0, false),
            Composited<Boolean>(1).apply {
                values.add(true)
                values.add(true)
            },
            Single(3, false),
            Composited<Boolean>(4).apply {
                values.add(true)
                values.add(true)
                values.add(true)
            },
            Single(7, false)
        )
    }
    "composite"{
        val list = listOf(false, true, true, false, true, true, true, false)
        val re = list.toComposited { it }
        re shouldBe listOf(
            Single(0, false),
            Composited<Boolean>(1).apply {
                values.add(true)
                values.add(true)
            },
            Single(3, false),
            Composited<Boolean>(4).apply {
                values.add(true)
                values.add(true)
                values.add(true)
            },
            Single(7, false)
        )
    }
})