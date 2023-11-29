package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.RefCallback
import react.dom.html.ReactHTML
import web.html.HTMLElement


external interface HeadLightProps : Props {
    var callback: (HTMLElement?) -> Unit
}

val HeadLightComponent = FC<HeadLightProps> { props ->
    ReactHTML.div {//clock head
        css {
            fontFamily = FontFamily.monospace
            color = NamedColor.green
            fontSize = 22.px
            fontWeight = FontWeight.bold
            textAlign = TextAlign.center
            marginBottom = 10.px
            marginTop = 12.px
        }
        ref = RefCallback {
            props.callback(it)
        }
        +"Start Here"
    }
}