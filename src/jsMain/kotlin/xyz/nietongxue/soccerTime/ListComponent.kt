import csstype.*
import emotion.react.css
import js.core.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.*
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import web.html.HTMLLIElement
import web.scroll.ScrollBehavior
import web.scroll.ScrollLogicalPosition
import web.timers.setTimeout
import xyz.nietongxue.soccerTime.*
import kotlin.js.Date

private val scope = MainScope()


val ListComponent = FC<Props> {
    var nextFixtureItemDomElement: HTMLLIElement? = null

    var fixtures by useState(emptyList<FixtureDetailed>())
    var first by useState<Int>(-1)
    var time by useState(Date.now().toLong())
    var openDialog by useState(false)
    fun scrollToCenter() {
//        console.log("scrolling")
//        console.log(nextFixtureItemDomElement)
        nextFixtureItemDomElement?.scrollIntoView(jso {
            block = ScrollLogicalPosition.center
            behavior = ScrollBehavior.smooth
        })

    }

    fun scrollAndRefresh() {
        time = Date.now().toLong()

//        first = fixtures.indexOfFirst {
//            (it.fixture.date * 1000) > time
//        }
        setTimeout({
            scrollToCenter()
        })

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
                openDialog = true
            }
        }
        ToNowButton {
            onClick = ::scrollAndRefresh
        }
    }

    ul {
        css {
            listStyleType = None.none
            height = 100.pct
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
                    underLines = if (index == first) (fixture.underLines + UnderLine.NEXT) else fixture.underLines
                }
            }
        }
    }


    Dialog {
        this.open = openDialog
        onClose = { _, _ -> openDialog = false }
//        DialogTitle{
//            + "this is a dialog title"
//        }
        DialogContent {
            DialogContentText {
                +"Some Customize here (TODO)"
            }
        }
        DialogActions {
            Button {
                onClick = { _ -> openDialog = false }
                +"OK"
            }
        }
    }
}