import csstype.*
import emotion.react.css
import js.core.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import mui.material.*
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.ul
import web.html.HTMLElement
import web.html.HTMLLIElement
import web.scroll.ScrollBehavior
import web.scroll.ScrollLogicalPosition
import xyz.nietongxue.soccerTime.*
import kotlin.js.Date

private val scope = MainScope()


val ListComponent = FC<Props> {
    var nextFixtureItemDomElement: HTMLElement? = null

    var fixtures by useState(emptyList<FixtureDetailed>())
    var firstAfterNow by useState<Int>(-1)
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
        scrollToCenter()


//        first = fixtures.indexOfFirst {
//            (it.fixture.date * 1000) > time
//        }
//        setTimeout({
        time = Date.now().toLong()
//        })

    }



    useEffect(time) {
        scope.launch {
            val f = getFixturesDetailed().sortedBy { it.fixture.date }
            firstAfterNow = f.indexOfFirst {
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

//        for ((index, fixture) in fixtures.withIndex()) {
//            if (!(fixture.underLines.contains(UnderLine.HIDDEN))) {
//                li {
//                    if (index == firstAfterNow) {
//                        ref = RefCallback {
//                            nextFixtureItemDomElement = it
//                            scrollToCenter()
//                        }
//                    }
//                    className = ClassName("fixture-item")
//                    FixtureComponent {
//                        value = fixture
//                        underLines =
//                            if (index == firstAfterNow) (fixture.underLines + UnderLine.NEXT) else fixture.underLines
//                    }
//                }
//            }
//        }
        val compositedViewMode = compositedDetail(fixtures).toItemViewMode()
        compositedViewMode.forEach {
            ListItemComponent {
                value = it
                callback = {
                    nextFixtureItemDomElement = it
                    scrollToCenter()
                }
            }
        }
    }


    Dialog {
        this.open = openDialog
        onClose = { _, _ -> openDialog = false }
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