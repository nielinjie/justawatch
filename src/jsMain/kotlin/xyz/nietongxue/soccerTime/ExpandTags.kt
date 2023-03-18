package xyz.nietongxue.soccerTime

import csstype.em
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div


external interface ETProps : Props {
    var tags: List<Tag>
}

val ExpandTags = FC<ETProps> {
    val tags = it.tags
    div {
        css {
            marginBottom = 1.em
            marginTop = 1.em
        }
        tags.sortedBy { it.power }.forEach { tag ->
            TagWithTextCom{
                value = tag
            }
        }
    }
}


