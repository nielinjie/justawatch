package xyz.nietongxue.soccerTime

import ListComponent
import csstype.*
import csstype.HtmlAttributes.Companion.href
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img


val App = FC<Props> {

        div {
            css {
                "@media(min-width: 800px)" {
                    display = Display.block
                    height = 40.vh
                }
                display = None.none
                margin = 2.em
                fontSize = 2.em
                fontFamily = FontFamily.monospace
            }
            div {
                css {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    alignItems = AlignItems.center
                }
                +"This app is designed for phone, use small screen for best experience."

                img {
                    css {
                        marginTop = 2.em
                    }
                    src = "/qr - just.png"
                    width = 150.0
                }
            }
        }
    div {
        css{
            "@media(min-width: 800px)" {
                height = 50.vh
            }
            height = 100.vh
        }
        ListComponent {
        }
    }
}