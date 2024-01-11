package xyz.nietongxue.soccerTime.custom

import csstype.*
import emotion.react.css
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import react.FC
import react.Props
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.pre
import react.router.useNavigate
import react.useEffectOnce
import react.useState
import xyz.nietongxue.soccerTime.UserSession
import xyz.nietongxue.soccerTime.getUserSession

private val scope = MainScope()

interface DoNotKnow<T> {
    val value: T?
    val know: Boolean
}

class Unknown<T> : DoNotKnow<T> {
    override val value: T? = null
    override val know: Boolean = false
}

class Known<T>(override val value: T?) : DoNotKnow<T> {
    override val know: Boolean = true
}

val CustomPage = FC<Props> {
    var session by useState<DoNotKnow<UserSession>>(Unknown())
    val navigate = useNavigate();

    useEffectOnce {
        scope.launch {
            session = Known(getUserSession())
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
        +"This is the page for customizing your soccer watch. (WIP)"
    }
    if (session.know) {
        if (session.value != null) {

            div {
                css {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    alignItems = AlignItems.center
                    fontSize = 2.em
                    fontFamily = FontFamily.monospace
                }
                +"Found your user info and customizing -"
            }
            pre {
                +session.value?.let {
                    JSON.stringify(it, null, 2)
                }
            }


        } else {
            navigate("/nouser")
        }
    } else {
        div {
            css {
                display = Display.flex
                flexDirection = FlexDirection.column
                alignItems = AlignItems.center
                fontSize = 2.em
                fontFamily = FontFamily.monospace
            }
            +"Session is unknown yet, please wait."
        }

    }

}