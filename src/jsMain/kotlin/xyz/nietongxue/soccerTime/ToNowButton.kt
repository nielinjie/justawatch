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
//val ToNowButton = FC<ToNowProps> { props ->
//    val radius = 24
//    button {
//        css {
//            padding = 0.px
//
//            width = radius*2.px
//            height = radius*2.px
//            borderRadius = radius.px
//            zIndex = integer(1000)
//            borderWidth = 3.px
//            fontSize =radius*2*0.7.px
//            fontWeight = FontWeight.bolder
//            fontFamily = FontFamily.monospace
//            opacity = number(0.7)
//            textAlign = TextAlign.center
//
//        }
//        onClick = { props.onClick() }
//        PlayArrow{
//
//        }
//    }
//}