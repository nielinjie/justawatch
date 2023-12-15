package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import js.core.jso
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.ul
import react.router.dom.Link
import react.useEffect
import react.useState
import web.html.HTMLElement
import web.scroll.ScrollBehavior
import web.scroll.ScrollLogicalPosition
import kotlin.js.Date

private val scope = MainScope()


val ListComponent = FC<Props> {
    var nextFixtureItemDomElement: HTMLElement? = null

    var fixtures by useState(emptyList<FixtureDetailed>())
    var firstAfterNow by useState<Int>(-1)
    var time by useState(Date.now().toLong())
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
            val f = getFixturesDetailed().defaultSort()
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



}