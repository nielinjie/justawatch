package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.create
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span
import react.useState
import kotlin.js.Date

/**
 * TODO, 设计一下外观
 * hot和watch的，稍微大一点
 * next比较重要，毕竟现在这个是个"watch" 可以把时间单独显示。
 *
 */

enum class Direction {
    LEFT, RIGHT
}

external interface FixtureProps : Props {
    var value: FixtureDetailed
    var underLines: Set<UnderLine>
}

val FixtureComponent = FC<FixtureProps> { props ->
    val (expanded, setExpanded) = useState<Boolean>(false)
    val collapsed = props.underLines.contains(UnderLine.COLLAPSED)
    val fixtureDetailed = props.value
    val durationOffset = durationOffset(fixtureDetailed.fixture.date * 1000, Date.now().toLong())
    fun itemCollapsed(): Boolean {
//        return (durationOffset.after && collapsed) && !expanded
        return false
    }

    with(fixtureDetailed) {
        div {
            onClick = { setExpanded(!expanded) }
            css {
                fontSize = 16.px
                marginLeft = 20.px
                marginRight = 20.px
                marginTop = if (itemCollapsed()) {
                    (5).px
                } else {
                    32.px
                }
                marginBottom = if (itemCollapsed()) {
                    (5).px
                } else {
                    32.px
                }
                position = Position.relative
            }

            div {
                css {
                    fontWeight = FontWeight.bolder
                    height = if (itemCollapsed()) 1.em else 3.em
                    fontFamily = FontFamily.sansSerif
                    if (!durationOffset.after) color = NamedColor.grey

                    // flex controlling
                    display = Display.flex
                    flexDirection = FlexDirection.row
                    justifyContent = JustifyContent.spaceBetween
                    alignItems = AlignItems.center

                }
                if (itemCollapsed()) {
                    div {}
                    TagsCom {
                        tags = this@with.tags
                        this.collapsed = collapsed
                    }
                    div {}
                } else {
                    TeamComponent {
                        team = teams.first
                        direction = Direction.LEFT
                    }

                    TagsCom {
                        tags = this@with.tags
                        this.collapsed = collapsed
                    }

                    LeagueLogoCom {
                        fixture = fixtureDetailed.fixture
                    }

                    TeamComponent {
                        team = teams.second
                        direction = Direction.RIGHT

                    }
                }
            }

            //状态段
            if (expanded) {
                StandingComponent {
                    this.fixtureDetailed = fixtureDetailed
                }
                ExpandTags {
                    this.tags = this@with.tags
                }
            }
            //结果段
            if (!props.underLines.contains(UnderLine.NEXT)) {
                if (!itemCollapsed()) {
                    StatusComponent {
                        fixture = fixtureDetailed.fixture
                        this.durationOffset = durationOffset
                    }
                }
            }
        }
    }
}