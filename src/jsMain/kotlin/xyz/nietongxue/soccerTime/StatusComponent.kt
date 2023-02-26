package xyz.nietongxue.soccerTime

import csstype.*
import emotion.css.merge
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML

external interface StatusProps : Props {
    var fixture: Fixture
    var durationOffset: DurationOffset
}

val StatusComponent = FC<StatusProps> { props ->
    with(props) {
        ReactHTML.div {
            css {
                fontFamily = FontFamily.monospace
                fontSize = 80.pct
                textAlign = TextAlign.center
            }
            +if (durationOffset.after) " ${durationOffset.string} "
            else
                (fixture.result ?: (fixture.status))
        }
    }
}
