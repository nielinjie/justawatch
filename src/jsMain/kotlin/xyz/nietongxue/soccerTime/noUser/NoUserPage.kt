package xyz.nietongxue.soccerTime.noUser

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.div

val NoUserPage = FC<Props> {
    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = AlignItems.center
            fontSize = 2.em
            fontFamily = FontFamily.monospace
        }
        div {
            +"This is the page of no user."
        }
        div {
            +"You can do nothing here, until you login."
        }
    }

    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = AlignItems.center
            fontSize = 2.em
            fontFamily = FontFamily.monospace
        }

        a {
            href = "/loginGithub"
            +"Login with Github"
        }
    }

}