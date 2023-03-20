package xyz.nietongxue.soccerTime

import csstype.Display
import csstype.em
import emotion.react.css
import mui.icons.material.MoreHoriz
import mui.icons.material.PlayArrow
import mui.material.Fab
import mui.material.FabColor
import mui.material.Size
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span

external interface MoreProps : Props {
    var onClick: () -> Unit
}


val MoreButton = FC<MoreProps> { props ->
    div {
        css {
            display = Display.inlineBlock
            marginRight = 1.em
        }
        Fab {

            onClick = {
                props.onClick()
            }
            color = FabColor.default
            size = Size.small
            MoreHoriz {

            }

        }
    }
}