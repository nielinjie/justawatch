package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import mui.icons.material.PlayArrow
import mui.material.Fab
import mui.material.FabColor
import mui.material.Size
import mui.material.SvgIconSize
import react.dom.html.ReactHTML.div


external interface ToNowProps : Props {
    var onClick: () -> Unit
}


val ToNowButton = FC<ToNowProps> { props ->

    Fab {
        onClick = {
            props.onClick()
        }
        color = FabColor.primary
        size = Size.large
        PlayArrow {
        }

    }
}