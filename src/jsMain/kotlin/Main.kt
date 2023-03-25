import react.create
import react.dom.client.createRoot
import web.dom.document
import xyz.nietongxue.soccerTime.App

fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find container!")
    createRoot(container).render(App.create())
}
