package xyz.nietongxue.soccerTime.noUser

import csstype.AlignItems
import csstype.Display
import csstype.FlexDirection
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
        }
        +"This is user page"


    }
    div {
        a {
            href = "/loginGithub"
            +"Login with Github"
        }
    }

}