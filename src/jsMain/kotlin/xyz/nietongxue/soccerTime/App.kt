package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.timers.setTimeout
import org.w3c.dom.HTMLLIElement
import org.w3c.dom.asList
import react.*
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import kotlin.js.Date
import kotlin.math.abs

private val scope = MainScope()

val App = FC<Props> {
    var nextItemRef: HTMLLIElement? = null

    fun scrolling() {
        nextItemRef?.scrollIntoView(object {
            val block = "center"
        })
    }

    var fixtures by useState(emptyList<Fixture>())
    var first by useState<Int>(-1)
    val refCallback: RefCallback<HTMLLIElement> = RefCallback {
        nextItemRef = it
        scrolling()
    }
    var nearest: HTMLLIElement? = null

    fun throttle(callbackFn: () -> Unit, limit: Int): () -> Unit {
        var wait = false;
        return { ->
            if (!wait) {
                callbackFn()
                wait = true;
                setTimeout({
                    wait = false;
                }, limit)
            }
        }
    }

    fun findElementAt(at: Int): HTMLLIElement {
        val items = document.getElementsByClassName("fixture-item").asList()
        return items.minBy { abs(it.getBoundingClientRect().top - at) } as HTMLLIElement
    }


    useEffectOnce {
        scope.launch {
            val f = getFixtures().sortedBy { it.date }
            first = f.indexOfFirst {
                (it.date * 1000) > Date.now().toLong()
            }
            fixtures = f
        }
    }

    h1 {
        +"Fixture List"
    }
    button {
        onClick = { scrolling() }
        +"Scroll to haha"
    }
    ul {
        css {
            listStyleType = None.none
            height = 80.vh
            overflowY = Auto.auto
            overflowX = Overflow.hidden
        }
        onScroll = {
            throttle({
                nearest?.style?.transform = ""
                nearest = findElementAt(200)
                nearest?.style?.transform = "scale(1.2)"
                nearest?.style?.transformOrigin = "left center"
            }, 500).invoke()
        }

        for ((index, fixture) in fixtures.withIndex()) {
            li {
                if (index == first) {
                    ref = refCallback
                }

                className = ClassName("fixture-item")
                FixtureComponent {
                    value = fixture
                    underLine = if (index == first) UnderLine.NEXT else null
                }
            }
        }
    }
}