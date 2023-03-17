package xyz.nietongxue.soccerTime

import react.FC
import react.Props
import react.dom.html.ReactHTML.div


external interface ETProps : Props {
    var tags: List<Tag>
}

val ExpandTags = FC<ETProps> {
    val tags = it.tags
    div {
        tags.forEach { tag ->
            TagWithTextCom{
                value = tag
            }
        }
    }
}


