package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import kotlin.js.Date

/**
 * TODO, 设计一下外观
 * hot和watch的，稍微大一点
 * next比较重要，毕竟现在这个是个"watch" 可以把时间单独显示。
 *
 */
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
        if (props.underLine == UnderLine.NEXT) {
            div {//clock head
                css {
                    fontFamily = FontFamily.monospace
                    color = NamedColor.green
                    fontSize = (22 * 3).px
                    fontWeight = FontWeight.bold
                    textAlign = TextAlign.center
                    marginBottom = (12 * 3).px
                }
                +durationOffset.string
            }
        }
        div {
            css {
                fontSize = (16 * 3).px
                fontWeight = FontWeight.bolder
                position = Position.relative;
                margin = 20.px
                height = 3.em
                fontFamily = FontFamily.sansSerif
                if (!durationOffset.after) color = NamedColor.grey
            }

            div {
                div {
                    +"$teamA "
                }
                div {
                    css {
                        position = Position.absolute
                        right = 0.px
                        top = 0.px
                    }
                    +"$teamB "
                }
            }
            if (props.underLine != UnderLine.NEXT) {

                div {
                    css {
                        marginTop = 10.px
                        fontFamily = FontFamily.monospace
                        fontSize = 80.pct
                    }
                    +if (durationOffset.after) " ${durationOffset.string} " else " "
                    +(fix.result ?: "")

                }
            }

        }
    }
}