import csstype.*
import emotion.react.css
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.js.jso
import org.w3c.dom.HTMLLIElement
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import xyz.nietongxue.soccerTime.*
import xyz.nietongxue.soccerTime.util.throttle
import kotlin.js.Date
private val scope = MainScope()



val ListComponent = FC<Props> {
    var nextFixtureItemDomElement: HTMLLIElement? = null
    var centralFixtureItemDomElement: HTMLLIElement? = null


    fun scrollToCenter() {
        nextFixtureItemDomElement?.scrollIntoView(jso {
            block = "center"
            behavior = "smooth"
        })
    }

    fun focusCentral():Unit  {
//        centralFixtureItemDomElement?.classList?.remove(focusCss.toString())
//        centralFixtureItemDomElement = findElementAt("center-line")
//        centralFixtureItemDomElement?.classList?.add(focusCss.toString())
//        centralFixtureItemDomElement?.style?.transform = "scale(1.2)"
//        centralFixtureItemDomElement?.style?.transformOrigin = "left center"

    }

    var fixtures by useState(emptyList<FixtureDetailed>())
    var first by useState<Int>(-1)

    useEffectOnce {
        scope.launch {
            val f = getFixturesDetailed().sortedBy { it.fixture.date }
            first = f.indexOfFirst {
                (it.fixture.date * 1000) > Date.now().toLong()
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
    ToNowButton {
        onClick =  ::scrollToCenter
    }

    ul {
        css {
            listStyleType = None.none
            height = 90.vh
            overflowY = Auto.auto
            overflowX = Overflow.hidden
            paddingInlineStart = 0.px
        }
        onScroll = {
            throttle(::focusCentral, 500).invoke()
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