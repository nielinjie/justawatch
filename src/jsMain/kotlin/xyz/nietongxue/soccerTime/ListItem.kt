package xyz.nietongxue.soccerTime

import csstype.ClassName
import react.FC
import react.Props
import react.dom.html.ReactHTML.li
import web.html.HTMLElement
import kotlin.js.Date

fun compositedDetail(details: List<FixtureDetailed>): CompositedList<FixtureDetailed> {
    return details.filterNot { it.underLines.contains(UnderLine.HIDDEN) }.toComposited {
        it.underLines.contains(UnderLine.COLLAPSED)
    }
}


external interface ListItemProps : Props {
    var value: ItemViewMode
    var callback: (HTMLElement?) -> Unit
}

interface ItemViewMode
class SingleFixture(val single: Single<FixtureDetailed>) : ItemViewMode
class CompositedFixture(val composited: Composited<FixtureDetailed>) : ItemViewMode
class HeadLight(val nextDate: Long) : ItemViewMode

fun CompositedList<FixtureDetailed>.toItemViewMode(): List<ItemViewMode> {
    val list = this.map {
        when (it) {
            is Single -> SingleFixture(it)
            is Composited -> CompositedFixture(it)
            else -> error("no known type")
        }
    }
//    val firstIndex: Int = this.indexOfFirst {
//        (when (it) {
//            is Single -> it.value
//            is Composited -> it.values.first()
//            else -> error("no known type")
//        }.fixture.date * 1000) > Date.now().toLong()
//    }
    this.map {
        when (it) {
            is Single -> it.value
            is Composited -> it.values.first()
            else -> error("no known type")
        }
    }.withIndex().firstOrNull {
        it.value.fixture.date * 1000 > Date.now().toLong()
    }?.let {
        it.index to it.value.fixture.date * 1000
    }?.also {
        val (nextIndex, nextDate) = it
        return list.toMutableList().also {
            it.add(nextIndex, HeadLight(nextDate))
        }
    }

    return list
}

val ListItemComponent = FC<ListItemProps> { props ->
    li {
        className = ClassName("fixture-item")
        when (val v = props.value) {
            is SingleFixture -> {
                FixtureComponent {
                    value = v.single.value
                    underLines = v.single.value.underLines
                }
            }

            is CompositedFixture -> {
                CompositedFixtureComponent {
                    details = v.composited.values
                }
            }

            is HeadLight -> {
                HeadLightComponent {
                    callback = props.callback
                    nextDate = v.nextDate
                }
            }

            else -> error("no known type")
        }
    }
}


