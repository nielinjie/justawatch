package xyz.nietongxue.soccerTime

import csstype.*
import emotion.css.css
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import mui.icons.material.TurnedIn
import react.dom.html.ReactHTML.span

external interface TagsProps: Props {
    var tags:List<Tag>
}
external interface TagProps:Props {
    var value:Tag
}

val TagsCom = FC<TagsProps>{
    div{
        css{

        }
        it.tags.forEach{
            TagCom{

            }
        }
    }
}

val TagCom = FC<TagProps>{
    span {
         css{
             width = 0.3.em
             height = 1.em
             marginLeft = 0.1.em
             marginRight = 0.1.em
             backgroundColor = Color("#7dacbc")
             display = Display.inlineBlock
             borderRadius = 6.px
             borderWidth = 2.px
         }
        + " "
    }
}

val TagWithTextCom = FC<TagProps>{
    div {
        span {
            css{
                width = 0.3.em
                height = 1.em
                marginLeft = 0.1.em
                marginRight = 0.4.em
                backgroundColor = Color("#7dacbc")
                display = Display.inlineBlock
                borderRadius = 6.px
                borderWidth = 2.px
            }
            + " "
        }
        css{
            marginTop = 0.5.em
            marginLeft = 5.em
            fontFamily = FontFamily.monospace
            fontSize = 12.px
            display = Display.flex
            flexDirection = FlexDirection.row
        }
        + it.value.text
    }
}

