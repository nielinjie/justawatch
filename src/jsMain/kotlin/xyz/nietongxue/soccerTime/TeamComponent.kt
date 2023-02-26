package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img



external interface LogoProps:Props{
    var team:Team
    var direction:Direction
}
val TeamComponent = FC<LogoProps> { props ->
    val team = props.team
    val direction = props.direction
    div {
        css{
            display = Display.flex
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
            flexDirection = when(direction){
                Direction.LEFT -> FlexDirection.row
                Direction.RIGHT -> FlexDirection.rowReverse
            }
            fontFamily = FontFamily.monospace
        }
        + team.code
        img {
            css {
                width = 2.em
                height = 2.em
                marginLeft = 2.em
                marginRight = 2.em
            }
            src = team.logo
        }
    }
}