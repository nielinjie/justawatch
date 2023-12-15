import js.core.jso
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.router.RouterProvider
import react.router.dom.createBrowserRouter
import web.dom.document
import xyz.nietongxue.soccerTime.App
import xyz.nietongxue.soccerTime.custom.CustomPage
import xyz.nietongxue.soccerTime.noUser.NoUserPage


val router_ = createBrowserRouter(
    arrayOf(jso {
        path = "/"
        element = App.create()
    }, jso {
        path = "/nouser"
        element = NoUserPage.create()
    }, jso {
        path = "/custom"
        element = CustomPage.create()
    }
    ),
)

val R = FC<Props> {
    RouterProvider {
        this.router = router_
    }
}

fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find container!")
    createRoot(container).render(R.create())
}
