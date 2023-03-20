import csstype.*
import emotion.react.css
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.jso
import mui.icons.material.More
import org.w3c.dom.HTMLLIElement
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import xyz.nietongxue.soccerTime.*
import kotlin.js.Date

private val scope = MainScope()


val ListComponent = FC<Props> {
    var nextFixtureItemDomElement: HTMLLIElement? = null
    var centralFixtureItemDomElement: HTMLLIElement? = null

    var fixtures by useState(emptyList<FixtureDetailed>())
    var first by useState<Int>(-1)
    var time by useState(Date.now().toLong())
    fun scrollToCenter() {
        nextFixtureItemDomElement?.scrollIntoView(jso {
            block = "center"
            behavior = "smooth"
        })

    }

    fun scrollAndRefresh() {
        time = Date.now().toLong()
//        first = fixtures.indexOfFirst {
//            (it.fixture.date * 1000) > time
//        }
////        setTimeout({
//            scrollToCenter()
//        })

    }



    useEffect(time) {
        scope.launch {
            val f = getFixturesDetailed().sortedBy { it.fixture.date }
            first = f.indexOfFirst {
                (it.fixture.date * 1000) > time
            }
            fixtures = f
        }
    }
    div {
        css(ClassName("center-line")) {
            position = Position.absolute
            top = 50.pct
        }
    }
    div {
        css {
            position = Position.absolute
            bottom = 2.pct
            right = 5.pct
        }
        MoreButton {
            onClick = {
                console.log("clicked")
            }
        }
        ToNowButton {
            onClick = ::scrollAndRefresh
        }
    }

    ul {
        css {
            listStyleType = None.none
            height = 98.vh
            overflowY = Auto.auto
            overflowX = Overflow.hidden
            paddingInlineStart = 0.px
        }

        for ((index, fixture) in fixtures.withIndex()) {
            li {
                if (index == first) {
                    ref = RefCallback {
                        nextFixtureItemDomElement = it
                        scrollToCenter()
                    }
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