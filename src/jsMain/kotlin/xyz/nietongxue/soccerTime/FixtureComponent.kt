package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.useState
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

enum class Direction {
    LEFT, RIGHT
}

external interface FixtureProps : Props {
    var value: FixtureDetailed
    var underLine: UnderLine?
}

val FixtureComponent = FC<FixtureProps> { props ->
    val (expanded, setExpanded) = useState<Boolean>(false)
    val fixtureDetailed = props.value
    val durationOffset = durationOffset(fixtureDetailed.fixture.date * 1000, Date.now().toLong())
    with(fixtureDetailed) {
        div {

            onClick = { setExpanded(!expanded) }
            css {
                margin = 32.px
            }
            //抬头段
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
                    height = 3.em
                    fontFamily = FontFamily.sansSerif
                    if (!durationOffset.after) color = NamedColor.grey

                    // flex controlling
                    display = Display.flex
                    flexDirection = FlexDirection.row
                    justifyContent = JustifyContent.spaceBetween
                    alignItems = AlignItems.center

                }


                TeamComponent {
                    team = teams.first
                    direction = Direction.LEFT
                }


                TeamComponent {
                    team = teams.second
                    direction = Direction.RIGHT

                }
            }
            if (expanded) {
                //状态段
                StandingComponent {
                    this.fixtureDetailed = fixtureDetailed
                }
            }
            //结果段
            if (props.underLine != UnderLine.NEXT) {

                StatusComponent {
                    fixture = fixtureDetailed.fixture
                    this.durationOffset = durationOffset
                }
            }
        }
    }
}