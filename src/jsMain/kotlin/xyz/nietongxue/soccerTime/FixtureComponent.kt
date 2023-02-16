package xyz.nietongxue.soccerTime

import csstype.NamedColor
import csstype.px
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import kotlin.js.Date


enum class UnderLine {
    NEXT, WATCHED, HOT
}

external interface FixtureProps : Props {
    var value: Fixture
    var underLine: UnderLine?
}

val FixtureComponent = FC<FixtureProps> { props ->
    val fix = props.value
    val durationOffset = durationOffset(fix.date * 1000, Date.now().toLong())
    with(fix) {
        div {
            css {
                margin = 10.px
                if (!durationOffset.after) color = NamedColor.grey
                else {
                    if (props.underLine != null)
                        color = when (props.underLine) {
                            UnderLine.NEXT -> NamedColor.green
                            else -> NamedColor.blue
                        }
                }
            }

            +"$teamA - $teamB "
            +if (durationOffset.after) " ${durationOffset.string} " else " "
            +(fix.result ?: "")
        }
    }
}