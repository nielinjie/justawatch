package xyz.nietongxue.soccerTime

import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.span
import react.useState

external interface CompositedDetailed : Props {
    var details: List<FixtureDetailed>
}

val CompositedFixtureComponent = FC<CompositedDetailed> {
    var expanded by useState(false)

    if (expanded) {
        it.details.forEach {
            li {
                FixtureComponent {
                    value = it
                    underLines = it.underLines
                }
            }
        }

    } else {
        div {
            css {
                display = Display.flex
                flexDirection = FlexDirection.row
                justifyContent = JustifyContent.center
                alignItems = AlignItems.center
            }
            div {
                onClick = { expanded = true }
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
}