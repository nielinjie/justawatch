package xyz.nietongxue.soccerTime

import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.ReactHTML.li

fun compositedDetail(details: List<FixtureDetailed>): CompositedList<FixtureDetailed> {
    return details.filterNot { it.underLines.contains(UnderLine.HIDDEN) }.toComposited {
        it.underLines.contains(UnderLine.COLLAPSED)
    }
}


external interface ListItemProps : Props {
    var value: CompositedListItem<FixtureDetailed>
}

val ListItemComponent = FC<ListItemProps> { props ->
    li {
        className = ClassName("fixture-item")
        when (val v = props.value) {
            is Single -> {
                FixtureComponent {
                    value = v.value
                    underLines = v.value.underLines
                }
            }

            is Composited -> {
                CompositedFixtureComponent {
                    details = v.values
                }
            }
        }
    }
}

