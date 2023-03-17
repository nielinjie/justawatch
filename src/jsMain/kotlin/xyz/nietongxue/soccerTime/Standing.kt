package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span


external interface StandingProps : Props {
    var fixtureDetailed: FixtureDetailed
}

val StandingComponent = FC<StandingProps> { props ->
    with(props) {


        div {
            css {

                // flex controlling
                display = Display.flex
                flexDirection = FlexDirection.row
                justifyContent = JustifyContent.spaceBetween
                alignItems = AlignItems.center

            }
            TeamStandingCom {
                standing = fixtureDetailed.standings.first
                direction = Direction.LEFT
            }
            TeamStandingCom {
                standing = fixtureDetailed.standings.second
                direction = Direction.RIGHT
            }

        }


    }
}

val TeamStandingCom = FC<TeamStandingProps> { props ->
    with(props) {
        div {
            css {
                flexDirection = when (this@with.direction) {
                    Direction.LEFT -> FlexDirection.row
                    Direction.RIGHT -> FlexDirection.rowReverse
                }
            }
            span {
                css {
                    fontSize = 12.px
                    fontFamily = FontFamily.monospace
                }
                +"@${standing.rank} ${standing.points}p "
                FormCom {
                    form = standing.form
                }
            }
        }

    }
}

external interface TeamStandingProps : Props {
    var standing: Standing
    var direction: Direction
}


val FormCom = FC<FormProps> { props ->
    with(props) {
        span {
            form.formChars.map{
                Point{
                    color = when(it){
                        "W" -> NamedColor.mediumaquamarine
                        "L" -> NamedColor.dimgray
                        "D" -> NamedColor.cornflowerblue
                        else -> error("not supported")
                    }
                }
            }
        }
    }

}

external interface FormProps : Props {
    var form: Form
}
external  interface PointProps:Props{
    var color:Color
}

val Point = FC<PointProps> { props ->
    with(props) {
        div {
            css {
                marginRight = 5.px
                display = Display.inlineBlock
                backgroundColor = this@with.color
                width = 6.px
                height = 6.px
                borderRadius = 3.px
            }
        }
    }
}
