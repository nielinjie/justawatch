package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.span

external interface CompositedDetailed : Props {
    var details: List<FixtureDetailed>
}

val CompositedFixtureComponent = FC<CompositedDetailed> {
    div {
        css {
            display = Display.flex
            flexDirection = FlexDirection.row
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
        }
        div {
            it.details.let {
                if (it.size > 10) {
                    it.subList(0, 5) + it.subList(it.size - 5, it.size)
                } else {
                    it
                }
            }.forEach {
                span {
                    css {
                        paddingLeft = 0.2.em
                        paddingRight = 0.2.em
                    }
                }
                TagsHorizontalCom {
                    tags = it.tags
                    collapsed = true
                }
            }
        }
    }
}