package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import mui.icons.material.PlayArrow
import mui.material.SvgIconSize


external interface ToNowProps : Props {
    var onClick: () -> Unit
}

val ToNowButton = FC<ToNowProps> { props ->
    val radius = 24
    button {
        css {
            padding = 0.px
            position = Position.absolute
            bottom = 10.pct
            right = 10.pct
            width = radius*2.px
            height = radius*2.px
            borderRadius = radius.px
            zIndex = integer(1000)
            borderWidth = 3.px
            fontSize =radius*2*0.7.px
            fontWeight = FontWeight.bolder
            fontFamily = FontFamily.monospace
            opacity = number(0.7)
            textAlign = TextAlign.center

        }
        onClick = { props.onClick() }
        PlayArrow{

        }
    }
}