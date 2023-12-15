package xyz.nietongxue.soccerTime.custom

import csstype.AlignItems
import csstype.Display
import csstype.FlexDirection
import emotion.react.css
import io.ktor.http.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.*
import react.dom.html.ReactHTML.div
import react.router.useNavigate
import remix.run.router.redirect
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
        }
        +"This is custom page"
    }
    if (session.know) {
        if (session.value != null) {
            div {
                +"session is true"
                +session.toString()
            }
        } else {
            navigate("/nouser")
        }
    } else {
        +"session is unkown"
    }

}